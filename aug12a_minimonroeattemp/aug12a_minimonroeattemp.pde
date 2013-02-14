import ddf.minim.*;

Minim minim;
AudioPlayer in;
 
PImage monroe;

void setup(){
  size(500,500);
  background(0);
  monroe = loadImage ("monroe.png");
  noStroke();
  smooth();
  
  minim = new Minim(this);
}
 
void draw(){
  float r =map(mouseX,0,width,0,255);
  float g =map(mouseY,0,width,0,255);
  //bg
  fill(r,g,100);
  rect(0,0,width,height);
  //whiteeyes
  fill(255);
  rect(110,235,250,50);
  //eyes
  float leftEyePos = map(mouseX,0,width,141,158);
  float rightEyePos = map(mouseX,0,width,255,275);
  fill(0);
  ellipse(leftEyePos,264,20,20);
  ellipse(rightEyePos,264,20,20);  
  //monroe
  image(monroe,0,0);

}
 
void stop()
{
  // always close audio I/O classes
  in.close();
  // always stop your Minim object
  minim.stop();
 
  super.stop();
}
