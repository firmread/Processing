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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PVector;
import shapes3d.animation.ShapeMover;
import shapes3d.animation.ShapeRotator;
import shapes3d.utils.Rot;
import shapes3d.utils.RotOrder;
import shapes3d.utils.VectorUtil;

/**
 * Abstract class - the base for all 3D shape classes. <br>
 * 
 * Provides functionality to control position, rotation, color and draw 
 * mode of the various shapes available in this library.<br>
 * See drawMode(int mode) for details on the draw modes available. <br>
 * 
 * This class supports the selection of a 3D shape at a given x/y screen position. 
 * It uses a colour picking algorithm where each object is drawn with a unique colour
 * ignoring lighting and surface normals. The colour at the x/y screen position can then
 * be used to identify the shape. <br>
 * This would normally be done using an off-screen buffer in P3D mode. Unfortunately 
 * a bug in Processing (see bug report 1393) means that clipping is not working  
 * correctly in P3D mode so this class provides two methods and the user can select 
 * whichever performs best in their application. The methods are <br>
 * <b><pre>pickShapeB(PApplet papplet, int x, int y) </pre></b><br>
 * This uses an off-screen P3D buffer and works with Processing V1.0.5;
 * in V1.0.9 the clipping bug means that objects behind the view point are drawn
 * to the buffer giving a distorted image. This is particularly noticeable when using
 * the Terrain class in this library. If all the objects are 'in front' the camera view 
 * then this method works fine. <br>
 * <b><pre>pickShape(PApplet papplet, int x, int y) </pre></b><br>
 * This uses the actual applet screen as the buffer thus resulting in a flicker to black 
 * when this method is called. It is only worth using this method if the main applet screen  
 * is using OPENGL (using P3D mode means you have the flicker and all the problems mentioned
 * earlier). Since it uses OpenGL to perform the clipping it means it bypasses the bug in P3D. <br>
 * 
 * @author Peter Lager
 *
 */
public abstract class Shape3D implements PConstants {

	public static final int WIRE = 		0x00000011;
	public static final int SOLID = 	0x00000012;
	public static final int TEXTURE = 	0x00000014;
	public static final int DRAWALL = WIRE | SOLID | TEXTURE;
	
	// Constants for Cone shapes
	public static int BASE =	0x02000001;
	public static int CONE = 	0x02000002;
	public static int ALL = BASE | CONE;

	// Constants for BezTube, Tube & Helix shapes
	public static int E_CAP =	0x03000001;
	public static int S_CAP = 	0x03000002;
	public static int BOTH_CAP = E_CAP | S_CAP;


	protected final static int WHITE = -1;
	protected final static int BLACK = -16777216;
	protected final static int GREY = -4144960;

	protected PApplet app;
	
	protected static HashMap<Integer, Shape3D> pickMap = new HashMap<Integer, Shape3D>();
	
	// Counter used to provide unique colour value for each shape
	protected static int nextPickColor = 0xff000000;
	// Buffer used for shape picking
	protected static PGraphics3D pickBuffer;

	protected PVector pos = new PVector(0,0,0);	
	protected PVector rot = new PVector(0,0,0);
	// Up vector (starts along positive Y axis)
	protected PVector up = new PVector(0,1,0);
	// Centre of rotation (starts as [0,0,0] )
	protected PVector centreRot = new PVector(0,0,0);
	
	// Auto rotate and move components
	protected ShapeRotator autoRot = null;
	protected ShapeMover autoMove = null;
	
	protected float shapeScale = 1.0f;
	
	protected int fillColor = WHITE;
	protected int strokeColor = BLACK;
	protected float strokeWeight = 2.0f;
	protected int drawMode = SOLID;
	protected PImage skin;

	protected boolean useWire = false;
	protected boolean useSolid = true;
	protected boolean useTexture = false;
	
	protected boolean visible = true;
	
	// A list of child shapes
	protected LinkedList<Shape3D> children = new LinkedList<Shape3D>();
	protected Shape3D parent = null;

	protected int pickColor;
	protected boolean pickable = true;
	protected static boolean pickModeOn = false;
	
	protected boolean drawLock = false;
	
	/**
	 * User defined tag for this shape. <br>
	 * This can be any text the user likes or finds useful. When a shape is created
	 * this is initialised to the name of the class e.g. Toroid, Cone etc.
	 * 
	 */
	public String tag = "";
	
	/**
	 * User defined tag number. <br>
	 * This can be any number the user likes or finds useful.
	 */
	public int tagNo = 0;
	
	/**
	 * All child classes should call this constructor.
	 */
	public Shape3D(){
		pickColor = ++nextPickColor;
		pickMap.put(pickColor, this);
		tag = this.getClass().getSimpleName();
		fillColor = WHITE;
	}
	
	/**
	 * Set the shape's tag value <br>
	 * 
	 * @param tag
	 */
	@Deprecated
	public void tag(String tag){
		this.tag = tag;
	}
	
	/**
	 * Get the shape's tag value
	 */
	@Deprecated
	public String tag(){
		return tag;
	}
	
	/**
	 * Find out if the shape is 'pickable' using the pickShape(B) methods
	 */
	public boolean pickable(){
		return pickable;
	}

	/**
	 * Sets whether the shape can be picked with the pickShape(B) methods
	 * @param pickable
	 */
	public void pickable(boolean pickable){
		this.pickable = pickable;
	}
	
	// ###################################################
	// Abstract method to be implemented in child class
	// ###################################################
	public abstract void draw();
	
	// Empty method to allow for polymorphism
	protected void drawForPicker(PGraphics3D pickBuffer){}

	/**
	 * Identify the 3D shape which is under the 2D screen pixel 
	 * position given by [x,y] <br>
	 * Uses the main applet screen for the buffer so results in quick 
	 * flicker to black when called. Only really useful if using OPENGL for
	 * you main display. <br>
	 * 
	 * @param papplet the main class that extends PApplet
	 * @param x 
	 * @param y
	 * @return reference to the selected shape or null if not over a shape
	 */
	public static Shape3D pickShape(PApplet papplet, int x, int y){
		papplet.background(papplet.color(0));
		papplet.noLights();
		papplet.noStroke();
		pickModeOn = true;
		pickBuffer = (PGraphics3D) papplet.g;
		pickBuffer.background(WHITE);
		drawAll();
		int colorPicked = papplet.get(x, papplet.height - y);
		pickModeOn = false;
		return  pickMap.get(colorPicked);
	}
	
	/**
	 * Identify the 3D shape which is under the 2D screen pixel 
	 * position given by [x,y] <br>
	 * Uses an off-screen P3D buffer so will experience problems associated with
	 * bug in P3D clipping found in V1.0.9 of Processing <br>
	 * Sets the camera and projection matrices to match that of the PApplet <br>
	 * 
	 * @param papplet the main class that extends PApplet
	 * @param x 
	 * @param y
	 * @return reference to the selected shape or null if not over a shape
	 */
	public static Shape3D pickShapeB(PApplet papplet, int x, int y){
		if(pickBuffer == null || pickBuffer.width != papplet.width || pickBuffer.height != papplet.height){
			pickBuffer = (PGraphics3D) papplet.createGraphics(papplet.width, papplet.height, P3D);
		}
		pickBuffer.beginDraw();
		// Set the camera same as the drawing surface
		pickBuffer.camera.set(((PGraphics3D)papplet.g).camera);
		pickBuffer.projection.set(((PGraphics3D)papplet.g).projection);
		pickBuffer.noLights();
		pickBuffer.noStroke();
		pickBuffer.background(WHITE);
		
		// Draw to the buffer
		pickModeOn = true;
		drawAll();
		int c = pickBuffer.get(x,y);
		pickModeOn = false;
		pickBuffer.endDraw();

		return pickMap.get(c);
	}
		
	/**
	 * Draw all shapes that have been created. <br>
	 * 
	 */
	public static void drawAll(){
		Iterator<Entry<Integer, Shape3D> > iter =  pickMap.entrySet().iterator();
		Shape3D currShape;
		Map.Entry<Integer, Shape3D> entry;
		while(iter.hasNext()){
			entry = iter.next();
			currShape = entry.getValue();
			// If this is a child then let the parent draw it.
			if (currShape.parent == null)
				currShape.draw();
		}
	}
	
	protected boolean isFlagSet(int flags, int flag){
		return (flags & flag) == flag;
	}
	
	/**
	 * Change the world position and rotation. <br>
	 * If these are different from existing values then recalculate the shape.
	 * @param up
	 * @param centreOfRot
	 */
	public void shapeOrientation(PVector up, PVector centreOfRot){
		boolean changed = false;
		if(up != null && up.mag() > 0 && !VectorUtil.same(this.up, up)){
			this.up.set(up);
			changed = true;
		}
		if(centreOfRot != null && !VectorUtil.same(this.centreRot, centreOfRot)){
			this.centreRot.set(centreOfRot);
			changed = true;
		}
		if(changed)
			calcShape();
	}

	/**
	 * Place holder for polymorphism.
	 */
	protected abstract void calcShape();
	
	/**
	 * Place holder for polymorphism.
	 */
	protected void calcNormals(){}
	
	/**
	 * @return the parent
	 */
	public Shape3D getParent() {
		return parent;
	}

	/**
	 * Sets the parent shape for this shape.
	 * @param shape the parent shape
	 */
	protected void setParent(Shape3D shape){
		parent = shape;
	}
		
	/**
	 * Adds a shape as a child shape. If the shape already has a parent
	 * it is removed from that parent. <br>
	 * It is the reverse of removeShape.
	 * 
	 * @param shape the shape to add
	 */
	public void addShape(Shape3D shape){
		// Remove from existing parent
		if(shape.getParent() != null){
			shape.getParent().removeShape(shape);
		}
		children.add(shape);
		shape.setParent(this);
		System.out.println("Adding "+shape.tag+" to "+tag);
	}

	/**
	 * Remove the shape from its parent. <br>
	 * It is the reverse of addShape.
	 * 
	 * @param shape the shape to remove.
	 */
	public void removeShape(Shape3D shape){
		children.remove(shape);
		shape.setParent(null);
		System.out.println("Removing "+shape.tag+" from "+tag);
	}
	
	/**
	 * Attach a shape maintaining the relative positions <br>
	 * Some work still needs doing to maintain position.
	 * @param shape
	 */
	public void attachShape(Shape3D shape){
		if(shape.getParent() != null){
			shape.getParent().detachShape(shape);
		}
		// Maintain current position and rotation
		PVector diff = PVector.sub(shape.pos, pos);
		Rot r = new Rot(diff, new PVector(0,0,1));
		r.applyTo(diff);
		shape.pos = diff;
		shape.pos.sub(pos);
		children.add(shape);
		shape.setParent(this);		
	}

	/**
	 * Detach a shape but attempt to preserve its global position.
	 * @param shape
	 */
	public void detachShape(Shape3D shape){
		PVector accPos = new PVector();
		calcAbsPos(this, shape.getPosVec(),accPos);
		shape.pos = accPos;
		children.remove(shape);
		shape.setParent(null);		
	}
	
	protected static void calcAbsPos(Shape3D s, PVector p, PVector acc){
		if(s.parent != null){
			calcAbsPos(s.parent, s.pos, acc);
		}
		// Reached the top of the tree
		Rot r = new Rot(RotOrder.XYZ, s.rot.x, s.rot.y, s.rot.z);
		acc.add(r.applyToNew(p));
		acc.add(s.pos);
	}
	
	/**
	 * Sets the draw mode for the shape. <br>
	 * The three constants Shape3D.SOILD, Shape3D.WIRE and Shape3D.TEXTURE are used 
	 * to determine how the shape is drawn. These can be or-ed together to get different
	 * drawing combinations e.g. Shape3D.SOLID | Shape3D.WIRE would be a coloured shape
	 * with edges. If the parameter is Shape3D.TEXTURE then and a texture has not
	 * been set then it will use SOLID.
	 * @param mode
	 */
	public void drawMode(int mode){
		mode &= DRAWALL;
		boolean validMode = ((mode & WIRE) == WIRE);
		validMode |= ((mode & SOLID) == SOLID);
		validMode |= ((mode & TEXTURE) == TEXTURE && skin != null);
		if(!validMode)
			mode = SOLID;
		drawMode = mode;
	}
	
	/**
	 * This method is overridden in the classes that have multiple parts e.g. Cone, 
	 * Tube and Helix
	 * @param mode
	 * @param parts
	 */
	public void drawMode(int mode, int parts){}
	
	/**
	 * Get the draw mode
	 * @return Shape3D.SOILD, Shape3D.WIRE or Shape3D.TEXTURE
	 */
	public int drawMode(){
		return drawMode;
	}
	
	/**
	 * Override this in classes that have multiple textures or
	 * parts.
	 */
	protected void drawWhat(){
		useSolid = ((drawMode & SOLID) == SOLID);
		useWire = ((drawMode & WIRE) == WIRE);
		useTexture = ((drawMode & TEXTURE) == TEXTURE && skin != null);
	}
	
	/**
	 * Will adjust the shape's position to ensure that it is over the terrain
	 * at a given height above it.
	 * @param t the terrain we want to use
	 * @param tMode Terrain.CLAMP or Terrain.WRAP
	 * @param heightAbove the height of the eye above the terrain
	 */
	public void adjustToTerrain(Terrain t, int tMode, float heightAbove){
		t.adjustPosition(pos, tMode);
		pos.y = t.getHeight(pos.x, pos.z) - heightAbove;
	}
	
	/**
	 * Get the current position of the shape as a PVector. <br>
	 * @return the current position
	 */
	public PVector getPosVec(){
		return getPosVec(null);
	}
	
	/**
	 * Get the current position of the shape as a PVector. <br>
	 * If target is not null it is used to store the current position.
	 * @param target
	 * @return the current position
	 */
	public PVector getPosVec(PVector target) {
		if(target == null)
			target = new PVector();
		target.set(pos);
		return target;
	}

	/**
	 * Get the current position of the shape as a float array.
	 * @return the current position as an array
	 */
	public float[] getPosArray(){
		return getPosArray(null);
	}
	
	/**
	 * Get the current position of the shape as a float array.
	 * If target is not null and has at least 3 elements then
	 * it is used to store the current position.
	 * @param target
	 * @return the current position as an array
	 */
	public float[] getPosArray(float[] target){
		if(target == null || target.length < 3)
			target = new float[3];
		target[0] = pos.x;
		target[1] = pos.y;
		target[2] = pos.z;
		return target;
	}

	/**
	 * Get the x position
	 */
	public float x(){
		return pos.x;
	}
	
	/**
	 * Get the y position
	 */
	public float y(){
		return pos.y;
	}
	
	/**
	 * Get the z position
	 */
	public float z(){
		return pos.z;
	}
	
	/**
	 * Sets the shape's current position.
	 * @param pos
	 */
	public void moveTo(PVector pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
		this.pos.z = pos.z;
	}

	/**
	 * Sets the shape's current position.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void moveTo(float x, float y, float z){
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}
	
	/**
	 * Sets the shape's current position. <br>
	 * Position is unchanged if the parameter array has less than 
	 * 3 elements.
	 * @param xyz
	 */
	public void moveTo(float[] xyz){
		if(xyz.length >= 3){
			pos.x = xyz[0];
			pos.y = xyz[1];
			pos.z = xyz[2];
		}
	}
	
	/**
	 * Move from the current position to another position in the stated
	 * time period.
	 * @param npos target destination.
	 * @param time time to complete move (seconds)
	 * @param delay time before the action starts
	 */
	public void moveTo(PVector npos, float time, float delay) {
		if(autoMove != null)
			autoMove.kill();
		autoMove = new ShapeMover(app, this, pos, npos, time, delay );
	}

	/**
	 * Move from the current position to another position in the stated
	 * time period.
	 * @param x
	 * @param y
	 * @param z
	 * @param time to complete move (seconds)
	 * @param delay time before the action starts
	 */
	public void moveTo(float x, float y, float z, float time, float delay){
		moveTo(new PVector(x,y,z), time, delay);
	}
	
	/**
	 * Move from the current position to another position in the stated
	 * time period.
	 * @param xyz
	 * @param time to complete move (seconds)
	 * @param delay time before the action starts
	 */
	public void moveTo(float[] xyz, float time, float delay){
		if(xyz.length >= 3)
			moveTo(new PVector(xyz[0], xyz[1], xyz[2]), time, delay);
	}
	
	/**
	 * Set the x position
	 * @param x
	 */
	public void x(float x){
		pos.x = x;
	}
	
	/**
	 * Sets the y position
	 * @param y
	 */
	public void y(float y){
		pos.y = y;
	}
	
	/**
	 * sets the z position
	 * @param z
	 */
	public void z(float z){
		pos.z = z;
	}
	
	/**
	 * Move relative to current position.
	 * @param move
	 */
	public void moveBy(PVector move){
		pos.add(move);
	}
	
	/**
	 * Move relative to current position.
	 * Ignored if the array has less than 3 elements.
	 * @param move
	 */
	public void moveBy(float[] move){
		if(move.length>= 3){
			pos.x += move[0];
			pos.y += move[1];
			pos.z += move[2];
		}
	}
	
	/**
	 * Move relative to current position.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void moveBy(float x, float y, float z){
		pos.x += x;
		pos.y += y;
		pos.z += z;
	}
	
	/**
	 * Move from the current position a new relative position in the stated
	 * time period.
	 * @param move target position relative to current position.
	 * @param time time to complete move (seconds)
	 */
	public void moveBy(PVector move, float time, float delay){
		if(autoMove != null)
			autoMove.kill();
		autoMove = new ShapeMover(app, this, pos, PVector.add(pos, move), time, delay);
	}
	
	/**
	 * Move from the current position a new relative position in the stated
	 * time period. 
	 * @param move
	 * @param time
	 */
	public void moveBy(float[] move, float time, float delay){
		if(move.length>= 3)
			moveBy(new PVector(move[0], move[1], move[2]), time, delay);
	}
	
	/**
	 * Move from the current position a new relative position in the stated
	 * time period.
	 * @param x
	 * @param y
	 * @param z
	 * @param time
	 * @param delay
	 */
	public void moveBy(float x, float y, float z, float time, float delay){
		moveBy(new PVector(x,y,z), time, delay);
	}
	
	/**
	 * Get the current rotations as a PVector
	 */
	public PVector getRotVec() {
		return new PVector(rot.x, rot.y, rot.z);
	}

	/**
	 * Get the current rotations as a PVector
	 * 
	 * @param target
	 */
	public PVector getRotVec(PVector target){
		if(target == null)
			target = new PVector();
		target.set(rot);
		return target;
	}
	
	/**
	 * Get the current rotations as an array.
	 */
	public float[] getRotArray(){
		return getRotArray(null);
	}
	
	/**
	 * Get the current rotations as an array. <br>
	 * If target is not null it is used to store the current position.
	 * @param target
	 */
	public float[] getRotArray(float[] target){
		if(target == null || target.length < 3)
			target = new float[3];
		target[0] = rot.x;
		target[1] = rot.y;
		target[2] = rot.z;
		return target;
	}
	
	/**
	 * Set the current rotations.
	 * @param angles an array of angle to set the rotation
	 */
	public void rotateTo(PVector angles) {
		rot.set(angles);
	}

	/**
	 * Set the current rotations.
	 * Ignored if the array has less than 3 elements.
	 * @param angles
	 */
	public void rotateTo(float[] angles){
		if(angles.length >= 3){
			rot.x = angles[0];
			rot.y = angles[1];
			rot.z = angles[2];
		}
	}
	
	/**
	 * Set the current rotations.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotateTo(float x, float y, float z) {
		rot.x = x;
		rot.y = y;
		rot.z = z;
	}
	
	/**
	 * Rotate to given angles over the stated time period 
	 * @param angles destination angle
	 * @param time to complete rotation (seconds)
	 */
	public void rotateTo(PVector angles, float time, float delay) {
		if(autoRot != null)
			autoRot.kill();
		autoRot = new ShapeRotator(app, this, rot, angles, time, delay);
	}

	/**
	 * Rotate to given angles over the stated time period 
	 * @param angles destination angle
	 * @param time to complete rotation (seconds)
	 */
	public void rotateTo(float[] angles, float time, float delay){
		if(angles.length >= 3){
			rotateTo(new PVector(angles[0], angles[1], angles[2]), time, delay);
		}
	}

	/**
	 * Rotate to given angles over the stated time period 
	 * @param x
	 * @param y
	 * @param z
	 * @param time to complete rotations (seconds)
	 */
	public void rotateTo(float x, float y, float z, float time, float delay) {
		rotateTo(new PVector(x,y,z), time, delay);
	}

	/**
	 * Set the rotation about the x axis
	 * @param x
	 */
	public void rotateToX(float x){
		rot.x = x;
	}
	
	/**
	 * Set the rotation about the y axis
	 * @param y
	 */
	public void rotateToY(float y){
		rot.y = y;
	}
	
	/**
	 * Set the rotation about the z axis
	 * @param z
	 */
	public void rotateToZ(float z){
		rot.z = z;
	}
	
	/**
	 * Rotate shape by relative amount. <br>
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotateBy(float x, float y, float z){
		rot.x += x;
		rot.y += y;
		rot.z += z;
	}
	
	/**
	 * Rotate shape by relative amount. <br>
	 * Ignored if the array has less than 3 elements. 
	 * @param angle
	 */
	public void rotateBy(float[] angle){
		if(angle.length >= 3){
			rot.x += angle[0];
			rot.y += angle[1];
			rot.z += angle[2];
		}
	}
	
	/**
	 * Rotate shape by relative amount. <br>
	 * @param angles
	 */
	public void rotateBy(PVector angles){
		rot.add(angles);
	}
	
	/**
	 * Rotate from the current orientation to a new relative orientation in
	 * the stated time period. 
	 * @param angles
	 * @param time time to complete the rotation (seconds)
	 * @param delay time before the action starts
	 */
	public void rotateBy(PVector angles, float time, float delay) {
		if(autoRot != null)
			autoRot.kill();
		autoRot = new ShapeRotator(app, this, rot, PVector.add(rot, angles), time, delay);
	}

	/**
	 * Rotate from the current orientation to a new relative orientation in
	 * the stated time period. 
	 * @param angles
	 * @param time time to complete the rotation (seconds)
	 * @param delay time before the action starts
	 */
	public void rotateBy(float[] angles, float time, float delay){
		if(angles.length >= 3)
			rotateBy(new PVector(angles[0], angles[1], angles[2]), time, delay);
	}

	/**
	 * Rotate from the current orientation to a new relative orientation in
	 * the stated time period. 
	 * @param x
	 * @param y
	 * @param z
	 * @param time time to complete the rotation (seconds)
	 * @param delay time before the action starts
	 */
	public void rotateBy(float x, float y, float z, float time, float delay) {
		rotateBy(new PVector(x,y,z), time, delay);
	}

	/**
	 * Stop the auto shape rotate.
	 */
	public void stopRotator(){
		if(autoRot != null){
			autoRot.kill();
			autoRot = null;
		}		
	}
	
	/**
	 * Stop the auto shape move.
	 */
	public void stopMover(){
		if(autoMove != null){
			autoMove.kill();
			autoMove = null;
		}
	}
	
	/**
	 * Sets the scale for drawing the shape
	 * @param scale
	 */
	public void scale(float scale){
		if(scale > 0.0f)
			shapeScale = scale;
	}
	
	/**
	 * Get the scale used for drawing.
	 */
	public float scale(){
		return shapeScale;
	}
	
	/**
	 * Sets the fill colour
	 * @param col fill color
	 */
	public void fill(int col){
		fillColor = col;
	}
	
	/**
	 * Get the fill colour
	 */
	public int fill(){
		return fillColor;
	}
	
	/**
	 * Sets the stroke colour
	 * @param col stroke color
	 */
	public void stroke(int col){
		strokeColor = col;
	}
	
	/** 
	 * Get the stroke color
	 */
	public int stroke(){
		return strokeColor;
	}
	
	/**
	 * Set the pick colour for this shape. <br>
	 * Included for those rare occasions when you might want to set
	 * your own pick colour. It enforces the rule that the pick colour
	 * is unique for each shape, so if you specify an existing colour
	 * no changes are made and the method returns false. It returns
	 * true if the pick colour was changed successfully.
	 * @param pcol the new pick color
	 */
	public boolean pickColor(int pcol){
		if(!pickMap.containsKey(pcol)){
			pickMap.remove(pickColor);
			pickColor = pcol;
			pickMap.put(pickColor, this);
			return true;
		}
		System.out.println("The pick color has to be unique - pick color has not been changed");
		return false;
	}
	
	/**
	 * Get the pick colour for this shape
	 * @return the pick color value
	 */
	public int pickColor(){
		return pickColor;
	}
	
	/**
	 * Sets the thickness of lines used in wireframe
	 * @param weight
	 */
	public void strokeWeight(float weight){
		strokeWeight = weight;
	}
	
	/**
	 * Gets the thickness of lines used in wireframe
	 */
	public float strokeWeight(){
		return strokeWeight;
	}
	
	/**
	 * Is the shape visible.
	 * @return true if visible
	 */
	public boolean visible() {
		return visible;
	}
	
	/**
	 * Sets the shapes visibility.
	 * @param visible the visible to set
	 */
	public void visible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Gets the a reference to the pick buffer.
	 */
	public static PGraphics3D getPickBuffer(){
		return pickBuffer;
	}
	
	/**
	 * Does nothing overridden in child classes.
	 * @param fname
	 */
	public void setTexture(String fname){ }
	
	/**
	 * Does nothing overridden in child classes.
	 * @param fname
	 * @param nRptEW
	 * @param nRptNS
	 */
	public void setTexture(String fname, int nRptEW, int nRptNS){ }
	
	/**
	 * If you have finished with a shape call this method to remove
	 * it from the list of shapes to draw / pick. <br>
	 * This enables garbage collection to release memory allocated to
	 * this shape.
	 */
	public void finishedWith(){
		pickMap.remove(pickColor);
	}
	
	public String toString(){
		return tag;
	}
}
