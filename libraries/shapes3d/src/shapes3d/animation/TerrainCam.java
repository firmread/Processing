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
import shapes3d.Terrain;

/**
 * This class provides a simple camera that can be used with the Terrain 
 * class. The movement is in the XZ plane and rotation is about the Y axis. <br>
 * The nearZ value is much smaller than the default value in Processing to
 * make sure the terrain is drawn right up to the camera position. <br>
 * 
 * @author Peter Lager
 *
 */
public class TerrainCam extends VCam {

	public TerrainCam(PApplet app) {
		super(app);
		nearZ = cameraZ / 1000000.0f;
		perspective();
		PApplet.println("TerrainCam ctor");

	}

	/**
	 * Will adjust the camera's position to ensure that it is over the terrain
	 * at a given height above it.
	 * @param t the terrain we want to use
	 * @param tMode Terrain.CLAMP or Terrain.WRAP
	 * @param heightAbove the height of the eye above the terrain
	 */
	public void adjustToTerrain(Terrain t, int tMode, float heightAbove){
		t.adjustPosition(eye, tMode);
		eye.y = t.getHeight(eye.x, eye.z) - heightAbove;
	}
	
	/**
	 * Move the camera based on direction of movement (forward vector), speed
	 * and time (in seconds)
	 * @param time
	 */
	public void move(float time){
		PVector move = PVector.mult(forward, speed);
		move.mult(time);
		eye.x += move.x;
		eye.z += move.z;
	}
		

	/**
	 * Rotate about the Y axis by a given angle in radians.
	 * @param angle
	 */
	public void rotateViewBy(float angle){
		float sinA = (float) Math.sin(angle);
		float cosA = (float) Math.cos(angle);

//		float nx = (lookAt.x - eye.x) * cosA - (lookAt.z - eye.z) * sinA;
//		float nz = (lookAt.x - eye.x) * sinA + (lookAt.z - eye.z) * cosA;
//		lookAt.x = eye.x + nx;
//		lookAt.z = eye.z + nz;
		
		float nx = lookDir.x * cosA - lookDir.z * sinA;
		float nz = lookDir.x * sinA + lookDir.z * cosA;
		lookDir.x = nx;
		lookDir.z = nz;
		lookDir.normalize();
	}
	
	/**
	 * Change direction of movement about Y axis BY angle in radians
	 * @param angle
	 */
	public void turnBy(float angle){
		float sinA = (float) Math.sin(angle);
		float cosA = (float) Math.cos(angle);
		float nx = forward.x * cosA - forward.z * sinA;
		float nz = forward.x * sinA + forward.z * cosA;
		forward.x = nx;
		forward.y = 0;
		forward.z = nz;
		forward.normalize();
	}
	
	/**
	 * Rotate about the Y axis TO a given angle.
	 * @param angle
	 */
	public void rotateViewTo(float angle){
		float sinA = (float) Math.sin(angle);
		float cosA = (float) Math.cos(angle);

//		PVector lookAt2D = PVector.sub(eye, lookAt);
//		lookAt2D.y = 0;
//		
//		float viewDist = lookAt2D.mag();
//		
//		float nx = viewDist * cosA;
//		float nz = viewDist * sinA;
//		lookAt.x = eye.x + nx;
//		lookAt.z = eye.z + nz;
		
		lookDir.set(1, 0, 0);
		float nx = lookDir.x * cosA - lookDir.z * sinA;
		float nz = lookDir.x * sinA + lookDir.z * cosA;
		lookDir.x = nx;
		lookDir.z = nz;
		lookDir.normalize();
	}
	
	/**
	 * Change direction of movement about Y axis TO angle in radians
	 * @param angle
	 */	
	public void turnTo(float angle){
		float sinA = (float) Math.sin(angle);
		float cosA = (float) Math.cos(angle);
		forward.set(1, 0, 0);
		float nx = forward.x * cosA - forward.z * sinA;
		float nz = forward.x * sinA + forward.z * cosA;
		forward.x = nx;
		forward.y = 0;
		forward.z = nz;
		forward.normalize();
	}

}
