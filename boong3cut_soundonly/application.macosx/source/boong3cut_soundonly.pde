
int shoutsq;

//,,,,,,,,Sound
import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioInput in;

float soundVolume;
float soundscale = 450;



void setup() {
  size (680,510);
  background(0);
  smooth();
  
  shoutsq = 200;
  
  //,,,,,,,,Sound
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 2048);
  
}





  
  
void draw() {
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





void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();
  
  super.stop();
}
