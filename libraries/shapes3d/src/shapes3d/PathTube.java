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
import shapes3d.utils.I_PathGen;
import shapes3d.utils.I_RadiusGen;
import shapes3d.utils.MeshSection;
import shapes3d.utils.Rot;
import shapes3d.utils.TubeRadius;
import shapes3d.utils.VectorUtil;

public class PathTube extends Mesh2DCoreWithCaps {

	/**
	 * <b>Warning: This should be changed as a last resort</b><br>
	 * It is used when calculating the tangent to the path 
	 * Although the user specifies 3 functions to determine 
	 * the x,y,z coordinates of a point it would be difficult 
	 * for the user to specify the formulae to calculate the
	 * tangent. So this is done by approximation, the tangent
	 * at the point p(t) is given by the slope of the line 
	 * between points p(t-dTan) and p(t+dTan). <br>
	 */
	public float dTan = 0.01f;

	/**
	 * Path generator
	 */
	protected I_PathGen tPath;

	/**
	 * tube radius generator
	 */
	protected I_RadiusGen tRad;

	/**
	 * If ends are very close but not a perfect match
	 * set this to true when the object is instantiated.
	 */
	protected boolean joinEnds = false;

	/**
	 * Create a tube with constant radius. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param pathGen the path generator for this shape
	 * @param rad the tube radius
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param continuous true if the two ends meet
	 * @param arguments optional integer value in range 1-7 inclusive (if missing a value of 2 is used)
	 */
	public PathTube(PApplet app, I_PathGen pathGen, float rad, 
			int nbrSlices, int nbrSegments, boolean continuous){
		super();
		ctorCore(app, pathGen, new TubeRadius(rad), nbrSlices, nbrSegments, 
				continuous);
	}

	/**
	 * Create a tube with radius defined by a generator
	 * 
	 * @param app the PApplet to draw this shape
	 * @param pathGen the path generator for this shape
	 * @param radGen the radius generator for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param continuous true if the two ends meet
	 * @param arguments optional integer value in range 1-7 inclusive (if missing a value of 2 is used)
	 */
	public PathTube(PApplet app, I_PathGen pathGen, I_RadiusGen radGen, 
			int nbrSlices, int nbrSegments, boolean continuous){
		super();
		ctorCore(app, pathGen, radGen, nbrSlices, nbrSegments, continuous);
	}

	/**
	 * Common code for 2 of the constructors
	 * 
	 * @param app the PApplet to draw this shape
	 * @param pathGen the path generator for this shape
	 * @param radGen the radius generator for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 */
	private void ctorCore(PApplet app, I_PathGen pathGen, I_RadiusGen radGen, 
			int nbrSlices, int nbrSegments, boolean continuous){
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;
		tPath = pathGen;
		tRad = radGen;
		joinEnds = continuous;
		calcShape();
	}

	/**
	 * Used internally to calculate the shape. <br>
	 * Create the arrays necessary for the coordinates and normals
	 */
	@Override
	protected void calcShape(){
		nsSteps = nsPieces + 1;
		ewSteps = ewPieces + 1;
		dTan = 0.001f / nsPieces;
		coord = new PVector[ewSteps][nsSteps];
		norm = new PVector[ewSteps][nsSteps];
		fullShape = new MeshSection(ewSteps, nsSteps);
		startEC = new EndCap();
		endEC = new EndCap();
		calcXYZ();
	}

	/**
	 * Used internally to calculate all the XYZ coordinates
	 */
	protected void calcXYZ() {
		// Get a circle of radius 1 rotating about axis (0,1,0)
		PVector[] circle = new PVector[ewSteps];
		PVector axis = new PVector(0,1,0); // can change this axis
		circle = createCircleAboutAxis(axis);
		// ======================================================

		Rot rot;	
		PVector pv;
		PVector[] points = getPoints(nsSteps);
		PVector[] tangents = getTangents(nsSteps);
		float t = 0.0f, dt = 1.0f/(nsSteps -1), rad;

		// Calculate the first ring (t=0)
		rot = new Rot(axis, tangents[0]);
		rad = tRad.radius(t); // Get radius at this position
		for(int ew = 0; ew < ewSteps; ew++) {
			// Rotate circle to new tangent
			pv = new PVector(circle[ew].x, circle[ew].y, circle[ew].z);
			rot.applyTo(pv);
			pv.normalize();
			pv.mult(rad);
			pv.add(points[0]);
			coord[ew][0] = pv;
		}

		// Calculate the remaining rings
		for(int ns = 1; ns < nsSteps ; ns++){
			rad = tRad.radius(t); // Get radius at this position
			rot = new Rot(tangents[ns-1], tangents[ns]);
			for(int ew = 0; ew < ewSteps; ew++) {
				// Rotate circle to new tangent
				pv = PVector.sub(coord[ew][ns-1], points[ns - 1]);
				rot.applyTo(pv);
				pv.normalize();
				pv.mult(rad);
				pv.add(points[ns]);
				coord[ew][ns] = pv;
			}
			t += dt;
		}

		// If ends are to meet average out end points
		PVector t0, t1, mid; 
		if(this.joinEnds){
			t0 = getTangent(0);
			t1 = getTangent(1);
			mid = PVector.add(t0,t1);
			mid.div(2);
			mid.normalize();
			Rot rot0 = new Rot(t0, mid);	
			Rot rot1 = new Rot(t1, mid);	
			for(int i = 0; i < ewSteps; i++){
				rot0.applyTo(coord[i][0]);
				rot1.applyTo(coord[i][nsPieces]);
			}
		}
		// Select appropriate method to calculate normals
		if(tRad.hasConstantRadius())
			calcNormals(points);
		else
			calcNormals();

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
		startEC.calcShape(end2, ewSteps, -1);
		endEC.calcShape(end1, ewSteps, 1);
	}

	protected PVector[] createCircleAboutAxis(final PVector axis){
		PVector[] circle = new PVector[ewSteps];
		float a = 0, da = TWO_PI / (ewSteps - 1.0f);
		// First create the circle about the (0,1,0) axis
		for(int i = 0; i < ewSteps - 1; i++){
			circle[i] = new PVector((float)Math.cos(a), 0, (float)Math.sin(a));
			a -= da;
		}	
		circle[ewSteps-1] = new PVector();
		circle[ewSteps-1].set(circle[0]);

		/*
		 * Calculate the rotation to move axis from (0,1,0) to new axis
		 * At present the axis is always 0,1,0 so this is never true it has been
		 * left just in case I need it later
		 * 
		PVector orgAxis = new PVector(0,1,0);
		if(!VectorUtil.same(orgAxis, axis)){
			Rot rot = new Rot(new PVector(0,1,0), axis);
			for(int i = 0; i < ewSteps; i++){
				rot.applyTo(circle[i]);
			}		
		}
		 
		*/
		
		return circle;
	}

	/**
	 * Simplified version for calculating normals and is only used
	 * when the BezTube is of constant radius. <br>
	 * For variable profile tubes the calcNormals() in Mesh2DCore class is used. 
	 */
	protected void calcNormals(PVector[] points) {
		for(int ns = 0; ns < nsSteps ; ns++){
			for(int ew = 0; ew < ewSteps; ew++) {
				norm[ew][ns] = PVector.sub(coord[ew][ns], points[ns]);
				norm[ew][ns].normalize();
			}
		}
	}

	/**
	 * Get the tangents along the path
	 * @param steps
	 * @return an array of tangent PVector(s)
	 */
	public PVector[] getTangents(int steps){
		PVector[] tangents = new PVector[steps];
		float deltaT = 1.0f/(steps - 1);
		float t = 0;
		for(int i = 0; i < steps; i++){
			tangents[i] = getTangent(t);
			t += deltaT;
		}
		return tangents;
	}

	/**
	 * Get the tangent to the tube at a given point along the tube. <br>
	 * This calculates an approximation to the point specified. See 
	 * {@link #dTan} for details
	 * 
	 * @param t >=0 and <=1.0
	 * @return tangent vector for position t
	 */
	public PVector getTangent(float t){
		t = PApplet.constrain(t, 0, 1);		
		float preT = t - dTan, postT = t + dTan;
		preT = (preT < 0) ? 0 : preT;
		postT = (postT > 1.0) ? 1 : postT;

		PVector tangent = new PVector();
		tangent.x = tPath.x(postT) - tPath.x(preT);
		tangent.y = tPath.y(postT) - tPath.y(preT);
		tangent.z = tPath.z(postT) - tPath.z(preT);

		float mag = tangent.mag();
		if(mag < 1e-4) dTan *= 2;
		tangent.div(mag);
		return tangent;
	}

	/**
	 * Get the tube centre points along the path
	 * @param steps
	 * @return an array of tube centres PVector(s)
	 */
	public PVector[] getPoints(int steps) {
		PVector[] points = new PVector[steps];
		float deltaT = 1.0f/(steps - 1);
		float t = 0;
		for(int i = 0; i < steps; i++){
			points[i] = getPoint(t);
			t += deltaT;
		}
		return points;
	}

	/**
	 * Get the centre point of the tube at a given point along the tube.
	 * 
	 * @param t >=0 and<=1.0
	 * @return the point on the Bezier curve for position t
	 */
	public PVector getPoint(float t) {
		return new PVector(tPath.x(t), tPath.y(t), tPath.z(t));
	}

	/**
	 * Get the normals along the path See getNormal(float) for more details.
	 * @param steps
	 * @return an array of normal PVector(s)
	 */
	public PVector[] getNormals(int steps) {
		PVector[] normals = new PVector[steps];
		float deltaT = 1.0f/(steps - 1);
		float t = 0;
		for(int i = 0; i < steps; i++){
			normals[i] = getNormal(t);
			t += deltaT;
		}
		return normals;
	}

	/**
	 * Get the normal vector of the tube at a given point along the tube. <br>
	 * In fact the normal to a parametric curve in 3D is not a vector but rather
	 * a plane. This does an approximation by interpolating between the normal 
	 * vectors (created when the PathTube was instantiated) either side of 't'. 
	 * <br>
	 * 
	 * @param t >=0 and <1
	 * @return the normal PVector at this point
	 */
	public PVector getNormal(float t){
		t = PApplet.constrain(t, 0.0f, 0.999999f);
		float floatIndex = t * nsPieces;
		int index  = (int) floatIndex;
		float ft = floatIndex - index;
		// Fix normal for smooth transition
		PVector normal = PVector.sub(norm[0][index + 1], norm[0][index]);
		normal.mult(ft);
		normal.add(norm[0][index]);
		return normal;		
	}

	public void setPath(I_PathGen newPath){
		if(newPath != null && newPath != tPath){
			tPath = newPath;
			calcXYZ();
		}	
	}
	
	/**
	 * If the shape is being changed dynamically then the coordinates 
	 * can become corrupted. Calling this method will recalculate all 
	 * the mesh coordinates and normals.
	 */
	public void restoreShape(){
		calcXYZ();
	}

	/**
	 * Draw the tube.
	 */
	public void draw(){
		if(visible && !drawLock){
			drawLock = true;
			// Draw the main body of the tube
			super.draw();
			// Now draw the end caps
			app.pushStyle();
			app.pushMatrix();
			if(pickModeOn){
				if(pickable && startEC.visible  && startEC.capDrawMode != WIRE)
					startEC.drawForPicker(pickBuffer);
				if(pickable && endEC.visible  && endEC.capDrawMode != WIRE)
					endEC.drawForPicker(pickBuffer);
			}
			else {
				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);
				app.fill(fillColor);

				if(startEC.visible)
					startEC.draw();
				if(endEC.visible)
					endEC.draw();

				if(children != null){
					Iterator<Shape3D> iter = children.iterator();
					while(iter.hasNext())
						iter.next().draw();
				}
				//	drawNormals();
			}
			app.popMatrix();
			app.popStyle();
			drawLock = false;
		}
	}

	/**
	 * Calculate the path length of a section of PathTube curve. <br>
	 * There is no formula for the exact length of a PathTube curve so any method
	 * will always be approximate. <br>
	 * This method will always 'underestimate' the actual path length. It works
	 * by subdividing the PathTube curve into a number of straight line segments
	 * and summing their lengths. <br>
	 * The number of subdivisions can be increased to improve the accuracy of the 
	 * result but at the cost of additional computation. <br>
	 * You may want to experiment but 100+ subdivisions normally give quite good 
	 * results. <br>
	 * 
	 * @param t0 start ( >=0.0 and  <t1 )
	 * @param t1 end ( >t0 and <=1.0 )
	 * @param steps number of subdivisions
	 * @return length of the curve section
	 */
	public float length(float t0, float t1, int steps){
		t0 = PApplet.constrain(t0, 0.0f, 1.0f);
		t1 = PApplet.constrain(t1, 0.0f, 1.0f);
		float dist = (t0 == t1)? 0 : -1;
		if(steps > 1){
			if(t0 > t1){
				float temp = t0;
				t0 = t1;
				t1 = temp;
			}
			float t = t0, deltaT = 1.0f / (steps - 1);
			PVector[] points = new PVector[steps];
			for(int i = 0; i < steps; i++){
				points[i] = getPoint(t);
				t += deltaT;
			}

			dist  = PVector.dist(points[0], points[1]);
			for(int i = 2; i < steps; i++)
				dist += PVector.dist(points[i-1], points[i]);
		}
		return dist;
	}
	
	/**
	 * Calculate the path length of the PathTube curve. <br>
	 * There is no formula for the exact length of a PathTube curve so any method
	 * will always be approximate. <br>
	 * This method will always 'underestimate' the actual path length. It works
	 * by subdividing the PathTube curve into a number of straight line segments
	 * and summing their lengths. <br>
	 * The number of subdivisions can be increased to improve the accuracy of the 
	 * result but at the cost of additional computation. <br>
	 * You may want to experiment but 200 subdivisions normally give quite good 
	 * results. <br>
	 * 
	 * @param steps number of subdivisions
	 * @return length of the curve
	 */
	public float length(int steps){
		float dist = -1;
		if(steps > 1){
			PVector[] points = getPoints(steps - 1);
			dist  = PVector.dist(points[0], points[1]);
			for(int i = 2; i < points.length; i++)
				dist += PVector.dist(points[i-1], points[i]);
		}
		return dist;
	}

	/**
	 * At present this is not available for this shape since it would
	 * need to modify the user defined path generator.
	 */
	public void shapeOrientation(PVector up, PVector centreOfRot){	
		System.out.println("The PathTube c;lass does not changing shape orientation!");
	}
}
