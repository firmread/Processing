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

package shapes3d.utils;

/**
 * Simple class to hold texture coordinates. <br>
 * Only used by Cone class and Mesh2DCoreWithCaps.EndCap classes
 * to hold 'circular' coordinates on a rectangular image.  
 * 
 * @author Peter Lager
 *
 */
public class UV {

	public float u;
	public float v;
	
	/**
	 * Default ctor sets u and v to zero
	 */
	public UV() {
		u = 0.0f;
		v = 0.0f;
	}

	/**
	 * @param u
	 * @param v
	 */
	public UV(float u, float v) {
		this.u = u;
		this.v = v;
	}
	
	/**
	 * Creates a string representing UV coordinates stored in the object.
	 */
	public String toString(){
		return"[UV " + u + ", "+v +"]";
	}

}
