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

package shapes3d.animation;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class provides a very simple 'portable video camera' that 
 * can me moved at a processor independent speed. <br>
 * 
 * Future releases are likely to see additional features being
 * added to this class.
 * 
 * @author Peter Lager
 *
 */
public class VCam extends Camera {

	protected PVector lookDir = new PVector(0,0,1);
	public PVector forward = new PVector(0,0,1);
	public float speed;

	
	/**
	 * Creates a perspective camera. The default values are: <br>
	 * perspective(fov, aspectRatio, nearZ, farZ) <br>
	 * fov = PI/3 <br>
	 * aspectRatio = width/height <br>
	 * nearZ = cameraZ / 1000000.0f <br>
	 * farZ = cameraZ*10.0f <br>
	 * where cameraZ is ((height/2.0) / tan(fov/2.0)) <br>
	 * The nearZ value is much smaller than the default value in Processing to
	 * make sure the terrain is drawn right up to the camera position. <br>
	 * @param app
	 */
	public VCam(PApplet app) {
		super(app);
	}

	/**
	 * Set the camera position ready for drawing. Equivalent to camera() function
	 * in Processing
	 */
	@Override
	public void camera(){
		app.camera(eye.x, eye.y, eye.z, 
				eye.x + lookDir.x, eye.y + lookDir.y, eye.z + lookDir.z,  
				up.x, up.y, up.z);
	}
	
	/**
	 * This should be used to update the cameras position etc.
	 * based on the time elapsed since last called.
	 */
	@Override
	public void update(float time) {
		PVector move = PVector.mult(forward, speed);
		move.mult(time);
		eye.add(move);		
	}
	
	public void lookDir(PVector ld){
		lookDir.set(ld);
	}
	
	public PVector lookDir(){
		return new PVector(lookDir.x, lookDir.y, lookDir.z);
	}
	
	/**
	 * Move the camera based on direction of movement (forward vector), speed
	 * and time (in seconds)
	 * @param time
	 */
	
	public void move(float time){
		update(time);
	}
		
	/**
	 * Sets the current speed for the camera.
	 * @param speed
	 */
	public void speed(float speed){
		this.speed = speed;
	}
	
	public float speed(){
		return speed;
	}


	
}
