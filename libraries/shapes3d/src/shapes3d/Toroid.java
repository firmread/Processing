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

import processing.core.PApplet;
import processing.core.PVector;
import shapes3d.utils.MeshSection;
import shapes3d.utils.Rot;

/**
 * This class represents a toroid (doughnut / donut shape) <br>
 * Although the toroid is circular as viewed from the end, the 'tube' cross-section
 * can be elliptical by setting different radii in the vertical and horizontal directions. <br>
 * The centre of rotation is the centre of the toroid with the 'tube' rotating round 
 * the y [0,1,0] axis. <br>
 * 
 * @author Peter Lager
 *
 */
public class Toroid extends Mesh2DCore {

	protected float tubeRadX = 16.0f;	// tube horz radius
	protected float tubeRadY = 16.0f;	// tube vert radius
	protected float ringRad = 100.0f;	// ring radius

	/**
	 * Create a toroid. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrTubeSegments number of sections around tube
	 * @param nbrLengthSegments number of sections along length
	 */
	public Toroid(PApplet app, int nbrTubeSegments, int nbrLengthSegments){
		super();
		this.app = app;
		nsPieces = nbrTubeSegments;
		ewPieces = nbrLengthSegments;
		calcShape();
	}

	/**
	 * Create a toroid. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrTubeSegments number of sections around tube
	 * @param nbrLengthSegments number of sections along length
	 * @param orrientation
	 * @param centreOfRotation
	 */
	public Toroid(PApplet app, int nbrTubeSegments, int nbrLengthSegments, 
			PVector orrientation, PVector centreOfRotation){
		super();
		this.app = app;
		nsPieces = nbrTubeSegments;
		ewPieces = nbrLengthSegments;
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
		calcXYZ();
	}

	/**
	 * Used internally to calculate the shape.
	 */
	protected void calcXYZ(){
		float tubeFactor = TWO_PI / (nsSteps - 1);
		float ringFactor = TWO_PI / (ewSteps - 1);
		float tAng = -HALF_PI, rAng = -ringFactor;

		for(int i = 0; i < nsSteps; i++){
			coord[0][i] = new PVector(
					ringRad + tubeRadX * PApplet.cos(tAng),
					tubeRadY * PApplet.sin(tAng),
					0);
			norm[0][i] = new PVector(
					tubeRadX * PApplet.cos(tAng),
					tubeRadY * PApplet.sin(tAng),
					0);
			norm[0][i].normalize();
			tAng += tubeFactor;
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
						orgVec.y,
						orgVec.x * sinA + orgVec.z * cosA);
				orgNorm = norm[0][j];
				norm[i][j] = new PVector(
						orgNorm.x * cosA - orgNorm.z * sinA,
						orgNorm.y,
						orgNorm.x * sinA + orgNorm.z * cosA);						
			}
			rAng -= ringFactor;
		}
		
		Rot orrient = new Rot(new PVector(0,1,0), up);
		for(int i = 0; i < ewSteps; i++) {
			for(int j = 0; j < nsSteps ; j++){
				orrient.applyTo(coord[i][j]);
				coord[i][j].add(this.centreRot);
				orrient.applyTo(norm[i][j]);
				norm[i][j].normalize();
			}
		}
	}

	/**
	 * Set the tube radii as well as the ring radius.
	 * @param tubeX
	 * @param tubeY
	 * @param ring
	 */
	public void setRadius(float tubeX, float tubeY, float ring){
		tubeRadX = tubeX;
		tubeRadY = tubeY;
		ringRad = ring;
		calcXYZ();
	}

}
