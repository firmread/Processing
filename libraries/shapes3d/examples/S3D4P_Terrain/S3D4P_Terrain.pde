import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;

import processing.opengl.*;

Terrain terrain;
Box skybox;
Cone[] droids;
Tube tube;
Ellipsoid obelisk;

PVector[] droidDirs;
int nbrDroids;

TerrainCam cam;
int camHoversAt = 8;

float terrainSize = 1000;
float horizon = 400;

long time;
float camSpeed;
int count;

// List of image files for texture
String[] textures = new String[] {
  "grid01.png", "tartan.jpg", "rouge.jpg",
  "globe.jpg",  "sampler01.jpg"};

void setup(){
  size(400,320, OPENGL);
  cursor(CROSS);

  terrain = new Terrain(this, 60, terrainSize, horizon);
  terrain.usePerlinNoiseMap(0, 40, 0.15f, 0.15f);
  terrain.setTexture("grass2.jpg", 4);
  terrain.tag = "Ground";
  terrain.tagNo = -1;
  terrain.drawMode(Shape3D.TEXTURE);
  
  skybox = new Box(this,1000,500,1000);
  skybox.setTexture("back.jpg", Box.FRONT);
  skybox.setTexture("front.jpg", Box.BACK);
  skybox.setTexture("left.jpg", Box.LEFT);
  skybox.setTexture("right.jpg", Box.RIGHT);
  skybox.setTexture("sky.jpg", Box.TOP);
  skybox.visible(false, Box.BOTTOM);
  skybox.drawMode(Shape3D.TEXTURE);
  skybox.tag = "Skybox";
  skybox.tagNo = -1;

  nbrDroids = 16;
  droids = new Cone[nbrDroids];
  droidDirs = new PVector[nbrDroids];
  for(int i = 0; i < nbrDroids; i++){
    droids[i] = new Cone(this, 10);
    droids[i].setSize(5,5,-20);
    droids[i].moveTo(getRandomPosOnTerrain(terrain, terrainSize, 50));
    droids[i].tagNo = 100;
    droids[i].fill(color(random(128,255), random(128,255), random(128,255)));
    droids[i].drawMode(Shape3D.SOLID);
    droidDirs[i] = getRandomVelocity(random(15,25));
    terrain.addShape(droids[i]);
  }

  obelisk = new Ellipsoid(this,4,20);
  obelisk.setRadius(5,30,5);
  obelisk.moveTo(getRandomPosOnTerrain(terrain, terrainSize, 28));
  obelisk.fill(color(0,255,0));
  obelisk.tag = "Globe";
  obelisk.tagNo = 1;
  obelisk.setTexture(textures[1]);
  obelisk.drawMode(Shape3D.TEXTURE);

  tube = new Tube(this, 10,30);
  tube.setSize(3, 10, 6, 6, 20);
  tube.moveTo(getRandomPosOnTerrain(terrain, terrainSize, 15.5));
  tube.tagNo = 0;
  tube.setTexture(textures[1]);
  tube.drawMode(Shape3D.TEXTURE);
  tube.fill(color(255,0,0), Tube.S_CAP);
  tube.fill(color(0,255,0), Tube.E_CAP);
  tube.drawMode(Shape3D.SOLID, Tube.BOTH_CAP);
  
  camSpeed = 10;
  cam = new TerrainCam(this);
  cam.adjustToTerrain(terrain, Terrain.WRAP, camHoversAt);
  cam.camera();
  cam.speed(camSpeed);
  cam.forward.set(cam.lookDir());

  // Tell the terrain what camera to use
  terrain.cam = cam;

  time = millis();
}

void draw(){
  background(255);
  ambientLight(220, 220, 220);
  directionalLight(255,255,255,-100,-200,200);
  // Get elapsed time
  long t = millis() - time;
  time = millis();

  // Update shapes on terrain
  update(t/1000.0f);

  // Update camera speed and direction
  if(mousePressed){
    float achange = (mouseX - pmouseX) * PI / width;
    // Keep view and move directions the same
    cam.rotateViewBy(achange);
    cam.turnBy(achange);
  }
  // Update camera speed and direction
  if(keyPressed){
    if(key == 'W' || key =='w' || key == 'P' || key == 'p'){
      camSpeed += (t/100.0f);
      cam.speed(camSpeed);
    }
    else if(key == 'S' || key =='s' || key == 'L' || key == 'l'){
      camSpeed -= (t/100.0f);
      cam.speed(camSpeed);
    }
    else if(key == ' '){
      camSpeed = 0;
      cam.speed(camSpeed);
    }
  }
  // Calculate amount of movement based on velocity and time
  cam.move(t/1000.0f);
  // Adjust the cameras position so we are over the terrain
  // at the given height.
  cam.adjustToTerrain(terrain, Terrain.WRAP, camHoversAt);
  // Set the camera view before drawing
  cam.camera();

  obelisk.draw();
  tube.draw();
  terrain.draw();

  // Get rid of directional lights so skybox is evenly lit.
  noLights();
  skybox.moveTo(cam.eye().x, 0, cam.eye().z);
  skybox.draw();
}

/**
 * Update artefacts and seekers
 */
public void update(float time){
  PVector np;
  obelisk.rotateBy(0, time*radians(16.9f), 0);
  tube.rotateBy(time*radians(25.6f), time*radians(6.871f), time*radians(17.3179f));
  for(int i = 0; i < nbrDroids; i++){
    np = PVector.add(droids[i].getPosVec(), PVector.mult(droidDirs[i],time));
    droids[i].moveTo(np);
    droids[i].adjustToTerrain(terrain, Terrain.WRAP, 15);
  }
}

/**
 * Get a random position on the terrain avoiding the edges
 * @param t the terrain
 * @param tsize the size of the terrain
 * @param height height above terrain
 * @return
 */
public PVector getRandomPosOnTerrain(Terrain t, float tsize, float height){
  PVector p = new PVector(random(-tsize/2.1f, tsize/2.1f), 0, random(-tsize/2.1f, tsize/2.1f));
  p.y = t.getHeight(p.x, p.z) - height;
  return p;
}

/**
 * Get random direction for seekers.
 * @param speed
 */
public PVector getRandomVelocity(float speed){
  PVector v = new PVector(random(-10000, 10000), 0 ,random(-10000,10000));
  v.normalize();
  v.mult(speed);
  return v;
}

/**
 * next texture or change color
 */
public void mouseClicked(){
  cam.camera();
  Shape3D selected = Shape3D.pickShape(this, mouseX, mouseY);
  println(selected);
  if(selected != null){
    if(selected.tagNo > textures.length)
      selected .fill(color(random(128,255), random(128,255), random(128,255)));
    else if(selected.tagNo >= 0){
      selected.tagNo = (selected.tagNo + 1) % textures.length;
      selected.setTexture(textures[selected.tagNo]);
    }
  }
}
