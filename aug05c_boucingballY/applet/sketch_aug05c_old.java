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

public class sketch_aug05c_old extends PApplet {

//declaring variable
float circlex;
float circley;
float vely = 3;
float velx = 5;

public void setup(){
  background(255);
  smooth();
  frameRate(60);
  size(500,500);
}

public void draw(){
  fill(255,227,8);
  //noStroke();
  strokeWeight(3);
  circlex+=velx;
  circley+=vely; // the same as circley=circley+vel
  ellipse(250,circley,circley,circley);
  if((circlex<0)||(circlex>width)){
    velx=velx*-1;
  }
  if((circley<0)||(circley>height)){
    vely=vely*-1;
  }
  
}
   //same as vel=vel
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "sketch_aug05c_old" });
  }
}
