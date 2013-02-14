/**
 * Simple program that displays some the shapes 
 * and features available in the Shapes3D library.
 *
 * Also requires PeasyCam library.
 * 
 * created by Peter Lager
 */


import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;

import peasy.org.apache.commons.math.*;
import peasy.*;
import peasy.org.apache.commons.math.geometry.*;

PeasyCam pCamera;
PMatrix3D baseMat;

Ellipsoid ellipsoid;
Toroid toroid;
Helix helix;
Cone cone;
Tube tube;
Box box;
Bezier2D bn2;
Bezier3D bn3;
BezShape bezierShape;
BezTube bezierTube;

int shapeNo = 5;

void setup() {
  size(400, 400, P3D);
  // Use this to have fixed lighting
  baseMat = g.getMatrix(baseMat);

  pCamera = new PeasyCam(this, 300);
  pCamera.lookAt(0, 0, 0, 110);

  makeBezier2();
  bezierShape = new BezShape(this, bn2, 40, 20);
  bezierShape.setTexture("tartan.jpg");
  bezierShape.stroke(color(255,255,0));
  bezierShape.strokeWeight(2.3);
  bezierShape.drawMode(Shape3D.TEXTURE);

  makeBezier3();
  //  bezierTube = new BezTube(this, bn3, 10, 10, 6);
  bezierTube.drawMode(Shape3D.WIRE);
  bezierTube.strokeWeight(1.0f);
  bezierTube.stroke(color(255));

  bezierTube.setTexture("stripes2.jpg", 2, 25);
  bezierTube.drawMode(Shape3D.TEXTURE);
  bezierTube.visible(true, Tube.BOTH_CAP);
  bezierTube.fill(color(200,0,0), BezTube.S_CAP);
  bezierTube.fill(color(200,200,0), BezTube.E_CAP);

  box = new Box(this);
  String[] faces = new String[] {
    "pd_9.jpg", "pd_a.jpg", "pd_10.jpg",
    "pd_k.jpg", "pd_j.jpg", "pd_q.jpg"            };
  box.setTextures(faces);
  box.fill(color(255));
  box.stroke(color(190));
  box.strokeWeight(1.2);
  box.setSize(100, 100, 100);
  box.rotateTo(radians(45), radians(45), radians(45));
  box.drawMode(Shape3D.TEXTURE | Shape3D.WIRE);

  cone = new Cone(this, 40);
  cone.fill(color(0,0,96), Cone.BASE);
  cone.stroke(color(160), Cone.BASE);
  cone.strokeWeight(2.5, Cone.BASE);
  cone.setSize(140,140,70);
  cone.moveTo(new PVector(0,-35,0));
  cone.rotateToX(radians(-30));
  cone.setTexture("stars03.jpg", Cone.CONE);
  cone.drawMode(Shape3D.TEXTURE, Cone.CONE);
  cone.drawMode(Shape3D.SOLID | Shape3D.WIRE , Cone.BASE);

  tube = new Tube(this, 8, 60);
  tube.setSize(100,70,40,40,240);
  tube.setTexture("sampler01.jpg");
  tube.setTexture("coinhead.jpg", Tube.S_CAP);
  tube.setTexture("cointail.jpg", Tube.E_CAP);
  tube.drawMode(Shape3D.TEXTURE);
  tube.drawMode(Shape3D.TEXTURE, Tube.BOTH_CAP);

  ellipsoid = new Ellipsoid(this, 16 ,24);
  ellipsoid.setTexture("globe.jpg");
  ellipsoid.setRadius(100, 80, 100);
  ellipsoid.drawMode(Shape3D.WIRE | Shape3D.TEXTURE);
  ellipsoid.stroke(color(160,160,0));

  helix = new Helix(this, 20,200);
  helix.setRadius(5, 10, 80);
  helix.setHelix(2.5f, 60);
  helix.moveTo(new PVector(0,0,0));
  helix.setTexture("stripes.jpg",20,1);
  helix.drawMode(Shape3D.TEXTURE);
  helix.fill(color(20,255,20), Helix.S_CAP);
  helix.fill(color(255,20,20), Helix.E_CAP);
  helix.drawMode(Shape3D.SOLID, Shape3D.BOTH_CAP);

  toroid = new Toroid(this, 20, 30);
  toroid.setRadius(50, 40, 80);
  toroid.rotateToX(radians(-30));
  toroid.moveTo(new PVector(0,0,0));
  toroid.fill(color(64,64,200));
  toroid.stroke(color(255));
  toroid.strokeWeight(1.8);
  toroid.drawMode(Shape3D.SOLID | Shape3D.WIRE);

  camera(0, 0, 300, 0, 0, 0, 0, 1, 0);
}

void draw() {
  background(20);
  pushMatrix();
  g.setMatrix(baseMat);
  // stage lighting
  directionalLight(200, 200, 200, 100, 150, -100);
  ambientLight(160, 160, 160);
  popMatrix();

  switch(shapeNo){
  case 0:
    bezierShape.draw();
    break;
  case 1:
    box.draw();
    break;
  case 2:
    cone.draw();
    break;
  case 3:
    tube.draw();
    break;
  case 4:
    ellipsoid.draw();
    break;
  case 5:
    helix.draw();
    break;
  case 6:
    toroid.draw();
    break;
  case 7:
    bezierTube.draw();
    break;
  }
} 

void keyPressed(){
  shapeNo = (shapeNo + 1)%8;
}

void makeBezier2(){
  int degree = 4;
  PVector[] v;
  float[][] d = new float[degree][2];		
  d[0][0] = 30;		
  d[0][1] = -80;
  d[1][0] = 120;		
  d[1][1] = -140;
  d[2][0] = 0;		
  d[2][1] = 20;
  d[3][0] = 80;		
  d[3][1] = 80;

  v = new PVector[degree];
  for(int i = 0; i < degree; i++){
    v[i] = new PVector(d[i][0], d[i][1]);
  }
  bn2 = new Bezier2D(v, v.length);
}

public void makeBezier3(){

  float[] prf = new float[] {
    88f, 52f, 30.1f, 21.9f, 14.3f, 13f, 12.8f, 12.8f, 
    12.8f, 12.5f, 12.1f, 12.1f, 12.2f, 12.1f, 11.9f, 11.7f, 
    11.6f, 11.6f, 11.7f, 11.8f, 11.9f, 12f, 11.8f, 11.7f, 
    11.4f, 11.3f, 11.7f, 12.1f, 12.5f, 12f, 12.5f, 19.5f
  };

  BezTubeRadius r = new BezTubeRadius(prf);

  PVector[] v = new PVector[] {
    new PVector(113f, -31f, -24f), 
    new PVector(82.2f, -31.5f, -21.2f), 
    new PVector(51.5f, -32f, -18.5f), 
    new PVector(-6.5f, -33f, -15.7f), 
    new PVector(-62f, -50f, -13f), 
    new PVector(-81.5f, -44f, -10.2f), 
    new PVector(-91f, -24f, -7.5f), 
    new PVector(-95f, 5f, -4.7f), 
    new PVector(-86f, 44f, -1.9f), 
    new PVector(-51f, 52f, 0.8f), 
    new PVector(1.5f, 65f, 3.5f), 
    new PVector(68f, 59f, 6.3f), 
    new PVector(93f, 9f, 9f), 
    new PVector(87.5f, -48f, 11.8f), 
    new PVector(43f, -44f, 14.5f), 
    new PVector(-108f, -35f, 17.3f)
    };

    bezierTube = new BezTube(this, new Bezier3D(v,v.length),r,100,16);
}
