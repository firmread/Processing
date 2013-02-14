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

package shapes3d.utils;

import processing.core.PApplet;

/**
 * This class is used to determine the radius of a tube shape
 * along its length as determined by the parametric variable t. <br>
 * 
 * This class is used for both flat (constant radius) and for tapered/
 * shaped tubes. <br>
 * 
 * You can create your own class to generate the tube radius but it must
 * implement the I_RadiusGen interface. Read comments in that file for
 * more detail.
 * 
 * @author Peter Lager
 *
 */
public class TubeRadius implements I_RadiusGen {

	private float[] radiusProfile;
	private float[] deltaRad;
	private float deltaT;

	@SuppressWarnings("unused")
	private TubeRadius(){ }

	/**
	 * Create a constant radius profiler.
	 * @param radius
	 */
	public TubeRadius(float radius){
		if(radius <= 0)
			radius = 1;		
		radiusProfile = new float[] {radius};
	}

	/**
	 * Create a profiler based on an array
	 * @param profile
	 */
	public TubeRadius(float[] profile){
		radiusProfile = profile;
		if(radiusProfile.length > 1){
			deltaT = 1.0f / (radiusProfile.length - 1);
			deltaRad = new float[radiusProfile.length - 1];
			for(int i = 0; i < deltaRad.length ; i++)
				deltaRad[i] = radiusProfile[i+1] - radiusProfile[i];
		}
	}

	/**
	 * Calculate the radius for a position on the Bezier path defined
	 * by the parametric variable t
	 */
	public float radius(float t) {
		t = PApplet.constrain(t, 0, 0.999999f);
		float radius;
		if(radiusProfile.length == 1)
			radius = radiusProfile[0];
		else {
			float p = t / deltaT;
			int index = (int)p;
			float dt = t - index * deltaT;
			radius = radiusProfile[index] + deltaRad[index] * (dt / deltaT);
		}
		return radius;
	}

	/**
	 * Return an object that defines the profile.
	 */
	public Object getController() {
		float[] nrad = new float[radiusProfile.length];
		System.arraycopy(radiusProfile, 0, nrad, 0, radiusProfile.length);
		return nrad;
	}

	/**
	 * Can only be certain of a constant radius if profile array has
	 * a single element.
	 */
	public boolean hasConstantRadius() {
		return (radiusProfile.length == 1);
	}

}
