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

package shapes3d.utils;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class is used to represent a single Bezier curve of degree >= 2 in
 * 3D space. <br>
 * Note the degree of a Bezier curve equals the number of control points. <br>
 * Its primary purpose is to act as a convenience class to maintain a collection
 * bezier controls points in 3 dimensions. <br>
 * 
 * In this library the Bezier3D object is used by the BezTube class to create
 * a tube that bends along  by rotating it about the Y axis [0,1,0].
 *
 * Degree	Shape					<br>
 *   2		straight line			<br>
 *   3		quadratic bezier		<br>
 *   4		cubic bezier			<br>
 *   and so on						<br>
 *   
 * @author Peter Lager
 *
 */
public class Bezier3D {
	
	// Used to hold the control points
	private float[] px;
	private float[] py;
	private float[] pz;

	// Used to hold intermediate values when calculating
	// a point on the curve
	private float[] pxi;
	private float[] pyi;
	private float[] pzi;

	// degree = order + 1  i.e. degree = number of points
	// order = cubic : degree = 4
	private int degree;

	// Used to store intermediate values when calculating
	// the tangent or normal at a point
	private float[] b;

	/**
	 * Create a Bezier object based on points passed as a 2D
	 * array. 
	 * 
	 * @param points 2D array[point no.][x/y]
	 * @param nbrPoints number of points to use from array
	 */
	public Bezier3D(float[][] points, int nbrPoints){
		degree = nbrPoints;
		makeArrays();
		for(int i = 0; i < degree; i++){
			px[i] = points[i][0];
			py[i] = points[i][1];
			pz[i] = points[i][2];
		}
	}

	/**
	 * Create a Bezier object based on an array of vectors. <br>
	 * The bezier curve will use all of the points in the array. <br>
	 * 
	 * @param points array of control points
	 */
	public Bezier3D(PVector[] points, int nbrPoints){
		degree = Math.min(points.length, nbrPoints);
		makeArrays();
		for(int i = 0; i < degree; i++){
			px[i] = points[i].x;
			py[i] = points[i].y;
			pz[i] = points[i].z;
		}
	}
	
	/**
	 * Used by the ctors
	 */
	private void makeArrays(){
		px = new float[degree];
		py = new float[degree];
		pz = new float[degree];
		pxi = new float[degree];
		pyi = new float[degree];
		pzi = new float[degree];
		b = new float[degree];
	}

	/**
	 * Insert a number of points into the bezier curve.
	 * 
	 * @param pts a 2D array of point i.e. [x][y]
	 * @param nbrPts the number of points to insert
	 * @param pos the position to insert (<=0 before first point; >= degree after last point)
	 */
	public void insertCtrlPoints(float[][] pts, int nbrPts, int pos){
		pos = PApplet.constrain(pos, 0, degree);

		int ndegree = degree + nbrPts;
		float[] npx = new float[ndegree];
		float[] npy = new float[ndegree];
		float[] npz = new float[ndegree];
		pxi = new float[ndegree];
		pyi = new float[ndegree];
		pzi = new float[ndegree];
		b = new float[ndegree];
		
		int src = 0, dst;
		// Copy original points up to pos
		for(dst = 0; dst < pos; dst++){
			npx[dst] = px[src];
			npy[dst] = py[src];
			npz[dst] = pz[src];
			src++;
		}
		// Copy new points
		for(int newp = 0; newp < nbrPts; newp++){
			npx[dst] = pts[newp][0];
			npy[dst] = pts[newp][1];	
			npz[dst] = pts[newp][2];	
			dst++;
		}
		// Copy any remaining original points
		for(int i = 0; i < degree - pos; i++){
			npx[dst] = px[src];
			npy[dst] = py[src];
			npz[dst] = pz[src];
			dst++;
			src++;			
		}
		px = npx;
		py = npy;
		pz = npz;
		degree = ndegree;
	}

	/**
	 * Insert a number of points into the bezier curve.
	 * 
	 * @param ptsX array of point x values
	 * @param ptsY array of point y values
	 * @param nbrPts the number of points to insert
	 * @param pos the position to insert (<=0 before first point; >= degree after last point)
	 */
	public void insertCtrlPoints(float[] ptsX, float ptsY[], float ptsZ[], int nbrPts, int pos){
		int no = Math.min(nbrPts, Math.min(ptsX.length, ptsY.length));
		float[][] temp = new float[no][3];
		for(int i = 0; i < no; i++){
			temp[i][0] = ptsX[i];
			temp[i][1] = ptsY[i];
			temp[i][2] = ptsZ[i];
		}
		insertCtrlPoints(temp, no, pos);
	}

	/**
	 * Insert a number of points into the bezier curve.
	 * 
	 * Although using PVector only the x,y values of the vector are used.
	 * 
	 * @param points an array of PVector's 
	 * @param nbrPts the number of points to insert
	 * @param pos the position to insert (<=0 before first point; >= degree after last point)
	 */
	public void insertCtrlPoints(PVector[] points, int nbrPts, int pos){
		int no = PApplet.constrain(nbrPts, 0, points.length);
		float[][] temp = new float[no][3];
		for(int i = 0; i < no; i++){
			temp[i][0] = points[i].x;
			temp[i][1] = points[i].y;			
			temp[i][2] = points[i].z;			
		}
		insertCtrlPoints(temp, no, pos);
	}
	
	/**
	 * Insert a single points into the bezier curve.
	 * 
	 * @param point a single point 
	 * @param pos the position to insert (<=0 before first point; >= degree after last point)
	 */
	public void insertCtrlPoint(PVector point, int pos){
		float[][] temp = new float[1][3];
		temp[0][0] = point.x;
		temp[0][1] = point.y;
		temp[0][2] = point.z;
		insertCtrlPoints(temp, 1, pos);
	}
	
	/**
	 * Insert a single points into the bezier curve.
	 * 
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 * @param pos the position to insert (<=0 before first point; >= degree after last point)
	 */
	public void insertCtrlPoint(float x, float y, float z, int pos){
		float[][] temp = new float[1][3];
		temp[0][0] = x;
		temp[0][1] = y;
		temp[0][2] = z;
		insertCtrlPoints(temp, 1, pos);
	}

	/**
	 * Remove control points from <b>first</b> to <b>last</b> inclusive. <br>
	 * Silently fails if first or last are outside array bounds or last < first or if it means the
	 * number of points left would be < 2
	 * 
	 * @param first the first point to be removed
	 * @param last the last point to be removed
	 * @return true if points are successfully removed
	 */
	public boolean removeCtrlPoints(int first, int last){
		if(last >= first && first >= 0 && last <= degree && (degree - 1 + first - last) >= 2){
			int ndegree = degree -1 + first - last;
			float[] npx = new float[ndegree];
			float[] npy = new float[ndegree];
			float[] npz = new float[ndegree];
			pxi = new float[ndegree];
			pyi = new float[ndegree];
			pzi = new float[ndegree];
			b = new float[ndegree];
			int dst, src;
			// Copy upto but not including 'first'
			for(dst = 0; dst < first; dst++){
				npx[dst] = px[dst];
				npy[dst] = py[dst];				
				npz[dst] = pz[dst];				
			}
			// Copy those after last
			for(src = last+1; src < degree; src++){
				npx[dst] = px[src];
				npy[dst] = py[src];				
				npz[dst] = pz[src];				
				dst++;
			}
			px = npx;
			py = npy;
			pz = npz;
			degree = ndegree;
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a single control point <br>
	 * Silently fails if point is outside array bounds or if it would leave
	 * less than 2 control points.
	 *  
	 * @param point
	 * @return true if point successfully removed
	 */
	public boolean removeCtrlPoint(int point){
		return removeCtrlPoints(point, point);
	}
	
	/**
	 * Change the value stored in a point.
	 * 
	 * @param point
	 * @param pos constrained to array bounds
	 */
	public void updateCtrlPoint(PVector point, int pos){
		pos = PApplet.constrain(pos, 0, degree - 1);
		px[pos] = point.x;
		py[pos] = point.y;
		pz[pos] = point.z;
	}
	
	/**
	 * Change the value stored in a point.
	 *  
	 * @param x
	 * @param y
	 * @param pos constrained to array bounds
	 */
	public void updateCtrlPoint(float x, float y, float z, int pos){
		pos = PApplet.constrain(pos, 0, degree - 1);
		px[pos] = x;
		py[pos] = y;
		pz[pos] = z;
	}
	
	/**
	 * Get a control point 
	 * @param pos constrained to array bounds
	 */
	public PVector getCtrlPoint(int pos){
		pos = PApplet.constrain(pos, 0, degree - 1);
		return new PVector(px[pos], py[pos], pz[pos]);
	}
	
	/**
	 * Get the number of control points used to form the curve. <br>
	 * @return number of points
	 */
	public int getNbrCtrlPoints() {
		return degree;
	}

	/**
	 * Get the bezier control points
	 * @return an array of the control points
	 */
	public PVector[] getCtrlPointArray() {
		PVector[] ctrlPoint = new PVector[degree];
		for(int i = 0; i < degree; i++)
			ctrlPoint[i] = new PVector(px[i], py[i], pz[i]);
		return ctrlPoint;
	}

	/* 
	 * #############################################################
	 * The following methods relate to the actual Bexzier curve
	 * #############################################################
	 */
	
	/**
	 * Calculate the tangent vector for a point on the bezier curve
	 * 
	 * @param t
	 * @return the tangent vector (normalised)
	 */
	public PVector tangent(float t){
		PVector tangent;
		float tx, ty, tz;
		bCoefficients(t);
		tx = bezierTangent(px,t);
		ty = bezierTangent(py,t);
		tz = bezierTangent(pz,t);
		tangent = new PVector(tx,ty,tz);
		tangent.normalize();
		return tangent;
	}

	/**
	 * Calculate the normal vector for a point on the bezier curve
	 * @param t
	 * @return the normal vector (normalised)
	 */
	public PVector normal(float t){
		PVector normal;
		Rot rot;
		float tx, ty, tz;
		bCoefficients(t);
		tx = bezierTangent(px,t);
		ty = bezierTangent(py,t);
		tz = bezierTangent(pz,t);
		
		rot = new Rot(new PVector(0,0,1), new PVector(tx,ty,tz));
		normal = rot.applyToNew(new PVector(0,1,0));
		normal.normalize();
		return normal;
	}

	/**
	 * Calculate the normals along the Bezier curve. <br>
	 * 
	 * @param steps the number of normals to calculate
	 * @return array of PVector holding normals
	 */
	public PVector[] normals(int steps){
		PVector normals[] = new PVector[steps];
		float t = 0.0f;
		float dt = 1.0f/(steps -1);
		for(int i = 0; i < steps; i++){
			normals[i] = normal(t);
			t += dt;
		}
		return normals;
	}

	/**
	 * Calculate the tangents along the Bezier curve. <br>
	 * 
	 * @param steps the number of tangents to calculate
	 * @return array of PVector holding tangents
	 */
	public PVector[] tangents(int steps){
		PVector tangents[] = new PVector[steps];
		float t = 0.0f;
		float dt = 1.0f/(steps -1);
		for(int i = 0; i < steps; i++){
			tangents[i] = tangent(t);
			t += dt;
		}
		return tangents;
	}

	/**
	 * Calculate the point for a given parametric point 't' on the bezier curve. 
	 * @param t
	 * @return (x,y) position for given t value
	 */
	public PVector point(float t){
		float t1 = 1.0f - t;
		System.arraycopy(px, 0, pxi, 0, degree);
		System.arraycopy(py, 0, pyi, 0, degree);
		System.arraycopy(pz, 0, pzi, 0, degree);
		for(int j = degree-1; j > 0; j--){
			for(int i = 0; i < j; i++){
				pxi[i] = t1 * pxi[i] + t * pxi[i+1];
				pyi[i] = t1 * pyi[i] + t * pyi[i+1];
				pzi[i] = t1 * pzi[i] + t * pzi[i+1];
			}
		}
		return new PVector(pxi[0], pyi[0], pzi[0]);
	}

	/**
	 * Calculate the points along the Bezier curve. <br>
	 * 
	 * @param steps the number of points to calculate
	 * @return array of PVector holding points
	 */
	public PVector[] points(int steps){
		PVector points[] = new PVector[steps];
		float t = 0.0f;
		float dt = 1.0f/(steps -1);
		for(int i = 0; i < steps; i++){
			points[i] = point(t);
			t += dt;
		}
		return points;
	}

	/**
	 * Used internally in calculating normals and tangents
	 * @param p
	 * @param t
	 * @return
	 */
	private float bezierTangent(float[] p, float t) {
		float v = 0.0f;
		for(int i = 1; i < degree; i++){
			v += (p[i-1] - p[i])*b[i];
		}
		return v;
	}

	/**
	 * Used internally in calculating normals and tangents
	 * @param t
	 */
	private void bCoefficients(float t){
		for(int j = 0; j < degree; j++)
			b[j] = 0;
		b[1] = 1;
		for(int j = 1; j < degree-1; j++){
			for(int i = j+1; i > 0; i--){
				b[i] = (1-t)*b[i] + t*b[i-1];
			}
		}
	}

	/**
	 * Calculate the path length of a section of Bezier curve. <br>
	 * There is no formula for the exact length of a Bezier curve so any method
	 * will always be approximate. <br>
	 * This method will always 'underestimate' the actual path length. It works
	 * by subdividing the Bezier curve into a number of straight line segments
	 * and summing their lengths. <br>
	 * The number of subdivisions can be increased to the accuracy of the result 
	 * but at the cost of additional computation. <br>
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
				points[i] = point(t);
				t += deltaT;
			}

			dist  = PVector.dist(points[0], points[1]);
			for(int i = 2; i < steps; i++)
				dist += PVector.dist(points[i-1], points[i]);
		}
		return dist;
	}
	
	/**
	 * Calculate the path length of the Bezier curve. <br>
	 * There is no formula for the exact length of a Bezier curve so any method
	 * will always be approximate. <br>
	 * This method will always 'underestimate' the actual path length. It works
	 * by subdividing the Bezier curve into a number of straight line segments
	 * and summing their lengths. <br>
	 * The number of subdivisions can be increased to the accuracy of the result 
	 * but at the cost of additional computation. <br>
	 * You may want to experiment but 100+ subdivisions normally give quite good 
	 * results. <br>
	 * 
	 * @param steps number of subdivisions
	 * @return length of the curve
	 */
	public float length(int steps){
		float dist = -1;
		if(steps > 1){
			PVector[] points = points(steps - 1);
			dist  = PVector.dist(points[0], points[1]);
			for(int i = 2; i < points.length; i++)
				dist += PVector.dist(points[i-1], points[i]);
		}
		return dist;
	}
}
