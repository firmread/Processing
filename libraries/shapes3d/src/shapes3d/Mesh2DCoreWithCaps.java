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

import processing.core.PApplet;
import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PVector;
import shapes3d.utils.Textures;
import shapes3d.utils.UV;

/**
 * This class extends the Mesh2DCore class to enable the use of end caps
 * for the Tube and Helix classes. <br>
 * 
 * It also provides methods to control the draw mode and visibility of 
 * the end caps also this class includes methods to set the texture, fill 
 * color, stroke color and stroke weight for the end caps. <br>
 * 
 * All these parameters can be set independently for each end cap based on
 * the second parameter in the setter methods. <br>
 * 
 * The second parameter is <br>
 * Tube.S_CAP, Tube.E_CAP or Tube.BOTHCAP <br>
 * these <br>
 * Helix.S_CAP, Helix.E_CAP or Helix.BOTHCAP <br>
 * are also valid
 * 
 * @author Peter Lager
 *
 */
public class Mesh2DCoreWithCaps extends Mesh2DCore {

	protected EndCap startEC, endEC;

	@Override
	protected void calcShape() {}

	/**
	 * Change the visibility of end caps.
	 * Note if the body to invisible then the end caps will not be
	 * displayed.
	 * 
	 * @param vis visibility
	 * @param caps which cap(s) affected
	 */
	public void visible(boolean vis, int caps){
		if(isFlagSet(caps, S_CAP))
			startEC.visible = vis;
		if(isFlagSet(caps, E_CAP))
			endEC.visible = vis;
	}

	/**
	 * Sets the draw mode for the end caps shape. <br>
	 * The three constants Shape3D.SOILD, Shape3D.WIRE and Shape3D.TEXTURE are used 
	 * to determine how the shape is drawn. These can be ored together to get different
	 * drawing combinations e.g. Shape3D.SOLID | Shape3D.WIRE would be a coloured shape
	 * with edges. If the parameter is Shape3D.TEXTURE then and a texture has not
	 * been set then it will use SOLID.
	 * @param mode end-cap draw mode
	 * @param caps caps which cap(s) affected
	 */
	public void drawMode(int mode, int caps){
		if(isFlagSet(caps, S_CAP))
			startEC.drawMode(mode);
		if(isFlagSet(caps, E_CAP))
			endEC.drawMode(mode);
	}
	
	/**
	 * Sets the colours to be used for the end caps.
	 * 
	 * @param col tube(s) cap colour
	 * @param caps which cap(s) affected
	 */
	public void fill(int col, int caps){
		if(isFlagSet(caps, S_CAP))
			startEC.col = col;
		if(isFlagSet(caps, E_CAP))
			endEC.col = col;
	}
	
	/**
	 * Sets the stroke weights to be used for the end caps
	 * 
	 * @param weight start end-cap stroke weight
	 * @param caps end-cap(s) affected
	 */
	public void strokeWeight(float weight, int caps){
		if(isFlagSet(caps, S_CAP))
			startEC.sweight = weight;
		if(isFlagSet(caps, E_CAP))
			endEC.sweight = weight;
	}

	/**
	 * Sets the wire color to be used for the end caps.
	 * 
	 * @param col tube(s) cap wire colour
	 * @param caps which cap(s) affected
	 */
	public void stroke(int col, int caps){
		if(isFlagSet(caps, S_CAP))
			startEC.scol = col;
		if(isFlagSet(caps, E_CAP))
			endEC.scol = col;
	}

	/**
	 * Sets the texture to be used for the end cap(s)
	 * @param fname image filename
	 * @param caps which cap(s) affected
	 */
	public void setTexture(String fname, int caps){
		PImage img = Textures.loadImage(app, fname);
		setTexture(img, caps);
	}

	/**
	 * Sets the texture to be used for the end cap(s)
	 * @param img the image to be used
	 * @param caps which cap(s) affected
	 */
	public void setTexture(PImage img, int caps){
		if(img != null){
			if(isFlagSet(caps, S_CAP))
				startEC.capSkin = img;
			if(isFlagSet(caps, E_CAP))
				endEC.capSkin = img;
		}
	}
	
	
	
	/**
	 * This class is used internally by the Tube and Helix classes
	 * to create end caps.
	 * 
	 * @author Peter Lager
	 *
	 */
	public class EndCap {

		public boolean visible = true;
		private PImage capSkin;
		private int col = WHITE;
		private int scol = WHITE;
		private float sweight = 1;

		private PVector n = new PVector();
		private PVector centre;
		private PVector[] edge;
		private UV[] uvLoc;

		public int capDrawMode = SOLID;
		
		public EndCap(){ }

		protected void calcShape(PVector[] edgeCoords, int steps, int uvDir){
			edge = edgeCoords;
			// Calculate centre of end cap
			centre = new PVector();
			for(int i = 1; i < edge.length; i++)
				centre.add(edge[i]);
			centre.div(edge.length - 1);
			// Calculate UV coordinates
			float angle = 0, dAng = uvDir * TWO_PI / (steps - 1.0f);
			uvLoc = new UV[steps + 1];
			uvLoc[0] = new UV(0.5f, 0.5f);
			for(int i = 1; i < steps + 1; i++){
				uvLoc[i] = new UV(0.5f + 0.5f * (float)Math.cos(angle), 0.5f + 0.5f * (float)Math.sin(angle));
				angle += dAng;
			}
			// Calculate normal
			PVector v1 = PVector.sub(edge[0], centre);
			PVector v2 = PVector.sub(edge[1], centre);		
			PVector.cross(v1, v2, n);
			if(uvDir != 1)
				n.mult(uvDir);
			n.normalize();
		}
		
		protected void drawWhat(){
			useSolid = ((capDrawMode & SOLID) == SOLID);
			useWire = ((capDrawMode & WIRE) == WIRE);
			useTexture = ((capDrawMode & TEXTURE) == TEXTURE && capSkin != null);
		}

		public void drawMode(int mode){
			mode &= DRAWALL;
			boolean validMode = ((mode & WIRE) == WIRE);
			validMode |= ((mode & SOLID) == SOLID);
			validMode |= ((mode & TEXTURE) == TEXTURE && capSkin != null);
			if(!validMode)
				mode = SOLID;
			capDrawMode = mode;
		}

		public void draw(){
			drawWhat();
			if(useTexture)
				drawWithTexture();
			else
				drawWithoutTexture();
			// drawNormals();
		}

		public void drawNormals(){
			PVector c = centre;
			PVector n = PVector.mult(this.n, 20);
			app.stroke(255,0,0);
			app.line(c.x,c.y,c.z,c.x+n.x,c.y+n.y,c.z+n.z);
			for(int t = 0; t < edge.length; t++) {
				c = edge[t];
				app.line(c.x,c.y,c.z,c.x+n.x,c.y+n.y,c.z+n.z);
			}
		}

		protected void drawWithoutTexture(){
			if(useWire){
				app.stroke(scol);
				app.strokeWeight(sweight);
			}
			else {
				app.noStroke();
			}
			if(useSolid)
				app.fill(col);
			else
				app.noFill();
			
			PVector c = centre;
			app.beginShape(TRIANGLE_FAN);
			app.normal(n.x, n.y, n.z);
			app.vertex(c.x, c.y, c.z);					
			for(int seg = 0; seg < edge.length; seg++) {
				c = edge[seg];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z);					
			}
			app.endShape(PApplet.CLOSE);
		}

		/**
		 * Draw using texture
		 */
		protected void drawWithTexture(){
			PVector c = centre;
			UV uv = new UV(0.5f, 0.5f);

			if(useSolid)
				app.fill(fillColor);
			if(useWire){
				app.stroke(scol);
				app.strokeWeight(sweight);
			}
			else {
				app.noStroke();
			}

			app.textureMode(NORMAL);
			app.beginShape(TRIANGLE_FAN);

			app.texture(capSkin);

			app.normal(n.x, n.y, n.z);
			app.vertex(c.x, c.y, c.z, uv.u, uv.v);
			for(int seg = 0; seg < edge.length; seg++) {
				c = edge[seg];
				uv = uvLoc[seg+1];	
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z, uv.u, uv.v);
			}
			app.endShape(PApplet.CLOSE);
		}

		protected void drawForPicker(PGraphics3D pickBuffer) {
			pickBuffer.pushMatrix();
			pickBuffer.translate(pos.x, pos.y, pos.z);
			pickBuffer.rotateX(rot.x);
			pickBuffer.rotateY(rot.y);
			pickBuffer.rotateZ(rot.z);
			pickBuffer.scale(shapeScale);
			pickBuffer.noStroke();

			PVector c = centre;
			if(visible){
				pickBuffer.beginShape(TRIANGLE_FAN);
				pickBuffer.fill(pickColor);
				pickBuffer.vertex(c.x, c.y, c.z);
				for(int seg = 0; seg < edge.length; seg++) {
					c = edge[seg];
					pickBuffer.vertex(c.x, c.y, c.z);					
				}
				pickBuffer.endShape(PApplet.CLOSE);
			}

			pickBuffer.popMatrix();
		}
		
	} // end of EndCap class

}
