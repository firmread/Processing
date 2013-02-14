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

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PVector;
import shapes3d.animation.VCam;
import shapes3d.utils.Textures;

/**
 * A simple terrain class. <br>
 * When created the terrain will be centred in the horizontal plane about world 
 * position [X=0, Z=0] with terrain height on the Y axis. <br>
 * At present this class does not allow the user to move or rotate the terrain, this
 * is to simplify coding of some of the methods and maybe relaxed in later versions. <br><br>
 * 
 * In Processing the positive Y direction is down, this is the opposite to OpenGL. This class
 * uses Processing coordinates so the higher the terrain the smaller (more negative the number).
 * This has implications when calculating the height map, and positioning objects relative
 * to the terrain surface. 
 * <a href="http://www.processing.org/reference/environment/#Coordinates">Processing coordinates</a>
 * 
 * @author Peter Lager
 *
 */
public class Terrain extends Shape3D  {

	public static final int CLAMP = 1;
	public static final int WRAP = 2;

	protected PVector[][] coord;
	protected PVector[][] norm;

	protected TSection[] tSections = new TSection[9];
	protected ArrayList<TSection> tSectionsToDraw = new ArrayList<TSection>();

	protected float horizon;
	protected float terrainSize;		// real world size
	protected float halfTerrainSize;	// real world size
	protected float gridSize;			// strip size (real world)
	protected int gSlices;				// must be power of 2 (number of physical strips E-W N-S)
	protected int gSteps;				// gSlices + 1

	protected float deltaUV;
	protected float	nearlyOne;

	protected boolean wrapEnabled = false;

	public VCam cam = null;

	/**
	 * Create a square flat terrain. <br>
	 * The 'real world' size is given by terrainSize. The drawing detail level is given
	 * by gridSlices (e.g. gridSlices would be 8 for a chessboard). <br>
	 * gridSlices must be an even number >= 8 if the value passed does not meet these 
	 * conditions it is incremented to the next even number >= 8.
	 * 
	 * @param app
	 * @param gridSlices must be an even number >= 8 (e.g. 8, 10, 12 etc)
	 * @param terrainSize size in 3D world
	 * @param horizon the visibility limit for drawing wrap round
	 */
	public Terrain(PApplet app, int gridSlices, float terrainSize, float horizon){
		super();
		this.app = app;
		this.terrainSize = terrainSize;
		halfTerrainSize = terrainSize /2;
		this.horizon = Math.min(horizon, 0.95f * terrainSize);
		gSlices = acceptableValue(gridSlices);
		gridSize = terrainSize / gSlices;
		gSteps = gSlices + 1;
		calcTerrainSections();
		calcXYZ(1, null);
	}

	/**
	 * Create a square terrain using a heightmap. <br>
	 * The 'real world' size is given by terrainSize. The drawing detail level is given
	 * by gridSlices (e.g. gridSlices would be 8 for a chessboard). <br>
	 * gridSlices must be an even number >= 8 if the value passed does not meet these 
	 * conditions it is incremented to the next even number >= 8. <br>
	 * The heights for each vertex is calculated for the value in the heightMap array times
	 * the heightMult. This is useful because we can easily create a normalised height map (all 
	 * values in range 0-1) using perlin noise or similar and then provide a multiply depending
	 * on the terrainSize. <br>
	 * If the size of heightMap array does not match the terrain size then either extra values
	 * are ignored or the heightMap is wrapped round the terrain. Useful because if the heightMap
	 * is of size [gridSlices][gridslices] the the heights wrap round i.e. heights on the 
	 * north/south edges and the east/west edges match - useful to when implementing an infinite
	 * terrain (my next step).<br>
	 * 
	 * @param app
	 * @param gridSlices must be an even number >= 8 (e.g. 8, 10, 12 etc)
	 * @param terrainSize size in 3D world
	 * @param heightMult multiplier for heights in heightMap array
	 * @param heightMap 2D float array representing heights
	 */
	public Terrain(PApplet app, int gridSlices, float terrainSize, float horizon, float heightMult, float[][] heightMap){
		super();
		this.app = app;
		this.terrainSize = terrainSize;
		halfTerrainSize = terrainSize /2;
		this.horizon = Math.min(horizon, 0.95f * terrainSize);
		gSlices = acceptableValue(gridSlices);
		gridSize = terrainSize / gSlices;
		gSteps = gSlices + 1;
		calcTerrainSections();
		calcXYZ(heightMult, heightMap);
	}

	/**
	 * Create 9 terrain sections with actual terrain in centre. Used internally 
	 * to wrap terrain.
	 */
	protected void calcTerrainSections(){
		int c = 0;	
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				tSections[c] = new TSection(i * terrainSize - halfTerrainSize,
						j* terrainSize - halfTerrainSize,
						i * terrainSize + halfTerrainSize,
						j* terrainSize + halfTerrainSize);
				c++;
			}
		}
	}

	/**
	 * Empty method
	 */
	@Override
	protected void calcShape(){ }
	
	protected void calcXYZ(float heightMult, float[][] heightMap){
		coord = new PVector[gSteps][gSteps];
		norm = new PVector[gSteps][gSteps];

		float startEW = - 0.5f * terrainSize;
		float startNS = - 0.5f * terrainSize;
		float currEW = startEW, currNS = startNS;
		int ew, ns;

		for(ew = 0; ew < gSteps; ew++ ){
			currNS = startNS;
			for(ns = 0; ns < gSteps; ns++){
				coord[ew][ns] = new PVector(currEW,0,currNS);
				norm[ew][ns] = new PVector(0,1,0);

				currNS += gridSize;
			}
			currEW += gridSize;
		}
	}

	/**
	 * Create the height map from a 2D array of floats and a multiplier.
	 * 
	 * @param heightMult
	 * @param heightMap
	 */
	public void applyHeightMap(float heightMult, float[][] heightMap){
		int ewMap = 1, nsMap = 1;

		if(heightMap != null){
			ewMap = heightMap.length;
			nsMap = heightMap[0].length;
		}
		int ew, ns;

		for(ew = 0; ew < gSteps; ew++ ){
			for(ns = 0; ns < gSteps; ns++){
				coord[ew][ns].y = heightMult * heightMap[ew % ewMap][ns % nsMap];
			}
		}
		calcNormals();
	}

	/**
	 * Create a height map based on Perlin noise
	 * @param minHeight the minimum height permitted in the map
	 * @param maxHeight the maximum height permitted in the map
	 * @param pOffsetX
	 * @param pOffsetY
	 */
	public void usePerlinNoiseMap(float minHeight, float maxHeight, float pOffsetX, float pOffsetY){
		float hm[][] = new float[gSlices][gSlices];
		float xoff = 1.0f, yoff;
		float deltaH = maxHeight - minHeight;
		for(int i = 0; i < gSlices; i++){
			yoff = 1.0f;
			for(int j = 0; j < gSlices; j++){
				hm[i][j] = -1 * deltaH * app.noise(xoff, yoff) + minHeight;
				yoff += pOffsetY;
			}
			xoff += pOffsetX;
		}	
		applyHeightMap(1, hm);
	}

	/**
	 * Set the texture to be used for this Shape (no tiling) <br>
	 * If the image is loaded successfully then the drawMode is changed 
	 * to TEXTURE. 
	 * @param fname image filename
	 */
	public void setTexture(String fname){
		skin = Textures.loadImage(app, fname);
		setTexture(skin, 1);
	}

	/**
	 * Set the texture to be used for this Shape and the number of
	 * texture repeats (tiling) <br>
	 * textureRepeats must be an even number and a factor of gridSlices 
	 * (as specified in the constructors). If not it will 
	 * be changed to meet this conditions. <br>
	 * If the image is loaded successfully then the drawMode is changed 
	 * to TEXTURE. 
	 * 
	 * @param fname image filename
	 * @param textureRepeats
	 */
	public void setTexture(String fname, int textureRepeats){
		skin = Textures.loadImage(app, fname);
		setTexture(skin, textureRepeats);
	}

	/**
	 * Set the texture to be used for this Shape (no tiling) <br>
	 * If the image is loaded successfully then the drawMode is changed 
	 * to TEXTURE. 
	 * @param img the image to use
	 */
	public void setTexture(PImage img){
		setTexture(img, 1);
	}
	
	/**
	 * Set the texture to be used for this Shape and the number of
	 * texture repeats (tiling) <br>
	 * textureRepeats must be a factor of gridSlices 
	 * (as specified in the constructors). If not it will 
	 * be changed to meet this conditions. <br>
	 * If the image is loaded successfully then the drawMode is changed 
	 * to TEXTURE. 
	 * 
	 * @param img the image to use
	 * @param textureRepeats
	 */
	public void setTexture(PImage img, int textureRepeats){
		if(img != null){
			int texRepeats = multipleOf(gSlices, textureRepeats);
			deltaUV = texRepeats / (float)(gSlices);
			nearlyOne = 1.0f - deltaUV / 10.0f;
			skin = img;
		}
	}
	
	/**
	 * Draw the terrain.
	 */
	public void draw(){
		tSectionsToDraw.clear();
		TSection overlap;
		// Calculate area of terrain to be drawn
		if(cam != null){
			// Camera present so use it to limit terrain drawing
			TSection camArea = this.getCamDrawArea();
			int sectionNo = 0;
			for(int i = -1; i <= 1; i++){
				for(int j = -1; j <= 1; j++){
					//   one of 9 possible position (camera area covered)
					overlap = tSections[sectionNo].intersection(camArea);
					if(overlap != null){
						overlap.tx = i * terrainSize;
						overlap.tz = j* terrainSize;
						overlap.calcDrawGrid(gridSize);
						tSectionsToDraw.add(overlap);
					}
					sectionNo++;
				}
			}
		}
		else {
			// No camera draw whole terrain
			overlap = new TSection(0,0,gSteps,gSteps);
			overlap.eEW = gSteps;
			overlap.eNS = gSteps;
			tSectionsToDraw.add(overlap);
		}

		// Now draw them
		if(pickModeOn){
			for(int i = 0; i < tSectionsToDraw.size(); i++)
				drawForPicker(pickBuffer, tSectionsToDraw.get(i));
		}
		else {
			app.pushStyle();
			app.pushMatrix();
			if(visible){
				switch(drawMode){
				case TEXTURE:
					for(int i = 0; i < tSectionsToDraw.size(); i++)
						drawWithTexture(tSectionsToDraw.get(i));
					break;
				case WIRE:
					for(int i = 0; i < tSectionsToDraw.size(); i++)
						drawWireFrame(tSectionsToDraw.get(i));		
					break;
				case SOLID:
				default:
					for(int i = 0; i < tSectionsToDraw.size(); i++)
						drawWithColor(tSectionsToDraw.get(i));		
				}
			}
			app.popMatrix();
			app.popStyle();
		}
	}

	/**
	 * Use this if drawMode is set to TEXTURE
	 */
	protected void drawWithTexture(TSection overlap){
		float currU = 0, currV = 0;
		float startU, startV;
		PVector c, n;

		app.pushStyle();
		app.pushMatrix();
		
		app.translate(overlap.tx, pos.y, overlap.tz);
		app.textureMode(NORMAL);
		app.noStroke();
		app.fill(fillColor);

		startU = overlap.sEW * deltaUV;
		while(startU > 1) startU -= 1;
		//		if(startU > nearlyOne) startU = 0;
		currU = startU;

		startV = overlap.sNS * deltaUV;
		while(startV > 1) startV -= 1;
		if(startV > nearlyOne) startV = 0;
		currV = startV;

		for(int ns = overlap.sNS; ns < overlap.eNS-1; ns++) {
			app.beginShape(TRIANGLE_STRIP);
			app.texture(skin);
			currU = startU;
			for(int ew = overlap.sEW; ew < overlap.eEW; ew++) {
				// If we have reached the last U (U ~= 1) then draw this point
				// end the shape, reset U to 0 and start a new shape.
				if(currU > nearlyOne){
					c = coord[ew][ns];
					n = norm[ew][ns];
					app.normal(n.x, n.y, n.z);
					app.vertex(c.x, c.y, c.z, currU, currV);

					c = coord[ew][ns+1];
					n = norm[ew][ns+1];
					app.normal(n.x, n.y, n.z);
					app.vertex(c.x, c.y, c.z, currU, currV + deltaUV);
					app.endShape();
					currU = 0;
					app.beginShape(TRIANGLE_STRIP);
					app.texture(skin);
				}
				c = coord[ew][ns];
				n = norm[ew][ns];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z, currU, currV);

				c = coord[ew][ns+1];
				n = norm[ew][ns+1];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z, currU, currV + deltaUV);
				currU += deltaUV;
			} // t for end
			currV += deltaUV;
			if(currV > nearlyOne) currV = 0;
			app.endShape();
		} // p for end	
		if(children != null){
			Iterator<Shape3D> iter = children.iterator();
			Shape3D shape;
			while(iter.hasNext()){
				shape = iter.next();
				if(overlap.isInside(shape.x(), shape.z()))
					shape.draw();
			}
		}
		app.popMatrix();
		app.popStyle();
	}


	/**
	 * Use this if drawMode is set to WIRE <br>
	 * Although other draw modes use a TRIANGLE_STRIP this node uses
	 * a QUAD_STRIP for aesthetics.
	 */
	protected void drawWireFrame(TSection overlap){
		PVector c, n;

		app.pushStyle();
		app.pushMatrix();
		app.translate(overlap.tx, pos.y, overlap.tz);
		for(int p = overlap.sNS; p < overlap.eNS-1; p++) {
			app.beginShape(QUAD_STRIP);
			app.noFill();
			app.stroke(this.strokeColor);
			app.strokeWeight(this.strokeWeight);

			for(int t = overlap.sEW; t < overlap.eEW; t++) {
				c = coord[t][p];
				n = norm[t][p];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z);					

				c = coord[t][p+1];
				n = norm[t][p+1];
				app.normal(n.x, n.y, n.z);	
				app.vertex(c.x, c.y, c.z);					
			}
			app.endShape();
		}
		if(children != null){
			Iterator<Shape3D> iter = children.iterator();
			Shape3D shape;
			while(iter.hasNext()){
				shape = iter.next();
				if(overlap.isInside(shape.x(), shape.z()))
					shape.draw();
			}
		}
		app.popMatrix();
		app.popStyle();
	}

	/**
	 * Use this if drawMode is set to SOLID
	 */
	protected void drawWithColor(TSection overlap){
		PVector c, n;

		app.pushStyle();
		app.pushMatrix();
		app.translate(overlap.tx, pos.y, overlap.tz);
		for(int p = overlap.sNS; p < overlap.eNS-1; p++) {
			app.beginShape(TRIANGLE_STRIP);
			app.fill(fillColor);
			app.noStroke();
			
			for(int t = overlap.sEW; t < overlap.eEW; t++) {
				c = coord[t][p];
				n = norm[t][p];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z);					

				c = coord[t][p+1];
				n = norm[t][p+1];
				app.normal(n.x, n.y, n.z);	
				app.vertex(c.x, c.y, c.z);					
			}
			app.endShape();
		}
		if(children != null){
			Iterator<Shape3D> iter = children.iterator();
			Shape3D shape;
			while(iter.hasNext()){
				shape = iter.next();
				if(overlap.isInside(shape.x(), shape.z()))
					shape.draw();
			}
		}
		app.popMatrix();
		app.popStyle();
	}


	/**
	 * Draw for shape picking.
	 * @param pickBuffer
	 * @param overlap
	 */
	protected void drawForPicker(PGraphics3D pickBuffer, TSection overlap){
		pickBuffer.pushMatrix();
		pickBuffer.translate(overlap.tx, pos.y, overlap.tz);
		// Ignore if wire frame but still consider children
		if(visible && pickable && drawMode != WIRE){
			PVector c;
			for(int p = overlap.sNS; p < (overlap.eNS-1); p++) {
				pickBuffer.beginShape(TRIANGLE_STRIP);
				pickBuffer.fill(pickColor);

				for(int t = overlap.sEW; t < overlap.eEW; t++) {
					c = coord[t][p];
					pickBuffer.vertex(c.x, c.y, c.z);					

					c = coord[t][p+1];
					pickBuffer.vertex(c.x, c.y, c.z);					
				}
				pickBuffer.endShape();
			}
		}
		if(children != null){
			Iterator<Shape3D> iter = children.iterator();
			Shape3D shape;
			while(iter.hasNext()){
				shape = iter.next();
				if(overlap.isInside(shape.x(), shape.z()))
					shape.draw();
			}
		}
		pickBuffer.popMatrix();
	}

	/**
	 * Used for testing only
	 */
	protected void drawNormals(){
		PVector n, c;
		app.stroke(255,0,0);
		for(int p = 0; p < gSteps; p++) {
			for(int t = 0; t < gSteps; t++) {
				c = coord[t][p];
				n = PVector.mult(norm[t][p],20);
				app.line(c.x,c.y,c.z,c.x+n.x,c.y+n.y,c.z+n.z);
			}
		}	
		app.noStroke();
	}

	/**
	 * Used internally to calculate the normals.
	 */
	protected void calcNormals() {
		PVector c , np, nt;
		int ns, ew;
		boolean lastEW, lastNS, lastOne;

		for(ns = 0; ns < gSteps; ns++) {
			lastNS = (ns == gSteps - 1);
			for(ew = 0; ew < gSteps; ew++) {
				lastEW = (ew == gSteps -1);
				lastOne = lastNS & lastEW;
				c = coord[ew][ns];
				if(lastNS)
					np = coord[ew][ns-1];
				else
					np = coord[ew][ns+1];

				if(lastEW)
					nt = coord[ew-1][ns];
				else 
					nt = coord[ew+1][ns];

				if( (lastEW || lastNS) && !lastOne)
					norm[ew][ns] = PVector.cross(PVector.sub(nt, c, null), 
							PVector.sub(c, np, null), 
							null);
				else
					norm[ew][ns] = PVector.cross(PVector.sub(nt, c, null), 
							PVector.sub(np, c, null), 
							null);

				norm[ew][ns].normalize();
			}
		}	
	}

	/**
	 * Change the positional vector (target) so that its x/z position is
	 * over the terrain. <br>
	 * Use mode = Terrain.CLAMP or Terrain.WRAP to determine whether to 
	 * force the target to remain over the terrain or wrap to the other side. <br>
	 * 
	 * @param target the position to adjust.
	 * @param mode Terrain.CLAMP or Terrain.WRAP
	 */
	public void adjustPosition(PVector target, int mode){
		switch(mode){
		case CLAMP:
			if(target.x < -halfTerrainSize) target.x = - halfTerrainSize;
			if(target.x > halfTerrainSize) target.x = halfTerrainSize;
			if(target.z < -halfTerrainSize) target.z = - halfTerrainSize;
			if(target.z > halfTerrainSize) target.z = halfTerrainSize;
			break;
		case WRAP:
			if(target.x < -halfTerrainSize) target.x += terrainSize;
			if(target.x > halfTerrainSize) target.x -= terrainSize;
			if(target.z < -halfTerrainSize) target.z += terrainSize;
			if(target.z > halfTerrainSize) target.z -= terrainSize;
			break;		
		}
	}

	/**
	 * Calculate the terrain height given a real world position. <br>
	 * Position must have been 'adjusted' to be over the terrain before 
	 * calling this method.
	 * 
	 * @param x
	 * @param z
	 * @return the terrain height (y) for position (x,z)
	 */
	public float getHeight(float x, float z){
		float px = (x + halfTerrainSize) / gridSize;
		float pz = (z + halfTerrainSize) / gridSize;

		int col0 = (int) px;
		int row0 = (int) pz;
		int col1 = (col0 + 1) % gSteps;
		int row1 = (row0 + 1) % gSteps;

		// calculate the position px,pz relative to the cell
		// tx & tz are in range >=0 and <= 1
		float tx = px - col0;
		float tz = pz - row0;
		float txtz = tx * tz;

		float h00 = coord[col0][row0].y;
		float h01 = coord[col1][row0].y;
		float h11 = coord[col1][row1].y;
		float h10 = coord[col0][row1].y;

		// Depending on which triangle pX, pZ is in correct the height
		// at the 'opposite' corner to make a flat square before
		// bilinear interpolation
		if (tz < 1 - tx)
			h11 = h00 + 2*((h01 + h10)/2 - h00);
		else
			h00 = h11 + 2*((h01 + h10)/2 - h11);

		// the final step is to perform a bilinear interpolation
		// to compute the height of the terrain directly below
		// the object.
		float final_height = h00 * (1.0f - tz - tx + txtz)
		+ h01 * (tx - txtz)
		+ h11 * txtz
		+ h10 * (tz - txtz);
		// System.out.println("["+h00+" "+h01+" "+h11+" "+h10+"] {"+tx+" "+tz+"   TH= " + final_height);
		return pos.y + final_height;
	}

	/**
	 * FOR INTERNAL USE ONLY
	 * An acceptable number of slices must be >= 8 and divisible by 2;
	 * @param n
	 * @return
	 */
	protected int acceptableValue(int n){
		n = Math.max(n, 8);
		if(n%2 != 0) n++;
		return n;
	}

	/**
	 * FOR INTERNAL USE ONLY
	 * If needed change small so it is >= 1 and a factor of big
	 * @param big
	 * @param small
	 * @return
	 */
	protected int multipleOf(int big, int small){
		if(small < 1)
			small = 1;
		else {
			small = Math.min(big,small);
			while(big % small != 0)
				small--;
		}
		return small;
	}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveTo(PVector pos) {}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveTo(float x, float y, float z){}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveTo(float[] xyz){}

	/**
	 * Not allowed to move Terrain
	 */
	public void x(float x){}

	/**
	 * Can change the y position of terrain.
	 */
	public void y(float y){
		pos.y = y;
	}

	/**
	 * Not allowed to move Terrain
	 */
	public void z(float z){}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveBy(PVector move){}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveBy(float[] move){}

	/**
	 * Not allowed to move Terrain
	 */
	public void moveBy(float x, float y, float z){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateTo(PVector angles) {}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateTo(float[] angles){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateTo(float x, float y, float z) {}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateToX(float x){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateToY(float y){}

	/**
	 * Set the rotation about the z axis
	 * @param z
	 */
	public void rotateToZ(float z){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateBy(float x, float y, float z){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateBy(float[] angle){}

	/**
	 * Not allowed to rotate Terrain
	 */
	public void rotateBy(PVector angles){}

	/** 
	 * Calculate area that needs to be drawn for the camera
	 * @return
	 */
	protected TSection getCamDrawArea(){
		float sz, sx, ex, ez;
		PVector leftSide, rightSide, centre;
		PVector lookDir = cam.lookDir();
		float aspectRatio = cam.aspectRatio();
		float fov  = cam.fov();
		if(aspectRatio > 1.0f)
			fov *= aspectRatio;

		float sinA = (float) Math.sin(-fov);
		float cosA = (float) Math.cos(-fov);
		leftSide = new PVector(lookDir.x * cosA - lookDir.z * sinA,
				0,
				lookDir.x * sinA + lookDir.z * cosA);
		leftSide.normalize();
		leftSide.mult(horizon);
		leftSide.add(pos);
		sinA = (float) Math.sin(fov);
		cosA = (float) Math.cos(fov);

		rightSide = new PVector(lookDir.x * cosA - lookDir.z * sinA,
				0,
				lookDir.x * sinA + lookDir.z * cosA);
		rightSide.normalize();
		rightSide.mult(horizon);
		rightSide.add(pos);

		centre = new PVector(lookDir.x,0,lookDir.z);
		centre.normalize();
		centre.mult(horizon);
		centre.add(pos);

		sx = Math.min(pos.x, Math.min(centre.x, Math.min(leftSide.x, rightSide.x)));
		ex = Math.max(pos.x, Math.max(centre.x, Math.max(leftSide.x, rightSide.x)));
		sz = Math.min(pos.z, Math.min(centre.z, Math.min(leftSide.z, rightSide.z)));
		ez = Math.max(pos.z, Math.max(centre.z, Math.max(leftSide.z, rightSide.z)));

		return new TSection(cam.eye().x + sx, cam.eye().z + sz, cam.eye().x + ex, cam.eye().z + ez);
	}

	protected boolean insideDrawArea(float x, float z){
		for(int i = 0; i < tSectionsToDraw.size(); i++){
			if(tSectionsToDraw.get(i).isInside(x, z))
				return true;
		}
		return false;
	}

	/**
	 * Simple class to represent a section of the terrain. Used internally to represent an
	 * area of the terrain to be drawn. <br>
	 * 
	 * @author Peter Lager
	 *
	 */
	public class TSection{

		// Real world centre
		public float tx, tz;
		// Real world position
		public float sx, sz, ex, ez;
		// Relative to centre
		public float t_sx, t_sz, t_ex, t_ez;

		public int sEW, eEW, sNS, eNS;

		/**
		 * @param sx
		 * @param sz
		 * @param ex
		 * @param ez
		 */
		public TSection(float sx, float sz, float ex, float ez) {
			this.sx = sx;
			this.sz = sz;
			this.ex = ex;
			this.ez = ez;
		}

		public TSection() {
			tx = tz = 0;
			sx = sz = ex = ez = 0;
			sEW = eEW = sNS = eNS = 0;
		}

		/**
		 * Calculate part of terrain grid to be drawn.
		 * @param gsize
		 */
		public void calcDrawGrid(float gsize){
			t_sx = sx - tx;
			t_ex = ex - tx;
			t_sz = sz - tz;
			t_ez = ez - tz;
			sEW = (int)((sx - tx + halfTerrainSize)/gsize);
			sNS = (int)((sz - tz + halfTerrainSize)/gsize);
			eEW = gSteps - (int)((tx + halfTerrainSize - ex)/gsize);
			eNS = gSteps - (int)((tz + halfTerrainSize - ez)/gsize);
		}

		/**
		 * Calculate and return a TSection representing the intersection with
		 * another TSection. 
		 * @param ts
		 */
		public TSection intersection(TSection ts){
			TSection is = null;
			float isx, isz, iex, iez;

			if(ez < ts.sz || ts.ez < sz || ex < ts.sx || ts.ex < sx)
				return null;

			isx = Math.max(sx, ts.sx);
			iex = Math.min(ex, ts.ex);
			isz = Math.max(sz, ts.sz);
			iez = Math.min(ez, ts.ez);
			is = new TSection(isx,isz,iex,iez);
			return is;
		}

		/**
		 * Is the point x,z inside this terrain section
		 * @param x
		 * @param z
		 */
		public boolean isInside(float x, float z){
			if(x >= t_sx && x <= t_ex && z >= t_sz && z <= t_ez)
				return true;
			else
				return false;
		}

		public String toString(){
			String s =  "TSection ("+tx+" "+tz+")  ["+sx+", "+sz+"] to ["+ex+", "+ez+"]"; 
			s += "    relative position ["+t_sx+", "+t_sz+"] to ["+t_ex+", "+t_ez+"]"; 
			return s;
		}
	}
}
