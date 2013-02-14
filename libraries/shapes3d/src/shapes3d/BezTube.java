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
import shapes3d.utils.TubeRadius;
import shapes3d.utils.Bezier3D;
import shapes3d.utils.I_RadiusGen;
import shapes3d.utils.MeshSection;
import shapes3d.utils.Rot;
import shapes3d.utils.VectorUtil;

/**
 * This class represents a tube that follows a Bezier curve in 3D space. <br>
 * The actual tube path is defined by a Bezier3D object
 * 
 * By default the bezier tube will have end caps. Each end cap can have its own <br>
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
public class BezTube extends Mesh2DCoreWithCaps {

	/**
	 *  The bezier used to generate the 3D shape
	 */
	protected Bezier3D bz;

	/**
	 * Tube radius calculator
	 */
	protected I_RadiusGen tRad;
	
	/**
	 * Create a BezTube
	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param rad the radius of the tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 */
	public BezTube(PApplet app, Bezier3D bez, float rad, int nbrSlices, int nbrSegments){
		super();
		ctorCore(app, bez, new TubeRadius(rad), nbrSlices, nbrSegments);
	}

	/**
	 * Create a BezTube
	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param radGen the radius generator for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 */
	public BezTube(PApplet app, Bezier3D bez, I_RadiusGen radGen, int nbrSlices, int nbrSegments){
		super();
		ctorCore(app, bez, radGen, nbrSlices, nbrSegments);
	}
	
	/**
	 * Create a tube. <br>
	 * 
	 * The Bezier control points are recalculated so to change the 'up'
	 * direction from the [0,1,0] axis to the new axis 'orientation' <br>
	 * The Bezier control points are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>

	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param rad the radius for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param orientation the new up direction
	 * @param centreOfRotation new centre of orientation
	 */
	public BezTube(PApplet app, Bezier3D bez, float rad, int nbrSlices, int nbrSegments, 
			PVector orientation, PVector centreOfRotation){
		super();
		ctorCore(app, bez, new TubeRadius(rad), nbrSlices, nbrSegments, orientation, centreOfRotation);
	}

	/**
	 * 
	 * Create a tube. <br>
	 * 
	 * The Bezier control points are recalculated so to change the 'up'
	 * direction from the [0,1,0] axis to the new axis 'orientation' <br>
	 * The Bezier control points are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>

	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param rad the radius generator for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param orientation the new up direction
	 * @param centreOfRotation new centre of orientation
	 */
	public BezTube(PApplet app, Bezier3D bez, I_RadiusGen rad, int nbrSlices, int nbrSegments, 
			PVector orientation, PVector centreOfRotation){
		super();
		ctorCore(app, bez, rad, nbrSlices, nbrSegments, orientation, centreOfRotation);
	}

	/**
	 * Common code for 2 of the constructors
	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param radGen the radius for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 */
	private void ctorCore(PApplet app, Bezier3D bez, I_RadiusGen radGen, int nbrSlices, int nbrSegments){
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;
		this.bz = bez;
		this.tRad = radGen;
		calcShape();
		calcXYZ();
	}
	
	/**
	 * Common code for 2 of the constructors
	 * @param app the PApplet to draw this shape
	 * @param bez the bezier curve to use as the tube centre
	 * @param radGen the radius for this tube
	 * @param nbrSlices number of slices along length
	 * @param nbrSegments number of sections around tube
	 * @param orientation the new up direction
	 * @param centreOfRotation new centre of orientation
	 */
	private void ctorCore(PApplet app, Bezier3D bez, I_RadiusGen radGen, int nbrSlices, int nbrSegments, 
			PVector orientation, PVector centreOfRotation){
		this.app = app;
		nsPieces = nbrSlices;
		ewPieces = nbrSegments;
		this.bz = bez;
		this.tRad = radGen;
		calcShape();
		shapeOrientation(orientation, centreOfRotation);
	}
	
	/**
	 * Change the world position and rotation. For this shape we need to
	 * cahnge the control points and recalculate the mesh.<br>
	 * If these are different from existing values then recalculate the shape.
	 * @param up the axis to use for the 'up' direction
	 * @param centreOfRot centre of rotation position
	 */
	public void shapeOrientation(PVector up, PVector centreOfRot){
		boolean changed = false;
		if(up != null && up.mag() > 0 && !VectorUtil.same(this.up, up)){
			this.up.set(up);
			changed = true;
		}
		if(centreOfRot != null && !VectorUtil.same(this.centreRot, centreOfRot)){
			this.centreRot.set(centreOfRot);
			changed = true;
		}
		if(changed) {
			// Adjust for new up vector & centre of rotation and 
			Rot orrient = new Rot(new PVector(0,1,0), this.up);
			PVector[] ctlPoint = bz.getCtrlPointArray();

			for(int i = 0; i < ctlPoint.length; i++) {
				orrient.applyTo(ctlPoint[i]);
				ctlPoint[i].add(centreRot);
			}
			setBez(new Bezier3D(ctlPoint, ctlPoint.length));
		}
		calcXYZ();
	}
	
	/**
	 * Used internally to calculate the shape. <br>
	 * Create the arrays necessary for the coordinates and normals
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
	}

	/**
	 * Used internally to calculate the shape. <br>
	 * 
	 * Calculate the coordinates and normals for the BezTube.
	 */
	protected void calcXYZ() {
//		float a = 0, da = TWO_PI / (ewSteps - 1.0f);
//		PVector[] points = bz.points(nsSteps);
//		PVector[] tangents = bz.tangents(nsSteps);
//
//		// Calculate the points on a circle of radius 1
//		PVector[] circle = new PVector[ewSteps];
//		for(int i = 0; i < ewSteps - 1; i++){
//			circle[i] = new PVector((float)Math.cos(a), (float)Math.sin(a), 0);
//			a -= da;
//		}	
//		circle[ewSteps-1] = new PVector(circle[0].x, circle[0].y, 0);
//		PVector axis = new PVector(0,0,1);
//		
//		Rot rot;	
//		PVector pv;
//
//		float t = 0.0f, dt = 1.0f/(nsSteps -1), rad;
//		
//		for(int ns = 0; ns < nsSteps ; ns++){
//			rad = tRad.radius(t); // Get radius at this position
//			rot = new Rot(axis, tangents[ns]);
//			for(int ew = 0; ew < ewSteps - 1; ew++) {
//				// Rotate circle to new tangent
//				pv = new PVector(circle[ew].x, circle[ew].y, circle[ew].z);
//				rot.applyTo(pv);
//				pv.normalize();
//				pv.mult(rad);
//				pv.add(points[ns]);
//				coord[ew][ns] = pv;
//				a -= da;
//			}
//			// First and last coordinate
//			coord[ewSteps-1][ns] = new PVector(coord[0][ns].x, coord[0][ns].y, coord[0][ns].z);
//			t += dt;
//		}
		
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
		startEC.calcShape(end2, ewSteps, 1);
		endEC.calcShape(end1, ewSteps, -1);
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
		PVector orgAxis = new PVector(0,1,0);
		// Calculate the rotation to move axis from (0,1,0) to new axis

		if(!VectorUtil.same(orgAxis, axis)){
			Rot rot = new Rot(new PVector(0,1,0), axis);
			for(int i = 0; i < ewSteps; i++){
				rot.applyTo(circle[i]);
			}		
		}
		return circle;
	}

	/**
	 * Change the bezier path and radius for the tube.
	 * @param bez the 3D bezier curve
	 * @param radius value for constant size tube radius
	 */
	public void setDetails(Bezier3D bez, float radius){
		I_RadiusGen arad = new TubeRadius(radius);
		setDetails(bez, arad);
	}

	/**
	 * Change the bezier path and radius for the tube.
	 * @param bez the 3D bezier curve
	 * @param rad tube radius size calculator to use
	 */
	public void setDetails(Bezier3D bez, I_RadiusGen rad){
		while(drawLock){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		drawLock = true;
		this.bz = bez;
		tRad = rad;
		shapeOrientation(up , centreRot);
		drawLock = false;
	}

	public void setRadius(float rad){
		TubeRadius nrad = new TubeRadius(rad);
		setRadius(nrad);
	}
	
	public void setRadius(I_RadiusGen rad){
		while(drawLock){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		drawLock = true;
		tRad = rad;
		calcXYZ();
		drawLock = false;		
	}
	
	/**
	 * Change the bezier path for the tube with existing radius.
	 * @param bez
	 */
	public void setBez(Bezier3D bez){
		while(drawLock){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		drawLock = true;
		this.bz = bez;
		shapeOrientation(up , centreRot);
		drawLock = false;
	}
	
	/**
	 * Get a reference to the Bezier3D object used for this tube.
	 * 
	 * @return Bezier3D object
	 */
	public Bezier3D getBez(){
		return bz;
	}

	/**
	 * Get the tube radius at a given point along the tube.
	 * 
	 * @param t >=0 and<=1.0
	 * @return tube radius
	 */
	public float getRadius(float t){
		return tRad.radius(t);
	}
	
	/**
	 * Get the object resposible for profiling the tube.
	 * 
	 * @return the profiler.
	 */
	public I_RadiusGen getRadiusProfiler(){
		return tRad;
	}
	
	/**
	 * Get the tangent to the tube at a given point along the tube.
	 * 
	 * @param t >=0 and<=1.0
	 * @return tangent vector for position t
	 */
	public PVector getTangent(float t){
		return bz.tangent(t);
	}
	
	/**
	 * Get the tangents along the path
	 * @param steps
	 * @return an array of tangent PVector(s)
	 */
	public PVector[] getTangents(int steps){
		return bz.tangents(steps);
	}
	
	/**
	 * Get the centre point of the tube at a given point along the tube.
	 * 
	 * @param t >=0 and<=1.0
	 * @return the point on the Bezier curve for position t
	 */
	public PVector getPoint(float t){
		return bz.point(t);
	}
	
	/**
	 * Get the tube centre points along the path
	 * @param steps
	 * @return an array of tube centres PVector(s)
	 */
	public PVector[] getPoints(int steps){
		return bz.points(steps);
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
	 * In fact the normal to a Bezier curve in 3D is not a vector but rather
	 * a plane. This does an approximation by interpolating between the normal 
	 * vectors (created when the BezTube was instantiated) either side of 't'. 
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
//				drawNormals();
			}
			app.popMatrix();
			app.popStyle();
			drawLock = false;
		}
	}

}
