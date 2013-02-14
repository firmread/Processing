/*
  Part of the Shapes 3D library for Processing 
  	http://www.lagers.org.uk

  Copyright (c) 2009 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package shapes3d;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;
import shapes3d.utils.MeshSection;
import shapes3d.utils.Rot;

/**
 * This class represents a helix along the Y axis. <br>
 * Although the helix is circular as viewed along it's length, the 'tube' cross-section
 * can be elliptical by setting different radii in the vertical and horizontal directions. <br>
 * The centre of rotation is the centre of the helix and half way along its length. <br>
 * 
 * By default the helix will have end caps. Each end cap can have its own <br>
 * <ul>
 * <li>draw mode</li>
 * <li>fill color</li>
 * <li>stroke color</li>
 * <li>stroke weight</li>
 * <li>texture</li>
 * <li>visibility</li>
 * </ul>
 * The methods to set the end cap properties can be found in the {@link Mesh2DCoreWithCaps} 
 * class documentation. The following constants should be used when specifying which cap(s)
 * are to be affected.
 * Helix.S_CAP  Helix.E_CAP  Helix.BOTH_CAP 
 * 
 * @author Peter Lager
 *
 */
public class Helix extends Mesh2DCoreWithCaps {
	
	protected float tubeRadX = 5.0f;	// tube radius
	protected float tubeRadY = 5.0f;	// tube radius
	protected float ringRad = 120.0f;

	protected float nbrTwirls = 4;
	protected float pitch = 15.0f;

	/**
	 * Create the helix. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrTubeSegments number of sections around tube
	 * @param nbrLengthSegments number of sections along length
	 */
	public Helix(PApplet app, int nbrTubeSegments, int nbrLengthSegments){
		super();
		this.app = app;
		nsPieces = nbrTubeSegments;
		ewPieces = nbrLengthSegments;
		calcShape();
	}

	/**
	 * Create the helix. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrTubeSegments number of sections around tube
	 * @param nbrLengthSegments number of sections along length
	 * @param up
	 * @param centreOfRot
	 */
	public Helix(PApplet app, int nbrTubeSegments, int nbrLengthSegments, 
			PVector up, PVector centreOfRot){
		super();
		this.app = app;
		nsPieces = nbrTubeSegments;
		ewPieces = nbrLengthSegments;
		if(up != null && up.mag() > 0)
			this.up.set(up);
		if(centreOfRot != null)
			this.centreRot.set(centreOfRot);
		calcShape();
	}
	
	/**
	 * Draw the helix.
	 */
	public void draw(){
		// Draw the main body of the tube
		super.draw();
		// Now draw the end caps
		app.pushStyle();
		app.pushMatrix();
		if(pickModeOn){
			if(visible && pickable){
				if(startEC.visible  && startEC.capDrawMode != WIRE)
					startEC.drawForPicker(pickBuffer);
				if(endEC.visible  && endEC.capDrawMode != WIRE)
					endEC.drawForPicker(pickBuffer);
			}
		}
		else {
			if(visible){
				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);
				app.fill(fillColor);

				if(visible){
					if(startEC.visible)
						startEC.draw();
					if(endEC.visible)
						endEC.draw();
				}

				if(children != null){
					Iterator<Shape3D> iter = children.iterator();
					while(iter.hasNext())
						iter.next().draw();
				}
				//drawNormals();
			}
		}
		app.popMatrix();
		app.popStyle();
	}

	/**
	 * Set the tube radii as well as the ring radius.
	 * @param tubeX
	 * @param tubeY
	 * @param lathe
	 */
	public void setRadius(float tubeX, float tubeY, float lathe){
		tubeRadX = tubeX;
		tubeRadY = tubeY;
		ringRad = lathe;
		calcXYZ();
	}

	/** 
	 * Set the numbers of turns as well as the pitch (the amount of offset
	 * on each full circumference. <br>
	 * 
	 * @param twirls
	 * @param pitch
	 */
	public void setHelix(float twirls, float pitch){
		this.nbrTwirls = twirls;
		this.pitch = pitch;
		calcXYZ();
	}

	/**
	 * Used internally to calculate the shape.
	 */
	@Override
	protected void calcShape(){
		nsSteps = nsPieces + 1;
		ewSteps = ewPieces + 1;
		coord = new PVector[ewSteps][nsSteps];
		norm = new PVector[ewSteps][nsSteps];
		fullShape = new MeshSection(ewSteps, nsSteps);
		startEC = new EndCap();
		endEC = new EndCap();
		calcXYZ();
	}

	/**
	 * Used internally to calculate the shape.
	 */
	protected void calcXYZ(){
		float tubeFactor = TWO_PI / (nsSteps - 1);
		float ringFactor = (nbrTwirls * TWO_PI) / (ewSteps - 1);

		float tAng = -HALF_PI, rAng = -ringFactor;
		float tsin, tcos;
		
		// First tube ellipse about XY axis
		for(int i = 0; i < nsSteps; i++){
			tsin = (float) Math.sin(tAng);
			tcos = (float) Math.cos(tAng);
			coord[0][i] = new PVector(
					tubeRadX * tcos,
					tubeRadY * tsin ,
					0);
			norm[0][i] = new PVector(
					tubeRadX * tcos,
					tubeRadY * tsin,
					0);
			norm[0][i].normalize();
			tAng += tubeFactor;
		}

		// centre helix on position for rotations
		float offsetY = -pitch*nbrTwirls/2.0f;
		
		PVector n,c;
		float pitchAngle = (float) Math.atan2(pitch, TWO_PI * ringRad);
		float psin = (float) Math.sin(-pitchAngle);
		float pcos = (float) Math.cos(-pitchAngle);
		
		// Tilt about x axis for pitch
		for(int i = 0; i < nsSteps; i++){
			c = coord[0][i];
			coord[0][i] = new PVector(c.x + ringRad,
					c.z*psin + c.y*pcos + offsetY,
						c.z*pcos - c.y*psin);
			n = norm[0][i];
			norm[0][i] = new PVector(n.x,
					 n.z*psin + n.y*pcos, n.z*pcos - n.y*psin);
		}
		
		PVector orgVec, orgNorm;
		float  sinA, cosA;
		rAng = -ringFactor;
		for(int i = 1; i < ewSteps; i++){
			sinA = (float) Math.sin(rAng);
			cosA = (float) Math.cos(rAng);
			for(int j = 0; j < nsSteps; j++){
				orgVec = coord[0][j];
				coord[i][j] = new PVector(
						orgVec.x * cosA - orgVec.z * sinA,
						orgVec.y - pitch * rAng / TWO_PI,
						orgVec.x * sinA + orgVec.z * cosA);
				orgNorm = norm[0][j];
				norm[i][j] = new PVector(
						orgNorm.x * cosA - orgNorm.z * sinA,
						orgNorm.y,
						orgNorm.x * sinA + orgNorm.z * cosA);						
			}
			rAng -= ringFactor;
		}
		
		// Orient and move centre of rotation if necessary
		Rot orrient = new Rot(new PVector(0,1,0), up);
		for(int i = 0; i < ewSteps; i++) {
			for(int j = 0; j < nsSteps ; j++){
				orrient.applyTo(coord[i][j]);
				coord[i][j].add(this.centreRot);
				orrient.applyTo(norm[i][j]);
				norm[i][j].normalize();
			}
		}
		
		// Now sort out end caps
		PVector[] end1 = new PVector[nsSteps];
		PVector[] end2 = new PVector[nsSteps];
		PVector ep;
		for(int i = 0; i < nsSteps; i++){
			ep = coord[0][i];
			end1[i] = new PVector(ep.x, ep.y, ep.z);
			ep = coord[ewPieces][i];
			end2[i] = new PVector(ep.x, ep.y, ep.z);
		}
		startEC.calcShape(end1, nsSteps, 1);
		endEC.calcShape(end2, nsSteps, -1);
	}

}
