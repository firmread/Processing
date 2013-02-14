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
 * Utility class to specify which part of the mesh to be drawn. <br>
 * 
 * Only applies to classes that inherit from Mesh2DCore i.e. BezierShape, Ellipsoid,
 * Helix, Tube and Toroid.  
 * 
 * @author Peter Lager
 *
 */
public class MeshSection {

	private int ewSteps, nsSteps;
	public int sEW, eEW, sNS, eNS;
	
	@SuppressWarnings("unused")
	private MeshSection(){}
	
	/**
	 * Create a mesh section based on number of steps.
	 * @param ewSteps
	 * @param nsSteps
	 */
	public MeshSection(int ewSteps, int nsSteps) {
		this.ewSteps = ewSteps;
		this.nsSteps = nsSteps;
		sEW = sNS = 0;
		eEW = ewSteps;
		eNS = nsSteps;
	}
	
	/**
	 * All parameters will be capped to the range >=0 and <= 1 and
	 * where necessary swapped so start < end position
	 * 
	 * @param ewStart
	 * @param ewEnd
	 * @param nsStart
	 * @param nsEnd
	 */
	public void setRange(float ewStart, float ewEnd, float nsStart, float nsEnd){
		float temp = 0;
		
		ewStart = PApplet.constrain(ewStart , 0.0f, 0.999f);
		ewEnd = PApplet.constrain(ewEnd, 0.0f, 0.999f);
		nsStart = PApplet.constrain(nsStart, 0.0f, 0.999f);
		nsEnd = PApplet.constrain(nsEnd, 0.0f, 0.999f);

		if(ewStart > ewEnd){
			temp = ewStart; ewStart = ewEnd; ewEnd = temp;
		}			
		if(nsStart > nsEnd){
			temp = nsStart; nsStart = nsEnd; nsEnd = temp;
		}

		sEW = (int)(ewStart * (ewSteps));
		eEW = ewSteps - (int)((1-ewEnd) * (ewSteps));
		sNS = (int)(nsStart * (nsSteps));
		eNS = nsSteps - (int)((1-nsEnd) * (nsSteps));
	}

	/**
	 * All parameters will be capped to the range >=0 and < ewPieces / nsPieces and
	 * where necessary swapped so start < end position
	 * 
	 * @param ewStartPiece
	 * @param ewEndPiece
	 * @param nsStartPiece
	 * @param nsEndPiece
	 */
	public void setRange(int ewStartPiece, int ewEndPiece, int nsStartPiece, int nsEndPiece){
		int temp = 0;
		
		System.out.println("(a) "+ewStartPiece+" "+ewEndPiece+"    "+nsStartPiece+" "+nsEndPiece);	
		ewStartPiece = PApplet.constrain(ewStartPiece , 0, ewSteps - 2);
		ewEndPiece = PApplet.constrain(ewEndPiece, 0, ewSteps - 2);
		nsStartPiece = PApplet.constrain(nsStartPiece, 0, nsSteps - 2);
		nsEndPiece = PApplet.constrain(nsEndPiece, 0, nsSteps - 2);

		System.out.println("(b) "+ewStartPiece+" "+ewEndPiece+"    "+nsStartPiece+" "+nsEndPiece);	

		if(ewStartPiece > ewEndPiece){
			temp = ewStartPiece; ewStartPiece = ewEndPiece; ewEndPiece = temp;
		}
		if(nsStartPiece > nsEndPiece){
			temp = nsStartPiece; nsStartPiece = nsEndPiece; nsEndPiece = temp;
		}

		System.out.println("(c) "+ewStartPiece+" "+ewEndPiece+"    "+nsStartPiece+" "+nsEndPiece);	

		sEW = ewStartPiece;
		eEW = ewEndPiece + 2;
		sNS = nsStartPiece;
		eNS = nsEndPiece + 2;
//		System.out.println("(d) "+this);	
	}
	
	public String toString(){
		return "Section2D ["+sEW+" "+eEW+"]  ["+sNS+" "+eNS+"]";
	}
}
