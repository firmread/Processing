import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class sketch_aug10a extends PApplet {

PImage monroe;
public void setup (){
  size(500,500);
  background(0);
  monroe = loadImage ("monroe.png");
  noStroke();
  smooth();

}

public void draw(){
  
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

public void mouseDragged() {
  fill(255);
  ellipse(146,260,50,50);
  ellipse(260,260,50,50);
    //eyes
  float leftEyePos = map(mouseX,0,width,141,158);
  float rightEyePos = map(mouseX,0,width,255,275);
  fill(0);
  ellipse(leftEyePos,264,20,20);
  ellipse(rightEyePos,264,20,20);  
}

public void mousePressed() {
  ellipse(146,260,50,50);
  ellipse(260,260,50,50);
    //eyes
  float leftEyePos = map(mouseX,0,width,141,158);
  float rightEyePos = map(mouseX,0,width,255,275);
  fill(0);
  ellipse(leftEyePos,264,20,20);
  ellipse(rightEyePos,264,20,20);  
}

  
public void mouseClicked() {
  ellipse(146,260,50,50);
  ellipse(260,260,50,50);
    //eyes
  float leftEyePos = map(mouseX,0,width,141,158);
  float rightEyePos = map(mouseX,0,width,255,275);
  fill(0);
  ellipse(leftEyePos,264,20,20);
  ellipse(rightEyePos,264,20,20);  
}
  
public void mouseReleased() {
  ellipse(146,260,50,50);
  ellipse(260,260,50,50);
    //eyes
  float leftEyePos = map(mouseX,0,width,141,158);
  float rightEyePos = map(mouseX,0,width,255,275);
  fill(0);
  ellipse(leftEyePos,264,20,20);
  ellipse(rightEyePos,264,20,20);  
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "sketch_aug10a" });
  }
}
