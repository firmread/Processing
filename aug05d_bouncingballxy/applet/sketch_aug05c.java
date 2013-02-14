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

public class sketch_aug05c extends PApplet {

//declaring variable
float circlex;
float circley;
float vely = 10;
float velx = 5;

public void setup(){
  background(255);
  smooth();
  frameRate(60);
  size(1000,180);
}

public void draw(){
  fill(random(200,255),random(200,255),0);
  //noStroke();
  strokeWeight(3);
  circlex+=velx;
  circley+=vely; // the same as circley=circley+vel
  ellipse(circlex,circley,40,40);
  if((circlex<0)||(circlex>width)){
    velx=velx*-1;
  }
  if((circley<0)||(circley>height)){
    vely=vely*-1;
  }
  
}
   //same as vel=vel
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "sketch_aug05c" });
  }
}
