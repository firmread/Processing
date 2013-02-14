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

/**
 * A box shape where the length breadth and depth can be set by the user. <br>
 * Since the visibility, color and texture of each face can be controlled independently
 * so this class ignores the drawMode value. <br>
 * Note that texture tiling is not supported in this shape. <br> 
 * The centre of rotation is the centre of the box. <br>
 * 
 * @author Peter Lager
 *
 */
public class Box extends Shape3D {

	// Flags for faces
	public final static int FRONT 		= 1;
	public final static int BACK 		= 2;
	public final static int LEFT 		= 4;
	public final static int RIGHT 		= 8;
	public final static int BOTTOM 		= 16;
	public final static int TOP 		= 32;
	public final static int ALL_SIDES	= 63;

	// Face numbers
	protected final static int FRONT_N = 0;
	protected final static int BACK_N = 1;
	protected final static int LEFT_N = 2;
	protected final static int RIGHT_N = 3;
	protected final static int BOTTOM_N = 4;
	protected final static int TOP_N = 5;

	protected PVector[] coord;
	protected PVector[] fNorm;
	protected int[] fColor;
	protected int[] sColor;
	protected float[] sWeight;
	protected int[] dmode;
	protected PImage[] image;
	protected boolean[] fvisible;

	protected float w, h, d;

	public static String getFaceName(int faceNbr){
		switch(faceNbr){
		case Box.FRONT:
			return "Front";
		case Box.BACK:
			return "Back";
		case Box.LEFT:
			return "Left";
		case Box.RIGHT:
			return "Right";
		case Box.BOTTOM:
			return "Bottom";
		case Box.TOP:
			return "Top";
		}
		return "Unknown face";
	}

	/**
	 * Gets the flag of a face chosen at random. <br>
	 * The flag values for each face <br>
	 * FRONT (1) <br>
	 * BACK (2) <br>
	 * LEFT (4) <br>
	 * RIGHT (8) <br>
	 * BOTTOM (16) <br>
	 * TOP (32) <br>
	 * They are powers of 2 so they can be ORed together in any combination. <br>
	 * @return one of the flag values selected at random.
	 */
	public static int getRndFace(){
		return (1 << ((int)(Math.random()*6)));
	}

	/**
	 * Creates a cube of size 100. Defaults to different colour for each face. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 */
	public Box(PApplet app){
		super();
		this.app = app;
		w = h = d = 100;
		createArrays();
		calcShape();
	}

	/**
	 * Creates a cube of size 100. Defaults to different colour for each face. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param up
	 * @param centreOfRot
	 */
	public Box(PApplet app, PVector up, PVector centreOfRot){
		super();
		this.app = app;
		w = h = d = 100;
		if(up != null && up.mag() > 0)
			this.up.set(up);
		if(centreOfRot != null)
			this.centreRot.set(centreOfRot);
		createArrays();
		calcShape();
	}

	/**
	 * Creates a cube of a given size. <br>
	 * 
	 * @param app
	 * @param size length of a side
	 */
	public Box(PApplet app, float size){
		super();
		this.app = app;
		w = h = d = size;
		createArrays();
		calcShape();
	}

	/**
	 * Creates a cube of a given size. <br>
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param size
	 * @param up
	 * @param centreOfRot
	 */
	public Box(PApplet app, float size, PVector up, PVector centreOfRot){
		super();
		this.app = app;
		w = h = d = size;
		if(up != null && up.mag() > 0)
			this.up.set(up);
		if(centreOfRot != null)
			this.centreRot.set(centreOfRot);
		createArrays();
		calcShape();
	}

	/**
	 * Creates a box of the given size. Defaults to different colour for each face. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param width
	 * @param height
	 * @param depth
	 */
	public Box(PApplet app, float width, float height, float depth){
		super();
		this.app = app;
		w = width;
		h = height;
		d = depth;
		createArrays();
		calcShape();
	}

	/**
	 * 
	 * The shape is recalculated so the [0,1,0] axis is now 'up' <br>
	 * The shape's coordinates are translated to make 'centreOfRot'
	 * the objects centre of rotation. <br>
	 * 
	 * @param app the PApplet to draw this shape
	 * @param width
	 * @param height
	 * @param depth
	 * @param up
	 * @param centreOfRot
	 */
	public Box(PApplet app, float width, float height, float depth, 
			PVector up, PVector centreOfRot){
		super();
		this.app = app;
		w = width;
		h = height;
		d = depth;
		if(up != null && up.mag() > 0)
			this.up.set(up);
		if(centreOfRot != null)
			this.centreRot.set(centreOfRot);
		createArrays();
		calcShape();
	}

	protected void createArrays(){
		// Create array of PImage for textures
		image = new PImage[6];

		// Create corner coordinate vectors
		coord = new PVector[8];
		for(int i = 0; i < 8; i++)
			coord[i] = new PVector();

		// Initialise normals
		fNorm = new PVector[6];
		for(int i = 0; i < 6; i++)
			fNorm[i] = new PVector();

		// Initialise draw mode to SOLID
		dmode = new int[6];
		for(int i = 0; i < 6; i++)
			dmode[i] = SOLID;
		
		// Initialise stroke color to BLACK
		sColor = new int[6];
		for(int i = 0; i < 6; i++)
			sColor[i] = BLACK;
		
		// Initialise stroke color to BLACK
		sWeight = new float[6];
		for(int i = 0; i < 6; i++)
			sWeight[i] = 1.5f;
		
		// Make all faces visible
		fvisible = new boolean[6];
		for(int i = 0; i < 6; i++)
			fvisible[i] = true;


		// Set colours for each face
		fColor = new int[6];
		fColor[FRONT_N] = app.color(0,0,255);		// Blue
		fColor[BACK_N] = app.color(0,255,0);		// Green
		fColor[LEFT_N] = app.color(255,0,0);		// Red
		fColor[RIGHT_N] = app.color(255,128,0);		// Orange
		fColor[TOP_N] = app.color(255); 			// White
		fColor[BOTTOM_N] = app.color(255,255,0); 	// Yellow		
	}
	
	/**
	 * Override abstract method inShape3D
	 */
	@Override
	protected void calcShape(){
		calcXYZ();
	}

	/**
	 * Get the size in the x direction
	 * @return the w
	 */
	public float getWidth() {
		return w;
	}

	/**
	 * Get the size in the y direction
	 * @return the h
	 */
	public float getHeight() {
		return h;
	}

	/**
	 * Get the size in the z direction
	 * @return the d
	 */
	public float getDepth() {
		return d;
	}

	/**
	 * Used internally to calculate the 8 corners
	 */
	protected void calcXYZ() {
		coord[0].set(-0.5f*w, -0.5f*h,  0.5f*d);
		coord[1].set( 0.5f*w, -0.5f*h,  0.5f*d);
		coord[2].set( 0.5f*w,  0.5f*h,  0.5f*d);
		coord[3].set(-0.5f*w,  0.5f*h,  0.5f*d);
		coord[4].set(-0.5f*w, -0.5f*h, -0.5f*d);
		coord[5].set( 0.5f*w, -0.5f*h, -0.5f*d);
		coord[6].set( 0.5f*w,  0.5f*h, -0.5f*d);
		coord[7].set(-0.5f*w,  0.5f*h, -0.5f*d);
		fNorm[0].set( 0, 0, 1);
		fNorm[1].set( 0, 0,-1);
		fNorm[2].set(-1, 0, 0);
		fNorm[3].set( 1, 0, 0);
		fNorm[4].set( 0, 1, 0);
		fNorm[5].set( 0,-1, 0);

		// Apply new orientation and centre of rotation
		Rot orrient = new Rot(new PVector(0,1,0), up);
		for(int i = 0; i < 8; i++) {
			orrient.applyTo(coord[i]);
			coord[i].add(this.centreRot);
		}
		for(int i = 0; i < 6 ; i++){
			orrient.applyTo(fNorm[i]);
			fNorm[i].normalize();
		}
	}

	/**
	 * Sets the size of the box.
	 * @param width
	 * @param height
	 * @param depth
	 */
	public void setSize(float width, float height, float depth){
		w = width;
		h = height;
		d = depth;
		calcXYZ();
	}

	/**
	 * Sets the color of all six faces
	 * @param col
	 */
	public void fill(int col){
		for(int i = 0; i < 6; i++)
			fColor[i] = col;
	}

	/**
	 * Set the color for one or more faces using boolean flags <br>
	 * FRONT, BACK, RIGHT, TOP, BOTTOM, ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * 
	 * @param col
	 * @param faces
	 */
	public void fill(int col, int faces){
		int fn = 0;
		if(faces > 0){
			while(faces > 0){
				if(faces % 2 == 1)
					fColor[fn] = col;
				faces /= 2;
				fn++;
			}
		}
	}

	/**
	 * Sets the draw mode of all six faces
	 * @param mode
	 */
	public void drawMode(int mode){
		for(int i = 0; i < 6; i++)
			dmode[i] = mode;
	}

	/**
	 * Set the draw mode for one or more faces using boolean flags <br>
	 * FRONT, BACK, RIGHT, TOP, BOTTOM, ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * 
	 * @param mode
	 * @param faces
	 */
	public void drawMode(int mode, int faces){
		int fn = 0;
		if(faces > 0){
			while(faces > 0){
				if(faces % 2 == 1)
					dmode[fn] = mode;
				faces /= 2;
				fn++;
			}
		}
	}

	/**
	 * Sets the stroke color of all six faces
	 * @param col
	 */
	public void stroke(int col){
		for(int i = 0; i < 6; i++){
			sColor[i] = col;
		}
	}

	/**
	 * Set the stroke color for one or more faces using boolean flags <br>
	 * FRONT, BACK, RIGHT, TOP, BOTTOM, ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * 
	 * @param col
	 * @param faces
	 */
	public void stroke(int col, int faces){
		int fn = 0;
		if(faces > 0){
			while(faces > 0){
				if(faces % 2 == 1)
					sColor[fn] = col;
				faces /= 2;
				fn++;
			}
		}
	}

	/**
	 * Sets the stroke weight of all six faces
	 * @param weight
	 */
	public void strokeWeight(float weight){
		for(int i = 0; i < 6; i++)
			sWeight[i] = weight;
	}

	/**
	 * Set the stroke weight for one or more faces using boolean flags <br>
	 * FRONT, BACK, RIGHT, TOP, BOTTOM, ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * 
	 * @param weight
	 * @param faces
	 */
	public void strokeWeight(float weight, int faces){
		int fn = 0;
		if(faces > 0){
			while(faces > 0){
				if(faces % 2 == 1)
					sWeight[fn] = weight;
				faces /= 2;
				fn++;
			}
		}
	}

	/**
	 * Set the visibility for one or more faces using boolean flags <br>
	 * Box.FRONT, Box.BACK, Box.RIGHT, Box.TOP, Box.BOTTOM, Box.ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * 
	 * @param visible
	 * @param faces
	 */
	public void visible(boolean visible, int faces){
		int fn = 0;
		if(faces > 0){
			while(faces > 0){
				if(faces % 2 == 1)
					fvisible[fn] = visible;
				faces /= 2;
				fn++;
			}
		}
	}

	/**
	 * Set the texture to be used on all faces.
	 * @param fname
	 */
	public void setTexture(String fname){
		PImage img = Textures.loadImage(app, fname);
		setTexture(img);
	}

	/**
	 * Set the image to be used as texture on all faces
	 * @param img the texture image
	 */
	public void setTexture(PImage img){
		skin = img;
		if(skin != null){
			app.textureMode(NORMAL);
			for(int i = 0; i < 6; i++)
				image[i] = skin;
		}		
	}
	
	/**
	 * Set the texture for one or more faces using boolean flags <br>
	 * Box.FRONT, Box.BACK, Box.RIGHT, Box.TOP, Box.BOTTOM, Box.ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * @param fname filename of the image files
	 * @param faces the faces to be textured
	 */
	public void setTexture(String fname, int faces){
		PImage img = Textures.loadImage(app, fname);
		setTexture(img, faces);
	}

	/**
	 * Set the texture image for one or more faces using boolean flags <br>
	 * Box.FRONT, Box.BACK, Box.RIGHT, Box.TOP, Box.BOTTOM, Box.ALL_SIDES <br>
	 * Or these together to get different combinations.
	 * @param img
	 * @param faces
	 */
	public void setTexture(PImage img, int faces){
		int fn = 0;
		if(faces > 0){
			skin = img;
			if(skin != null){
				while(faces > 0){
					if(faces % 2 == 1)
						image[fn] = skin;
					faces /= 2;
					fn++;
				}
			}
		}
	}

	/**
	 * Loads as many images as possible using the filenames provided and
	 * use these as textures. 
	 * @param fnames
	 */
	public void setTextures(String[] fnames){
		for(int i = 0; i < 6; i++){
			image[i] = Textures.loadImage(app, fnames[i % fnames.length]);
		}
	}

	/**
	 * Uses as many images as provided and use these to texture the sides.
	 * @param imgs
	 */
	public void setTextures(PImage[] imgs){
		for(int i = 0; i < 6; i++)
			image[i] = imgs[i % imgs.length];
	}

	protected void drawWhat(int faceNo){
		useSolid = ((dmode[faceNo] & SOLID) == SOLID);
		useWire = ((dmode[faceNo] & WIRE) == WIRE);
		useTexture = ((dmode[faceNo] & TEXTURE) == TEXTURE);
	}

	@Override
	public void draw() {
		if(visible){
			drawWhat();
			if(pickModeOn){
				if(pickable && drawMode != WIRE){
					pickBuffer.pushMatrix();
					if(pickable && drawMode != WIRE)
						drawForPicker(pickBuffer);
					
					// Now do any children
					if(children != null){
						Iterator<Shape3D> iter = children.iterator();
						while(iter.hasNext())
							iter.next().drawForPicker(pickBuffer);
					}
					pickBuffer.popMatrix();
				}
			}
			else {
				app.pushStyle();
				app.pushMatrix();
				
				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);

				if(fvisible[FRONT_N])
					drawFace(FRONT_N,coord[0],coord[1],coord[2],coord[3]);
				if(fvisible[BACK_N])
					drawFace(BACK_N,coord[5],coord[4],coord[7],coord[6]);
				if(fvisible[LEFT_N])
					drawFace(LEFT_N, coord[4],coord[0],coord[3],coord[7]);
				if(fvisible[RIGHT_N])
					drawFace(RIGHT_N, coord[1],coord[5],coord[6],coord[2]);
				if(fvisible[BOTTOM_N])
					drawFace(BOTTOM_N,coord[3],coord[2],coord[6],coord[7]);
				if(fvisible[TOP_N])
					drawFace(TOP_N,coord[4],coord[5],coord[1],coord[0]);

				// drawNormals();
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

	protected void drawFace(int faceNbr, PVector c0, PVector c1, PVector c2, PVector c3){
		drawWhat(faceNbr);
		app.beginShape(QUADS);
		if(useSolid)
			app.fill(fColor[faceNbr]);
		else
			app.noFill();
		if(useWire){
			app.stroke(sColor[faceNbr]);
			app.strokeWeight(sWeight[faceNbr]);
		}
		else {
			app.noStroke();
		}
		app.normal(fNorm[faceNbr].x, fNorm[faceNbr].y, fNorm[faceNbr].z);

		if(image[faceNbr] != null && useTexture){
			app.textureMode(NORMAL);
			app.texture(image[faceNbr]);
			app.vertex(c0.x, c0.y, c0.z, 0, 0);
			app.vertex(c1.x, c1.y, c1.z, 1, 0);
			app.vertex(c2.x, c2.y, c2.z, 1, 1);
			app.vertex(c3.x, c3.y, c3.z, 0, 1);			
		}
		else {
			app.vertex(c0.x, c0.y, c0.z);
			app.vertex(c1.x, c1.y, c1.z);
			app.vertex(c2.x, c2.y, c2.z);
			app.vertex(c3.x, c3.y, c3.z);
		}
		app.endShape();
		// drawNormals(c0,c1,c2,c3,norm[faceNbr]);
	}

	/**
	 * Draw box for picker
	 */
	protected void drawForPicker(PGraphics3D pickBuffer) {
		pickBuffer.translate(pos.x, pos.y, pos.z);
		pickBuffer.rotateX(rot.x);
		pickBuffer.rotateY(rot.y);
		pickBuffer.rotateZ(rot.z);
		pickBuffer.scale(shapeScale);
		pickBuffer.noStroke();

		if(fvisible[FRONT_N])
			drawFacePicker(FRONT_N,coord[0],coord[1],coord[2],coord[3]);
		if(fvisible[BACK_N])
			drawFacePicker(BACK_N,coord[5],coord[4],coord[7],coord[6]);
		if(fvisible[LEFT_N])
			drawFacePicker(LEFT_N, coord[4],coord[0],coord[3],coord[7]);
		if(fvisible[RIGHT_N])
			drawFacePicker(RIGHT_N, coord[1],coord[5],coord[6],coord[2]);
		if(fvisible[BOTTOM_N])
			drawFacePicker(BOTTOM_N,coord[3],coord[2],coord[6],coord[7]);
		if(fvisible[TOP_N])
			drawFacePicker(TOP_N,coord[4],coord[5],coord[1],coord[0]);
	}


	protected void drawFacePicker(int faceN, PVector c0, PVector c1, PVector c2, PVector c3) {
		pickBuffer.beginShape(QUADS);
		pickBuffer.fill(pickColor);
		pickBuffer.vertex(c0.x, c0.y, c0.z);
		pickBuffer.vertex(c1.x, c1.y, c1.z);
		pickBuffer.vertex(c2.x, c2.y, c2.z);
		pickBuffer.vertex(c3.x, c3.y, c3.z);
		pickBuffer.endShape();
	}

	/**
	 * Used for testing calculation of normals only.
	 * @param c0
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param n
	 */
	protected void drawNormals(PVector c0, PVector c1, PVector c2, PVector c3, PVector n){
		app.pushStyle();
		n = PVector.mult(n,5);
		app.stroke(255,0,0);
		app.strokeWeight(1);
		app.noFill();
		app.line(c0.x, c0.y, c0.z, c0.x + n.x, c0.y + n.y, c0.z + n.z);
		app.line(c1.x, c1.y, c1.z, c1.x + n.x, c1.y + n.y, c1.z + n.z);
		app.line(c2.x, c2.y, c2.z, c2.x + n.x, c2.y + n.y, c2.z + n.z);
		app.line(c3.x, c3.y, c3.z, c3.x + n.x, c3.y + n.y, c3.z + n.z);
		app.popStyle();			
	}
}

