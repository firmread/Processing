/**
Earth and Moon
Demonstrating the Shapes3D library.
created by Peter Lager
*/

import shapes3d.utils.*;
import shapes3d.*;

Ellipsoid earth, moon, stars;

void setup(){
  size(420,380, P3D);	

  // Create the earth
  earth = new Ellipsoid(this, 20 ,30);
  earth.setTexture("earth.jpg");
  earth.setRadius(90);
  earth.moveTo(new PVector(0,0,0));
  earth.drawMode(Shape3D.TEXTURE);
  
  // Create the moon
  moon = new Ellipsoid(this,10,15);
  moon.setTexture("moon.jpg");
  moon.setRadius(20);
  moon.moveTo(0,0,220);
  moon.drawMode(Shape3D.TEXTURE);

  // Create the star background
  stars = new Ellipsoid(this,10,10);
  stars.setTexture("stars01.jpg",5,5);
  stars.setRadius(500);
  stars.drawMode(Shape3D.TEXTURE);

  // Add the moon to the earth this makes 
  // its position relative to the earth's
  earth.addShape(moon);

  frameRate(30);
}

void draw(){
  // Change the rotations before drawing
  earth.rotateBy(0, radians(0.6), 0);
  moon.rotateBy(0, radians(1.0), 0);
  stars.rotateBy(0, 0, radians(0.03));


  background(40);
  pushMatrix();
  camera(0, -190, 350, 0, 0, 0, 0, 1, 0);
  ambientLight(80,80,80);
  directionalLight(255, 255, 255, -150, 150, -80);

  // Draw the earth (will cause all added shapes
  // to be drawn i.e. the moon)
  earth.draw();

  // Reset the lights
  noLights();
  ambientLight(180,180,180);
  stars.draw();
  popMatrix();
}
