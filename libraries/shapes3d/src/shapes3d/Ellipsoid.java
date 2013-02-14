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
 * An ellipsoid is a spherical shape where the radius in the 3 primary directions (x,y,z)
 * are not necessarily the same. If they are the same then we have a sphere. <br>
 * The centre of rotation is the centre of the ellipsoid. <br>
 *  
 * @author Peter Lager
 *
 */
public class Ellipsoid extends Mesh2DCore {

	protected float radX, radY, radZ;

	/**
	 * Create an ellipsoid shape.
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSlices north-south
	 * @param nbrSegments east-west
	 */
	public Ellipsoid(PApplet app, int nbrSlices, int nbrSegments){
		super();
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;
		radX = radY = radZ = 50;
		calcShape();
	}
	
	/**
	 * Create an ellipsoid shape.
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSlices
	 * @param nbrSegments
	 * @param up
	 * @param centreOfRot
	 */
	public Ellipsoid(PApplet app, int nbrSlices, int nbrSegments, 
			PVector up, PVector centreOfRot){
		super();
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;
		radX = radY = radZ = 50;
		if(up != null && up.mag() > 0)
			this.up.set(up);
		if(centreOfRot != null)
			this.centreRot.set(centreOfRot);
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
	 * Used internally to calculate the coordinates and normals.
	 */
	protected void calcXYZ(){
		float angNS = 0, angEW = 0;
		float angNSfactor = PI / (nsSteps - 1);
		float andEWfactor = TWO_PI / (ewSteps - 1);
		float sinNS, cosNS;

		Rot orrient = new Rot(new PVector(0,1,0), up);
		PVector n, c;
		for(int p = 0; p < nsSteps; p++) {
			angEW = 0.0f;
			sinNS = (float) Math.sin(angNS);
			cosNS = (float) Math.cos(angNS);
			for(int t = 0; t < ewSteps; t++) {
				c = new PVector( 
						radX * sinNS * (float) Math.cos(angEW), 	// x
						-radY * cosNS,								// y
						radZ * sinNS * (float) Math.sin(angEW)) ;	// z
				// Set orrientation
				coord[t][p] = orrient.applyTo(c);
				c = coord[t][p];
				n = new PVector();
				norm[t][p] = n;
				n.x = c.x;
				n.y = c.y;
				n.z = c.z;
				n.normalize();
				coord[t][p].add(centreRot);
				angEW -= andEWfactor;
			}
			angNS += angNSfactor;
		}		
	}

	/**
	 * Set the radii for each axis
	 * @param radiusX
	 * @param radiusY
	 * @param radiusZ
	 */
	public void setRadius(float radiusX, float radiusY, float radiusZ){
		this.radX = radiusX;
		this.radY = radiusY;
		this.radZ = radiusZ;
		calcXYZ();
	}
	
	/**
	 * Set the radii for each axis.
	 * @param radiusX
	 * @param radiusY
	 * @param radiusZ
	 */
	public void setSize(float radiusX, float radiusY, float radiusZ){
		this.radX = radiusX;
		this.radY = radiusY;
		this.radZ = radiusZ;
		calcXYZ();
	}

	/**
	 * Set the radius for all three axis (sphere)
	 * @param radius
	 */
	public void setRadius(float radius){
		this.radX = this.radY = this.radZ = radius;
		calcXYZ();
	}
	
	/**
	 * Set the radius for all three axis (sphere)
	 * @param radius
	 */
	public void setSize(float radius){
		this.radX = this.radY = this.radZ = radius;
		calcXYZ();
	}

}
