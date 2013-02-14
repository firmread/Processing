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

import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PVector;
import shapes3d.utils.MeshSection;
import shapes3d.utils.Textures;

/**
 * Support class providing draw and texture methods for those shapes using a grid mesh.<br>
 * ie BezierShape, Ellipsoid, Helix, Toroid and Tube.
 * 
 * @author Peter Lager
 *
 */
public abstract class Mesh2DCore extends Shape3D {

	protected PVector[][] coord;
	protected PVector[][] norm;

	protected float deltaU, deltaV;
	protected int ewPieces, nsPieces;
	protected int ewSteps, nsSteps;

	protected float nearlyOne;

	protected ArrayList<MeshSection> sections;

	protected MeshSection fullShape;

	/**
	 * Default constructor
	 */
	public Mesh2DCore(){
		super();
	}

	/**
	 * Will calculate the normals for any 2D mesh. <br>
	 * This is a heavy mathematical process so should be overridden in 
	 * child classes if there is a suitable alternative method.
	 */
	protected void calcNormals() {
		PVector c , np, nt;
		int ns, ew;
		for(ns = 0; ns < nsSteps; ns++) {
			for(ew = 0; ew < ewSteps /* - 1*/; ew++) {
				c = coord[ew][ns];
				if(ns == nsSteps - 1){
					np = PVector.sub(c, coord[ew][ns-1]);
					np.add(c);
				}
				else
					np = coord[ew][ns+1];

				if(ew == ewSteps-1)
					nt = coord[1][ns];
				else
					nt = coord[ew+1][ns];

				norm[ew][ns] = PVector.cross(PVector.sub(nt, c, null), 
						PVector.sub(np, c, null), 
						null);

				norm[ew][ns].normalize();
			}
		}	
	}

	/**
	 * Returns a MeshSection object (initially set for full shape) 
	 * which can be modified and then added as a drawing section. 
	 * @return a full MeshSection object for modification
	 */
	public MeshSection getDrawSection(){
		return new MeshSection(ewSteps, nsSteps);
	}

	/**
	 * Add a drawing section to the list of sections to draw.
	 * This cancels full shape drawing!
	 * @param section
	 */
	public void addDrawSection(MeshSection section){
		if(sections == null)
			sections = new ArrayList<MeshSection>();
		sections.add(section);
	}

	/**
	 * Remove a draw section from the list, if it is the last one
	 * then switch to full shape draw.
	 * @param section
	 */
	public void removeDrawSection(MeshSection section){
		if(sections != null){
			sections.remove(section);
			if(sections.size() == 0)
				sections = null;
		}
	}

	/**
	 * Remove all draw sections and go back to full shape drawing.
	 */
	public void setDrawFullShape(){
		if(sections != null)
			sections.clear();
		sections = null;
	}

	/**
	 * Set the texture to be used for this Shape (no tiling) <br>
	 * If the image is loaded successfully then the drawMode is changed to TEXTURE. 
	 * @param fname
	 */
	public void setTexture(String fname){
		setTexture(fname, 1, 1);
	}

	/**
	 * Set the image to be used for this Shape (no tiling) <br>
	 * @param img if not null then the drawMode is changed to TEXTURE.
	 */
	public void setTexture(PImage img){
		setTexture(img, 1, 1);
	}

	/**
	 * Set the texture to be used for this Shape and the number of
	 * texture repeats (tiling) <br>
	 * If the image is loaded successfully then the drawMode is changed to TEXTURE. 
	 * 
	 * <pre><b>
	 *                          nRptEW              nPrtNS </b>
	 * BezierShape              around Y axis       length
	 * Tube                     circumference       length
	 * Ellipsoid                east-west           north-south
	 * Helix                    along length        round tube
	 * Toroid                   along length        round tube
	 * </pre><br>
	 * 
	 * @param fname
	 * @param ewRepeats
	 * @param nsRepeats
	 */
	public void setTexture(String fname, int ewRepeats, int nsRepeats){
		PImage img = Textures.loadImage(app, fname);
		setTexture(img, ewRepeats, nsRepeats);
	}

	/**
	 * Set the image to be used for this Shape and the number of
	 * texture repeats (tiling) <br>
	 * 
	 * <pre><b>
	 *                          nRptEW              nPrtNS </b>
	 * BezierShape              around Y axis       length
	 * Tube                     circumference       length
	 * Ellipsoid                east-west           north-south
	 * Helix                    along length        round tube
	 * Toroid                   along length        round tube
	 * </pre><br>
	 * 
	 * @param img if not null then the drawMode is changed to TEXTURE.
	 * @param ewRepeats
	 * @param nsRepeats
	 */
	public void setTexture(PImage img, int ewRepeats, int nsRepeats){
		ewRepeats = Math.max(1, ewRepeats);
		nsRepeats = Math.max(1, nsRepeats);

		while(ewPieces % ewRepeats != 0){
			ewRepeats--;
		}
		deltaU = ewRepeats / (float)(ewSteps - 1);

		while(nsPieces % nsRepeats != 0){
			nsRepeats--;
		}
		deltaV = nsRepeats / (float)(nsSteps - 1);

		nearlyOne = 1.0f - Math.min(deltaU, deltaV)/10.0f;
		skin = img;
	}

	/**
	 * If visible draw the shape based on drawMode.
	 */
	@Override
	public void draw(){
		if(visible){
			drawWhat();
			if(pickModeOn){
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
			else {
				app.pushStyle();
				app.pushMatrix();

				app.translate(pos.x, pos.y, pos.z);
				app.rotateX(rot.x);
				app.rotateY(rot.y);
				app.rotateZ(rot.z);
				app.scale(shapeScale);

				if(useTexture)
					drawWithTexture();
				else
					drawWithoutTexture();

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


	protected void drawForPicker(PGraphics3D pickbuffer){
		if(drawMode != WIRE){
			if(sections != null)
				for(int i = 0; i < sections.size(); i++)
					drawForPicker(pickBuffer, sections.get(i));
			else
				drawForPicker(pickBuffer, fullShape);
		}
	}

	protected void drawForPicker(PGraphics3D pickBuffer, MeshSection section){
		pickBuffer.pushMatrix();
		pickBuffer.translate(pos.x, pos.y, pos.z);
		pickBuffer.rotateX(rot.x);
		pickBuffer.rotateY(rot.y);
		pickBuffer.rotateZ(rot.z);
		pickBuffer.scale(shapeScale);
		pickBuffer.noStroke();

		PVector c;
		for(int p = section.sNS; p < section.eNS - 1; p++){
			pickBuffer.beginShape(TRIANGLE_STRIP);
			pickBuffer.fill(pickColor);
			for(int t = section.sEW; t < section.eEW; t++) {
				c = coord[t][p];
				pickBuffer.vertex(c.x, c.y, c.z);					
				c = coord[t][p+1];
				pickBuffer.vertex(c.x, c.y, c.z);					
			}
			pickBuffer.endShape();
		}

		pickBuffer.popMatrix();
	}

	protected void drawWithTexture(){
		if(useSolid)
			app.fill(fillColor);
		else
			app.noFill();
		if(useWire){
			app.stroke(strokeColor);
			app.strokeWeight(strokeWeight);
		}
		else {
			app.noStroke();
		}
		if(sections != null){
			for(int i = 0; i < sections.size(); i++)
				drawWithTexture(sections.get(i));
		}
		else
			drawWithTexture(fullShape);

		//drawNormals();
	}

	protected void drawWithTexture(MeshSection section){
		float currU = 0, currV = 0;
		float startU = 0, startV = 0;
		PVector c, n;

		startU = section.sEW * deltaU;
		while(startU > 1.0f) startU -= 1.0f;
		currU = startU;

		startV = section.sNS * deltaV;
		while(startV > 1) startV -= 1.0f;
		if(startV > nearlyOne) startV = 0;
		currV = startV;

		app.textureMode(NORMAL);

		for(int p = section.sNS; p < section.eNS - 1; p++){
			currU = startU;
			app.beginShape(QUAD_STRIP);
			app.texture(skin);
			for(int t = section.sEW; t < section.eEW; t++){
				if(currU > nearlyOne){
					c = coord[t][p];
					n = norm[t][p];
					app.normal(n.x, n.y, n.z);
					app.vertex(c.x, c.y, c.z, currU, currV);
					c = coord[t][p+1];
					n = norm[t][p+1];
					app.normal(n.x, n.y, n.z);
					app.vertex(c.x, c.y, c.z, currU, currV + deltaV);
					app.endShape();
					currU = 0;
					app.beginShape(QUAD_STRIP);
					if(useSolid)
						app.fill(fillColor);
					if(useWire){
						app.stroke(strokeColor);
						app.strokeWeight(strokeWeight);
					}
					app.texture(skin);
				}
				c = coord[t][p];
				n = norm[t][p];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z, currU, currV);
				c = coord[t][p+1];
				n = norm[t][p+1];
				app.normal(n.x, n.y, n.z);
				app.vertex(c.x, c.y, c.z, currU, currV + deltaV);
				currU += deltaU;
			} // t for end
			app.endShape();
			currV += deltaV;
			if(currV > nearlyOne) currV = 0; 
		} // p for end
	} // ewTile for end


	/**
	 * Used in SOLID mode to apply a single color over the surface
	 */
	protected void drawWithoutTexture(){
		if(useWire){
			app.stroke(strokeColor);
			app.strokeWeight(strokeWeight);
		}
		else {
			app.noStroke();
		}
		if(useSolid)
			app.fill(fillColor);
		else
			app.noFill();
		if(sections != null)
			for(int i = 0; i < sections.size(); i++)
				drawWithoutTexture(sections.get(i));
		else
			drawWithoutTexture(fullShape);		

		//drawNormals();
	}

	/**
	 * Use this if drawMode is set to SOLID
	 */
	protected void drawWithoutTexture(MeshSection section){
		PVector c, n;

		for(int p = section.sNS; p < section.eNS - 1; p++) {
			app.beginShape(QUAD_STRIP);
			for(int t = section.sEW; t < section.eEW; t++) {
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
	}

	/**
	 * Used for testing only
	 */
	protected void drawNormals(){
		PVector n, c;
		app.stroke(255,0,0);
		for(int p = 0; p < nsSteps; p++) {
			for(int t = 0; t < ewSteps; t++) {
				c = coord[t][p];
				n = PVector.mult(norm[t][p],20);
				app.line(c.x,c.y,c.z,c.x+n.x,c.y+n.y,c.z+n.z);
			}
		}	
		app.noStroke();
	}


}
