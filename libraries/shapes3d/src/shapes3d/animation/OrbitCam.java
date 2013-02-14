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

package shapes3d.animation;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * A very basic camera that can orbit a particular position in
 * 3D space as specified by the lookAt vector.
 * 
 * @author Peter Lager
 *
 */
public class OrbitCam extends Camera{
	
	private float rotLat, rotLong;
	private float minRotLat, minRotLong;
	private float maxRotLat, maxRotLong;
	private int wrapLat, wrapLong;

	protected PVector lookAt = new PVector(0,0,0);

	private float minD;
	private float maxD;
	private float dist;



	public OrbitCam(PApplet app) {
		super(app);
		initCam(new PVector(), 100);
	}

	public OrbitCam(PApplet app, float dist){
		super(app);
		initCam(new PVector(), dist);
	}

	public OrbitCam(PApplet app, PVector lookAt, float dist){
		super(app);
		initCam(lookAt, dist);
	}

	protected void initCam(PVector lookAt, float distance){
		this.lookAt.set(lookAt);
		dist = distance;

		rotLat = 0;
		rotLong = 0;
		
		minRotLat = 0;
		maxRotLat = PApplet.PI;
		
		minRotLong = 0;
		maxRotLong = PApplet.TWO_PI;
		wrapLat = LIMIT;
		wrapLong = WRAP;
		
		minD = Float.MIN_VALUE;
		maxD = Float.MAX_VALUE;

		calcEye();
	}
	
	public void setDistance(float distance){
		dist = PApplet.constrain(distance, minD, maxD);
		calcEye();
	}

	public void changeDistance(float delta){
		dist = PApplet.constrain(dist + delta, minD, maxD);
		calcEye();
	}
	
	protected void calcEye(){
		float sinUD = (float) Math.sin(rotLat); 
		float cosUD = (float) Math.cos(rotLat); 
		float sinLR = (float) Math.sin(rotLong); 
		float cosLR = (float) Math.cos(rotLong); 

		eye.x = lookAt.x + dist * sinUD * cosLR;
		eye.y = -(lookAt.y + dist * cosUD);
		eye.z = lookAt.z + dist * sinUD * sinLR;
	}

	/**
	 * Set the camera position ready for drawing. Equivalent to camera() function
	 * in Processing
	 */
	@Override
	public void camera(){
		app.beginCamera();
		app.camera(eye.x, eye.y, eye.z,
				lookAt.x, lookAt.y, lookAt.z,  
				0,1,0);
		app.endCamera();
	}

	@Override
	public void update(float time) {
	}
	

	public void setRangeLatAngle(float minA, float maxA, int mode){
		wrapLat = mode;
		minRotLat = Math.min(minA, maxA);
		maxRotLat = Math.max(minA, maxA);
		setAngleLat(rotLat);
	}

	public void setRangeLongAngle(float minA, float maxA, int mode){
		wrapLong = mode;
		minRotLong = Math.min(minA, maxA);
		maxRotLong = Math.max(minA, maxA);
		setAngleLong(rotLong);
	}

	public void changeAngles(float deltaLat, float deltaLong){
		rotLat = fixAngle(rotLat + deltaLat, minRotLat, maxRotLat, wrapLat);
		rotLong = fixAngle(rotLong + deltaLong, minRotLong, maxRotLong, wrapLong);
		calcEye();
	}

	public void setAngles(float latA, float longA){
		rotLat = fixAngle(latA, minRotLat, maxRotLat, wrapLat);
		rotLong = fixAngle(longA, minRotLong, maxRotLong, wrapLong);
		calcEye();
	}

	public void setAngleLat(float latA){
		rotLat = fixAngle(latA, minRotLat, maxRotLat, wrapLat);
		calcEye();
	}

	public void setAngleLong(float longA){
		rotLong = fixAngle(longA, minRotLong, maxRotLong, wrapLong);
		calcEye();
	}

	protected float fixAngle(float a, float min, float max, int wrap){
		switch(wrap){
		case WRAP:
			if(a < min)
				a =  max - (a - min);
			else if(a > max)
				a = min + (a - max);
			break;
		case LIMIT:
		default:
			a = PApplet.constrain(a, min, max);	
		}
		return a;		
	}

	public void setMinDist(float minDist) {
		if(minDist < maxD){
			minD = minDist;
			setDistance(dist);
		}
	}

	public void setMaxDist(float maxDist) {
		if(maxDist > minD){
			maxD = maxDist;
			setDistance(dist);
		}
	}

	public void setRangeDist(float minDist, float maxDist){
		minD = Math.min(minDist, maxDist);
		maxD = Math.max(minDist, maxDist);
		if(minD == maxD)
			maxD += 0.0000001f;
		setDistance(dist);
	}

	/**
	 * Get the 3D coordinates of what the camera is looking at.
	 * @return the 3D position the camera is looking at
	 */
	public PVector lookAt(){
		return new PVector(lookAt.x, lookAt.y, lookAt.z);
	}
	
	/**
	 * Specify the position that camera is looking at.
	 * @param lookAt PVector with look at coordinates.
	 */
	public void lookAt(PVector lookAt){
		this.lookAt.x = lookAt.x;
		this.lookAt.y = lookAt.y;
		this.lookAt.z = lookAt.z;
	}
	

}
