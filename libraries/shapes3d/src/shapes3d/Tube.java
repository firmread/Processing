/*
  Part of the Shapes 3D library for Processing 
  	http://www.lagers.org.uk

  Copyright (c) 2010 Peter Lager

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
import shapes3d.utils.RotOrder;

/**
 * This class represents a tube its length along the y [0,1,0] axis. <br>
 * The tube can be 'deformed' by setting the radii (x & z axis) at each end as
 * well as the height. <br>
 * The centre of rotation is the centre of the tube halfway along its length. <br>
 * 
 * By default the tube will have end caps. Each end cap can have its own <br>
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
 * Tube.S_CAP  Tube.E_CAP  Tube.BOTH_CAP 
 * 
 * @author Peter Lager
 *
 */
public class Tube extends Mesh2DCoreWithCaps {

	protected float radXbot, radZbot, radXtop, radZtop;
	protected float height;

	/**
	 * Create a tube. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 */
	public Tube(PApplet app, int nbrSlices, int nbrSegments){
		super();
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;

		radXbot = radZbot = 60;
		radXtop = 60; radZtop = 60;
		height = 120;
		calcShape();
	}

	/**
	 * Create a tube. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param orrientation
	 * @param centreOfRotation
	 */
	public Tube(PApplet app, int nbrSlices, int nbrSegments, 
			PVector orrientation, PVector centreOfRotation){
		super();
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;

		radXbot = radZbot = 60;
		radXtop = 60; radZtop = 60;
		height = 120;
		if(orrientation != null && orrientation.mag() > 0)
			this.up.set(orrientation);
		if(centreOfRotation != null)
			this.centreRot.set(centreOfRotation);
		calcShape();
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
	 * Set the radii at each end and the length for the tube.
	 * @param rTopX
	 * @param rTopZ
	 * @param rBotX
	 * @param rBotZ
	 * @param height
	 */
	public void setSize(float rTopX, float rTopZ, float rBotX, float rBotZ, float height){
		this.radXtop = rTopX;
		this.radZtop = rTopZ;
		this.radXbot = rBotX;
		this.radZbot = rBotZ;
		this.height = height;
		calcXYZ();
	}

	/**
	 * Set the radii at each end of the tube.
	 * @param rTopX
	 * @param rTopZ
	 * @param rBotX
	 * @param rBotZ
	 */
	public void setSize(float rTopX, float rTopZ, float rBotX, float rBotZ){
		this.radXtop = rTopX;
		this.radZtop = rTopZ;
		this.radXbot = rBotX;
		this.radZbot = rBotZ;
		calcXYZ();
	}

	/**
	 * Create a tube that connects 2 points in 3D space
	 * @param startPos
	 * @param endPos
	 */
	public void setWorldPos(PVector startPos, PVector endPos){
		float[] angles = new float[]{0,0,0};
		//	    start
		pos = PVector.add(startPos, endPos);
		pos.div(2);
		height = PVector.dist(startPos, endPos);
		calcXYZ();
		Rot rotter = new Rot( new PVector(0,1,0), PVector.sub(endPos, pos));
		try{
			angles = rotter.getAngles(RotOrder.XYZ);
		}
		catch(Exception excp){
			PApplet.println("Check your start and end position vectors"); 
		}
		rot.x = angles[0];
		rot.y = angles[1];
		rot.z = angles[2];
	}

	/**
	 * Create a tube that connects 2 points in 3D space
	 * @param x0
	 * @param y0
	 * @param z0
	 * @param x1
	 * @param y1
	 * @param z1
	 */
	public void setWorldPos(float x0, float y0, float z0, float x1, float y1, float z1){
		setWorldPos(new PVector(x0,y0,z0), new PVector(x1,y1,z1));
	}

	/**
	 * Used internally to calculate the shape.
	 */
	protected void calcXYZ() {
		float a = 0, da = TWO_PI / (ewSteps - 1.0f);
		float rx, startRadX = radXtop, deltaRadX = (radXbot - radXtop)/(nsSteps - 1.0f);
		float rz, startRadZ = radZtop, deltaRadZ = (radZbot - radZtop)/(nsSteps - 1.0f);
		float hy, startY = -height/2.0f, deltaY = height/(nsSteps - 1.0f);
		int ew, ns;

		for(ew = 0; ew < ewSteps; ew++){
			rx = startRadX;
			rz = startRadZ;
			hy = startY;
			for(ns = 0; ns< nsSteps; ns++){
				coord[ew][ns] = new PVector(rx * (float) Math.cos(a), hy, rz * (float) Math.sin(a));
				rx += deltaRadX;
				rz += deltaRadZ;
				hy += deltaY;
			}
			a -= da;
		}
		calcNormals();

		// Adjust for new up vector & centre of rotation and 
		Rot orrient = new Rot(new PVector(0,1,0), up);
		for(int i = 0; i < ewSteps; i++) {
			for(int j = 0; j < nsSteps ; j++){
				orrient.applyTo(coord[i][j]);
				coord[i][j].add(centreRot);
				orrient.applyTo(norm[i][j]);
				norm[i][j].normalize();
			}
		}
		// Now sort out end caps
		PVector[] end1 = new PVector[ewSteps];
		PVector[] end2 = new PVector[ewSteps];
		PVector ep;
		for(int i = 0; i < ewSteps; i++){
			ep = coord[i][0];
			end1[i] = new PVector(ep.x, ep.y, ep.z);
			ep = coord[i][nsPieces];
			end2[i] = new PVector(ep.x, ep.y, ep.z);
		}
		startEC.calcShape(end1, ewSteps, -1);
		endEC.calcShape(end2, ewSteps, 1);
	}
	
	/**
	 * Draw the tube.
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
//				drawNormals();
			}
		}
		app.popMatrix();
		app.popStyle();
	}

}