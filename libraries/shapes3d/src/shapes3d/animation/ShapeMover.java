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
 * Class to cause a shape to move from one position to another over a fixed
 * time period and after an optional delay start time.<Bbr>
 * 
 * @author Peter Lager
 *
 */
public class ShapeMover extends AbstractVectorChangeAction {

	/**
	 * Create and initialise the auto mover. <br>
	 * 
	 * @param theApp
	 * @param shape
	 * @param start the start position
	 * @param end the desired position
	 * @param duration time allowed to move the shape
	 */
	public ShapeMover(PApplet theApp, Shape3D shape, PVector start, PVector end, float duration, float delay){
		super(theApp, shape, start, end, duration, delay);
	}

	/**
	 * Update the position once per frame.
	 */
	public void pre(){
		PVector newValue = update();
		if(newValue != null)
			shape.moveTo(newValue);
	}

}
