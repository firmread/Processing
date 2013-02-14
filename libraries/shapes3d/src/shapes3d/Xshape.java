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

/**
 * This is a simple way for users to add their own shapes so they work
 * as part of the Shapes3D library. <br>
 * To use this class create your shape class which must implement the IShape 
 * interface to work with this library). Then you create an object of your 
 * class and use setXShape to add it to an Xshape object. <br>
 * 
 * 
 * @author Peter Lager
 *
 */
public class Xshape extends Shape3D {

	IShape xshape = null;


	/**
	 * @return the xshape
	 */
	public IShape getXshape() {
		return xshape;
	}


	/**
	 * @param xshape the xshape to set
	 */
	public void setXshape(IShape xshape) {
		this.xshape = xshape;
	}

	@Override
	protected void calcShape() { }

	@Override
	public void draw() {
		app.pushStyle();
		app.pushMatrix();
		if(pickModeOn){
			if(xshape != null){
				pickBuffer.pushMatrix();
				pickBuffer.translate(pos.x, pos.y, pos.z);
				pickBuffer.rotateX(rot.x);
				pickBuffer.rotateY(rot.y);
				pickBuffer.rotateZ(rot.z);
				pickBuffer.scale(shapeScale);
				pickBuffer.noStroke();
				pickBuffer.fill(pickColor);
				if(visible && drawMode != WIRE){
					xshape.drawForPicker(pickBuffer);
				}
				if(children != null){
					Iterator<Shape3D> iter = children.iterator();
					while(iter.hasNext())
						iter.next().drawForPicker(pickBuffer);
				}
				pickBuffer.popMatrix();
			}
		}
		else {
			if(xshape != null && visible){
				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);

				switch(drawMode){
				case TEXTURE:
					app.fill(fillColor);
					app.noStroke();
					xshape.drawWithTexture(app, skin);
					break;
				case WIRE:
					app.noFill();
					app.stroke(strokeColor);
					app.strokeWeight(strokeWeight);
					xshape.drawWireFrame(app);
					break;
				case SOLID:
				default:
					app.noStroke();
					app.fill(fillColor);
					app.textureMode(NORMAL);
					xshape.drawWithColor(app);
				}
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
}


