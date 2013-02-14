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

public class sketch_aug05f_2 extends PApplet {



//declaring variable
float circlex=0;
float circley=0;
float vely = 10;
float velx = 10;
float g = 1;

public void setup(){
  background(0);
  smooth();
  frameRate(60);
  size(1000,180);
}

public void draw(){

  fill(random(150,200),random(150,200),0,random(100,255));
  //noStroke();
  strokeWeight(1);
  circlex+=velx;
  vely+=g;
  circley+=vely;
   // the same as circley=circley+vel
  ellipse(circlex,circley,random(40,50),random(40,50));
  if((circlex<0)||(circlex>width)){
    velx=velx*-1;
  }
  if((circley<0)||(circley>height)){
    vely=vely*-1;
  }
  
}
   //same as vel=vel
   //inserting picture
   
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "sketch_aug05f_2" });
  }
}
