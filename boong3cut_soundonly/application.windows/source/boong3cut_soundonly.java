import processing.core.*; 
import processing.xml.*; 

import ddf.minim.*; 
import ddf.minim.signals.*; 

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

public class boong3cut_soundonly extends PApplet {


int shoutsq;

//,,,,,,,,Sound



Minim minim;
AudioInput in;

float soundVolume;
float soundscale = 450;



public void setup() {
  size (680,510);
  background(0);
  smooth();
  
  shoutsq = 200;
  
  //,,,,,,,,Sound
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 2048);
  
}





  
  
public void draw() {
  noStroke();
  background(255);
  rectMode(CENTER);
  fill(0,150);
  rect(width/2,height/2,shoutsq,shoutsq);
  
  soundVolume = in.left.level()*soundscale;
  println(soundVolume + "," + shoutsq);
  if (soundVolume <100 && shoutsq > 10){
    shoutsq -=3;
  }
  if (soundVolume <200 && shoutsq >10){
    shoutsq -=2;
  }
  if (soundVolume > 100 ){
    if( shoutsq < 300){
      shoutsq += 2;
    }
    if( shoutsq < 600){
      shoutsq += 1;
    }
  }
  if (soundVolume > 300 ){
    if(shoutsq < 300){
      shoutsq += 3;
    }
    if(shoutsq < 600){
      shoutsq += 2;
    }
    if(shoutsq <1000){
      shoutsq += 1;
    }
  }    

 
}





public void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();
  
  super.stop();
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "boong3cut_soundonly" });
  }
}
