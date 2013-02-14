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

/**
 * As the name suggests this class acts as an Anchpr for other shapes.
 * Objects of this clsss have no visible form. Create other shapes and
 * add to an Anchor object to group shapes together to get a common 
 * centre of rotation, scaling and translation.
 * 
 * @author Peter Lager
 *
 */
public class Anchor extends Shape3D {

	
	public Anchor(PApplet app){
		super();
		this.app = app;
	}
	
	public Anchor(PApplet app, PVector pos){
		super();
		this.app = app;
	}
	
	/**
	 * No shape to calculate so empty method
	 */
	@Override
	protected void calcShape() {
	}

	/**
	 * Will cause added shapes to be drawn
	 */
	@Override
	public void draw() {
		app.pushStyle();
		app.pushMatrix();
		if(pickModeOn){
			pickBuffer.pushMatrix();
			pickBuffer.translate(pos.x, pos.y, pos.z);
			pickBuffer.rotateX(rot.x);
			pickBuffer.rotateY(rot.y);
			pickBuffer.rotateZ(rot.z);
			pickBuffer.scale(shapeScale);

			if(children != null){
				Iterator<Shape3D> iter = children.iterator();
				while(iter.hasNext())
					iter.next().drawForPicker(pickBuffer);
			}
			pickBuffer.popMatrix();
		}
		else {
			app.translate(pos.x, pos.y, pos.z);
			app.rotateX(rot.x);
			app.rotateY(rot.y);
			app.rotateZ(rot.z);
			app.scale(shapeScale);

			if(children != null){
				Iterator<Shape3D> iter = children.iterator();
				while(iter.hasNext())
					iter.next().draw();
			}
		}
		app.popMatrix();
		app.popStyle();
	}


}
