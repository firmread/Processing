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
 * Base class for all camera types provided by Shapes3D
 * 
 * @author Peter Lager
 *
 */
public abstract class Camera {

	public static final int WRAP = 0;
	public static final int LIMIT = 1;

	protected PApplet app;
	
	protected PVector eye = new PVector(0,0,0);
	protected PVector up = new PVector(0,1,0);
	
	protected final float cameraZ;
	protected float fov;
	protected float aspectRatio;
	protected float nearZ;
	protected float farZ;

	
	protected Camera(){
		cameraZ = 0;
	}

	
	/**
	 * Creates a perspective camera. The default values are: <br>
	 * perspective(fov, aspectRatio, nearZ, farZ) <br>
	 * fov = PI/3 <br>
	 * aspectRatio = width/height <br>
	 * nearZ = cameraZ / 10.0f <br>
	 * farZ = cameraZ*10.0f <br>
	 * where cameraZ is ((height/2.0) / tan(fov/2.0)) <br>
	 * @param app
	 */
	public Camera(PApplet app) {
		this.app = app;
		fov = (float) (Math.PI/3);
		cameraZ = (app.height/2.0f) / (float)Math.tan(fov/2.0f);
		nearZ = cameraZ / 10.0f;
		farZ = cameraZ * 10.0f;
		aspectRatio = ((float)app.width)/((float)app.height);
	}
	
	public abstract void camera();

	public void perspective(){
		app.perspective(fov, aspectRatio, nearZ, farZ);
	}

	public abstract void update(float time);
	
	/**
	 * Get a copy of the camera's position
	 * @return the camera's location
	 */
	public PVector eye(){
		return new PVector(eye.x, eye.y, eye.z);
	}
	
	/**
	 * Set the camera's position.
	 * @param eye
	 */
	public void eye(PVector eye){
		this.eye.x = eye.x;
		this.eye.y = eye.y;
		this.eye.z = eye.z;
	}
	
	/**
	 * Change the z-position of the nearest clipping plane
	 * @param nearZ
	 */
	public void nearZ(float nearZ){
		this.nearZ = nearZ;
		app.perspective(fov, aspectRatio, nearZ, farZ);		
	}
	
	/**
	 * Get the z-position of the nearest clipping plane
	 * @return the nearZ clipping value
	 */
	public float nearZ(){
		return nearZ;
	}
	
	/**
	 * Change the z-position of the farthest clipping plane
	 * @param farZ
	 */
	public void farZ(float farZ){
		this.farZ = farZ;
		app.perspective(fov, aspectRatio, nearZ, farZ);				
	}
	
	/**
	 * Get the z-position of the farthest clipping plane
	 * @return the farZ clipping value
	 */
	public float farZ(){
		return farZ;
	}
	
	/**
	 * @param fov the fov to set
	 */
	public void fov(float fov) {
		this.fov = fov;
		app.perspective(fov, aspectRatio, nearZ, farZ);				
	}
	
	/**
	 * Get the camera's field of view.
	 * @return the camera's field of view angle in radians.
	 */
	public float fov() {
		return fov;
	}
	
	/**
	 * Set the perspective aspect ratio.
	 * @param aspectRatio
	 */
	public void aspectRatio(float aspectRatio){
		this.aspectRatio = aspectRatio;
		app.perspective(fov, aspectRatio, nearZ, farZ);				
	}
	
	/**
	 * Get the perspective aspect ratio currently being used.
	 * @return the camera's aspect ratio
	 */
	public float aspectRatio(){
		return aspectRatio;
	}

	
}
