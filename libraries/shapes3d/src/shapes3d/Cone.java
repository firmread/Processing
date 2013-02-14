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

package shapes3d;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PVector;
import shapes3d.utils.Rot;
import shapes3d.utils.Textures;
import shapes3d.utils.UV;

/**
 * A 3D cone shape. <br>
 * The centre of rotation is the centre of the flat end (base) and the cone 
 * point in the y [0,1,0] direction. <br>
 * 
 * @author Peter Lager
 *
 */
public class Cone extends Shape3D {
	

	protected Disc cone, base;

	protected float height = 80;
	protected float radX = 30, radZ = 30;

	protected int nSegs;

	/**
	 * Create a cone shape. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSegments number of segments (pizza slices)
	 */
	public Cone(PApplet app, int nbrSegments){
		super();
		this.app = app;
		nSegs = nbrSegments + 2;
		cone = new Disc();
		base = new Disc();
		calcShape();
	}

	/**
	 * Create a cone shape. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param nbrSegments number of segments (pizza slices)
	 * @param orrientation
	 * @param centreOfRotation
	 */
	public Cone(PApplet app, int nbrSegments, 
			PVector orrientation, PVector centreOfRotation){
		super();
		this.app = app;
		nSegs = nbrSegments + 2;
		cone = new Disc();
		base = new Disc();
		if(orrientation != null && orrientation.mag() > 0)
			this.up.set(orrientation);
		if(centreOfRotation != null)
			this.centreRot.set(centreOfRotation);
		calcShape();
	}

	/**
	 * Set radii of open end.
	 * @param radiusX
	 * @param radiusZ
	 */
	public void setRadius(float radiusX, float radiusZ){
		this.radX = radiusX;
		this.radZ = radiusZ;
		calcShape();
	}

	/**
	 * Set the height of the cone
	 * @param height
	 */
	public void setHeight(float height){
		this.height = height;
		calcShape();
	}

	/**
	 * Set the raddi of open end and height of the cone.
	 * @param radiusX
	 * @param radiusZ
	 * @param height
	 */
	public void setSize(float radiusX, float radiusZ, float height){
		this.radX = radiusX;
		this.radZ = radiusZ;
		this.height = height;
		calcShape();
	}

	@Override
	protected void calcShape() {
		calcXYZ();
	}

	/**
	 * Internal use to calculate the XYZ coordinates for a TRIANGLE_FAN.
	 */
	protected void calcXYZ() {
		float angle = 0, dAng = TWO_PI / (nSegs - 2.0f);

		PVector[] coord = new PVector[nSegs];
		PVector[] norm = new PVector[nSegs];

		coord = new PVector[nSegs];
		norm = new PVector[nSegs];

		coord[0] = new PVector(0, -height, 0);
		norm[0] = new PVector(0,-1,0);

		for(int i = 1; i < nSegs; i++){
			coord[i] = new PVector(radX * (float)Math.cos(angle), 0, radZ * (float)Math.sin(angle));
			angle -= dAng;
		}	

		// Calculate the normals
		norm[0] = new PVector(0,-1,0);
		PVector cp = coord[0];
		for(int i = 1; i < nSegs - 1; i++){
			norm[i] = PVector.cross(PVector.sub(cp, coord[i],null), 
					PVector.sub(coord[((i+1)%nSegs) ], coord[i], null),null);
		}
		norm[nSegs-1] = new PVector();
		norm[nSegs-1].set(norm[1]);

		// Apply any orientation or new rotation centre
		Rot orrient = new Rot(new PVector(0,1,0), up);
		for(int i = 0; i < nSegs; i++){
			orrient.applyTo(coord[i]);
			coord[i].add(this.centreRot);
			orrient.applyTo(norm[i]);
			norm[i].normalize();			
		}
		cone.setShape(coord, norm, 1);

		PVector baseCenter = new PVector(0,0,0);
		PVector baseNorm = new PVector(0,1,0);
		orrient.applyTo(baseCenter);
		baseCenter.add(this.centreRot);
		orrient.applyTo(baseNorm);
		baseNorm.normalize();
		coord[0] = baseCenter;
		norm = new PVector[] { baseNorm };
		base.setShape(coord, norm, -1);
	}

	/**
	 * Sets the texture for the cone surface only
	 * @param imgFile file name for image
	 */
	@Override
	public void setTexture(String imgFile){
		cone.discSkin = Textures.loadImage(app, imgFile);
	}
	
	/**
	 * Sets the texture for the cone surface only
	 * @param img texture image
	 */
	public void setTexture(PImage img){
		cone.discSkin = img;
	}
	
	/**
	 * Sets the image to be used for TOP, BASE or ALL depending
	 * on second parameter.
	 * @param imgFile image file for texture
	 * @param sides the parts are to be textured
	 */
	public void setTexture(String imgFile, int sides){
		skin = Textures.loadImage(app, imgFile);
		setTexture(skin, sides);
	}
	
	/**
	 * Sets the image to be used for TOP, BASE or ALL depending
	 * on second parameter.
	 * @param img the texture image
	 * @param sides the parts are to be textured
	 */
	public void setTexture(PImage img, int sides){
		if(img != null){
			skin = img;
			if(isFlagSet(sides, CONE))
				cone.discSkin = img;
			if(isFlagSet(sides, BASE))
				base.discSkin = img;
		}
	}
	
	/**
	 * Sets the visibility to be used for CONE, BASE or ALL depending
	 * on second parameter.
	 * @param visible
	 * @param sides Cone.BASE, Cone.CONE or Cone.ALL
	 */
	public void visible(boolean visible, int sides){
		if(isFlagSet(sides, CONE)){
			this.visible = visible;
			cone.visible = visible;
		}
		if(isFlagSet(sides, BASE))
			base.visible = visible;
	}
	
	/**
	 * Sets the fill color for the cone part
	 */
	public void fill(int col){
			fillColor = col;
			cone.col = col;
	}
	
	/**
	 * Sets the fill color to be used for CONE, BASE or ALL depending
	 * on second parameter.
	 * 
	 * @param col the color to apply
	 * @param sides Cone.BASE, Cone.CONE or Cone.ALL
	 */
	public void fill(int col, int sides){
		if(isFlagSet(sides, CONE)){
			fillColor = col;
			cone.col = col;
		}
		if(isFlagSet(sides, BASE))
			base.col = col;
	}
	
	/**
	 * Sets the wire colour for the cone top
	 */
	public void stroke(int col){
			fillColor = col;
			cone.col = col;
	}
	
	/**
	 * Sets the wire color to be used for CONE, BASE or ALL depending
	 * on second parameter.
	 * 
	 * @param col the wire color to apply
	 * @param sides Cone.BASE, Cone.CONE or Cone.ALL
	 */
	public void stroke(int col, int sides){
		if(isFlagSet(sides, CONE)){
			fillColor = col;
			cone.scol = col;
		}
		if(isFlagSet(sides, BASE))
			base.scol = col;
	}
	
	/**
	 * Sets the wire thickness for the cone part.
	 * 
	 * @param weight
	 */
	public void strokeWeight(float weight){
		cone.sweight = weight;
		strokeWeight = weight;
	}
	
	/**
	 * Sets the wire thickness for CONE, BASE or ALL depending
	 * on second parameter.
	 * 
	 * @param weight
	 * @param sides Cone.BASE, Cone.CONE or Cone.ALL
	 */
	public void strokeWeight(float weight, int sides){
		if(isFlagSet(sides, CONE)){
			cone.sweight = weight;
			strokeWeight = weight;
		}
		if(isFlagSet(sides, BASE)){
			base.sweight = weight;
		}
	}
	
	/**
	 * Sets the draw mode for the cone part. <br>
	 * The three constants Shape3D.SOILD, Shape3D.WIRE and Shape3D.TEXTURE are used 
	 * to determine how the shape is drawn. These can be ored together to get different
	 * drawing combinations e.g. Shape3D.SOLID | Shape3D.WIRE would be a coloured shape
	 * with edges. If the parameter is Shape3D.TEXTURE then and a texture has not
	 * been set then it will use SOLID.
	 * @param mode
	 */
	public void drawMode(int mode){
		cone.drawMode(mode);
	}

	/**
	 * Sets the colour to be used for TOP, BASE or ALL depending
	 * on second parameter.
	 * @param mode 
	 * @param sides Cone.BASE, Cone.CONE or Cone.ALL
	 */
	public void drawMode(int mode, int sides){
		if(isFlagSet(sides, CONE))
			cone.drawMode(mode);
		if(isFlagSet(sides, BASE))
			base.drawMode(mode);
	}

	/**
	 * Draw the cone according to drawMode.
	 */
	@Override
	public void draw() {
		if(visible){
			drawWhat();
			if(pickModeOn){
				pickBuffer.pushMatrix();
				if(pickable){
					if(cone.drawMode != WIRE)
						cone.drawForPicker(pickBuffer);
					if(base.visible && base.drawMode != WIRE)
						base.drawForPicker(pickBuffer);
					
					if(children != null){
						Iterator<Shape3D> iter = children.iterator();
						while(iter.hasNext())
							iter.next().drawForPicker(pickBuffer);
					}
				}
				pickBuffer.popMatrix();
			}
			else {
				app.pushStyle();
				app.pushMatrix();
				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);
				app.fill(fillColor);

				if(cone.visible)
					cone.draw();
				if(base.visible)
					base.draw();
				
				if(children != null){
					Iterator<Shape3D> iter = children.iterator();
					while(iter.hasNext())
						iter.next().draw();
				}
				app.popMatrix();
				app.popStyle();
			}
		}
	}

	// The cone has 2 of these top & base
	private class Disc {
		private boolean visible = true;
		private PImage discSkin;
		private int drawMode = SOLID;

		private boolean useWire = false;
		private boolean useSolid = true;
		private boolean useTexture = false;

		private int col, scol;
		private float sweight;
		
		private PVector[] coord;
		private PVector[] norm;
		private UV[] uvLoc;
		private float angDir;
		
		public Disc(){
			col = WHITE;
			scol = BLACK;
			sweight = strokeWeight;
		}

		public void setShape(PVector[] c, PVector[] n, float angDir){
			int nbrNorms = n.length;
			coord = new PVector[c.length];
			norm = new PVector[c.length];
			for(int i = 0; i < c.length; i++){
				coord[i] = new PVector(c[i].x, c[i].y, c[i].z);
				norm[i] = new PVector(n[i%nbrNorms].x, n[i%nbrNorms].y, n[i%nbrNorms].z);
			}	
			this.angDir = angDir;
			calcUV();
		}

		protected void drawWhat(){
			useSolid = ((drawMode & SOLID) == SOLID);
			useWire = ((drawMode & WIRE) == WIRE);
			useTexture = ((drawMode & TEXTURE) == TEXTURE && discSkin != null);
		}

		public void drawMode(int mode){
			mode &= DRAWALL;
			boolean validMode = ((mode & WIRE) != 0);
			validMode |= ((mode & SOLID) != 0);
			validMode |= ((mode & TEXTURE) != 0);
			if(!validMode)
				mode = SOLID;
			drawMode = mode;
		}
		
//		public void setTexture(PImage img){
//			skin = img;
//			if(img != null){
//				drawMode = TEXTURE;
//				skin = img;
//			}
//			else 
//				drawMode = SOLID;
//
//		}

		protected void calcUV() {
			float angle = 0, dAng = angDir * TWO_PI / (nSegs - 1.0f);
			uvLoc = new UV[nSegs];
			uvLoc[0] = new UV(0.5f, 0.5f);
			for(int i = 1; i < nSegs; i++){
				uvLoc[i] = new UV(0.5f + 0.5f * (float)Math.cos(angle), 0.5f + 0.5f * (float)Math.sin(angle));
				angle -= dAng;
			}	
		}

		public void draw(){
			if(visible){
				drawWhat();
				if(useTexture)
					drawWithTexture();
				else 
					drawWithoutTexture();
				//drawNormals();
			}
		}

		protected void drawWithoutTexture(){
			if(useSolid)
				app.fill(col);
			else
				app.noFill();
			if(useWire){
				app.stroke(scol);
				app.strokeWeight(sweight);
			}
			else {
				app.noStroke();
			}

			PVector c, n;

			app.beginShape(TRIANGLE_FAN);

			for(int seg = 0; seg < nSegs; seg++) {
				c = coord[seg];
				n = norm[seg];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z);					
			}
			app.endShape(PApplet.CLOSE);
		}

		/**
		 * Draw using texture
		 */
		protected void drawWithTexture(){
			PVector c, n;
			UV uv;

			if(useSolid)
				app.fill(col);
			else
				app.noFill();
			if(useWire){
				app.stroke(scol);
				app.strokeWeight(sweight);
			}
			else {
				app.noStroke();
			}

			app.textureMode(NORMAL);
			app.beginShape(TRIANGLE_FAN);
			app.texture(discSkin);
			for(int seg = 0; seg < nSegs; seg++) {
				c = coord[seg];
				n = norm[seg];
				uv = uvLoc[seg];	
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

				PVector c;
				pickBuffer.beginShape(TRIANGLE_FAN);
				pickBuffer.fill(pickColor);
				for(int seg = 0; seg < nSegs; seg++) {
					c = coord[seg];
					pickBuffer.vertex(c.x, c.y, c.z);					
				}
				pickBuffer.endShape(PApplet.CLOSE);
				pickBuffer.popMatrix();
		}
		
		public void drawNormals(){
			PVector c, n;
			app.stroke(255,0,0);
			for(int t = 0; t < nSegs; t++) {
				c = coord[t];
				n = PVector.mult(norm[t],20);
				app.line(c.x,c.y,c.z,c.x+n.x,c.y+n.y,c.z+n.z);
			}
		}

	}

}
