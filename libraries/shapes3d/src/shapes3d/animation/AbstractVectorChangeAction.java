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
import shapes3d.Shape3D;

/**
 * Abstract class that can be used to interpolate between 2 PVector
 * objects over a given time. It is also possible to specify a delay
 * time before the interpolation starts. <br>
 * 
 * Inherit from the class to provide useful classes such as ShapeMover
 * and shapeRotator. <br>
 *  
 * @author Peter Lager
 *
 */
public abstract class AbstractVectorChangeAction {

	protected final PApplet app;
	protected final Shape3D shape;

	protected StopWatch timer;
	protected float runTime = 0;
	protected float delay;
	protected final float duration;

	protected final PVector start = new PVector();
	protected final PVector end = new PVector(); 
	protected final PVector diff;

	protected boolean running;

	/**
	 * Create and initialise the auto rotator. <br>
	 * 
	 * @param theApp
	 * @param shape
	 * @param start the start rotation angles
	 * @param end the desired rotation angles
	 * @param duration time allowed to rotate the shape
	 */
	public AbstractVectorChangeAction(PApplet theApp, Shape3D shape, PVector start, PVector end, float duration, float delay) {
		app = theApp;
		this.start.set(start);
		this.end.set(end);
		diff = PVector.sub(this.end, this.start);
		this.duration = duration;
		this.delay = delay;
		runTime = 0;
		this.shape = shape;
		timer = new StopWatch();
		app.registerPre(this);
	}

	protected void start(){

	}
	/**
	 * Update the vector.
	 */
	public PVector update(){
		PVector newValue = null;
		timer.update();
		float deltaTime = timer.lapTime();
		if(delay > 0){
			delay -= deltaTime;
			System.out.println("Delay " + delay);
		}
		else if(delay < 0){
			runTime += delay;
			delay = 0;
			timer.reset();
			running = true;
		}
		else {
			runTime += deltaTime;
			if(runTime < duration){
				newValue = PVector.mult(diff, runTime / duration);
				newValue.add(start);
				running = false;
			}
			else {
				newValue = new PVector(end.x, end.y, end.z);
				app.unregisterPre(this);
			}
		}
		return newValue;
	}

	/**
	 * Stop this action
	 */
	public void kill(){
		running = false;
		app.unregisterPre(this);
	}

	/**
	 * Are we still running or have we reached the desired target position.
	 * @return true if not finished
	 */
	public boolean isRunning(){
		return running;
	}

}
