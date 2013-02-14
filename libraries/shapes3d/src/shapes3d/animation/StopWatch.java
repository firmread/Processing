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

/**
 * Simple implementation of a timer that records the current and the lap time 
 * (in seconds). <br><br>
 * Use <pre>Clock.currTime</pre> to get the time since the Clock was created or
 * last reset. It is updated on every call to the setLapTime() method.<br>
 * Use <pre>Clock.lapTime</pre> to get the time since the last call to the 
 * setLapTime() method. <br>,br>
 * 
 * The current and lap times can be automatically updated for you every frame, or
 * if you want more control over when they are updated the you have the option to
 * call the setLapTime() method manually. The version use get depends on which of
 * the two create() methods you use.
 * 
 * @author Peter Lager
 *
 */
public class StopWatch {

	private long clockStartTime;
	private long last;

	private float currTime;
	private float lapTime;
	
	/**
	 * Use this constructor to create the timer. <br>
	 * The timer must be updated every frame. 
	 */
	public StopWatch(){
		clockStartTime = System.currentTimeMillis();
		last = clockStartTime + 100000000;		
	}
	
	/**
	 * You must use this constructor
	 * Using this constructor means that the gameTime and elapsedTime are 
	 * automatically updated for you just before the draw method is called.
	 * So there is no need for you to call the setLapTime() method.
	 * 
	 * @param app the PApplet object
	 */
	public StopWatch(PApplet app){
		clockStartTime = System.currentTimeMillis();
		last = clockStartTime + 100000000;
	}
	
	/**
	 * Update gameTime and elapsed time. <br>
	 * This must be called every frame. So if you have a timer object
	 * called <pre>t</pre> then at the at the start of the draw() method
	 * update the timer with <br>
	 * <pre>t.update();</pre> 
	 */	
	public void update(){
		long ct = System.currentTimeMillis();
		currTime = (ct - clockStartTime)/1000.0f;
		lapTime = (ct - last)/1000.0f;
		lapTime = (lapTime < 0) ? 0.0f : lapTime;
		last = ct;		
	}
	/**
	 * Get the time between last 2 updates
	 * @return time in seconds
	 */
	public float lapTime(){
		return lapTime;
	}
	
	/**
	 * Get the time since the timer was created/reset
	 * @return time in seconds
	 */
	public float currTime(){
		return currTime;
	}
	
	/**
	 * Reset the clock. <br>
	 */
	public void reset(){
		clockStartTime = System.currentTimeMillis();
		last = clockStartTime + 100000000;
	}
	
}
