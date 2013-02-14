/*
  Part of the Shapes 3D library for Processing 
  	http://www.lagers.org.uk

  Copyright (c) 20010 Peter Lager

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

import processing.core.PApplet;
import processing.core.PGraphics3D;
import processing.core.PImage;

/**
 * You can create a class to represent your own shape. To work with the library the
 * class must implement this interface. It defines all the necessary draw methods to
 * be implemented by your class. Objects created from your class are are used in
 * conjunction with the XShape class to work seamlessly with this library. <br>
 * Although an implementation is required for each method, use an empty method for 
 * unwanted drawing modes. <br>
 * 
 * 
 * @author Peter Lager
 *
 */
public interface IShape {

	/**
	 * Draw the shape for the 3D picker. There is no need to set the pick color since 
	 * it is done in XShape before we get here. <br>
	 * @param pickBuffer
	 */
	public void drawForPicker(PGraphics3D pickBuffer);
	
	/**
	 * Draw the shape with the given texture. The textureMode has been set to NORMAL
	 * if you are using pixel coordinates then set to IMAGE. <br>
	 * 
	 * @param app
	 * @param skin
	 */
	public void drawWithTexture(PApplet app, PImage skin);

	/**
	 * Draw a wire frame image. Most of the shapes use quads rather then 
	 * triangles for better visual effect. <br>
	 * @param app
	 */
	void drawWireFrame(PApplet app);
	
	/**
	 * Draw with the colour already set. <br>
	 * @param app
	 */
	public void drawWithColor(PApplet app);
	
}
