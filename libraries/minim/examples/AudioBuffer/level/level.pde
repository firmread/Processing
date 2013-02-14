/**
  * This sketch is an example of how to use the <code>level</code> method of an <code>AudioBuffer</code> to get the 
  * level of one of an <code>AudioSource</code>'s sample buffers. The classes in Minim that extend <code>AudioSource</code> 
  * and therefore inherit the <code>left</code>, <code>right</code>, and <code>mix</code> buffers of that class, are 
  * <code>AudioInput</code>, <code>AudioOutput</code>, <code>AudioSample</code>, and <code>AudioPlayer</code>. 
  * Not coincidentally, these are also all of the classes in Minim that are <code>Recordable</code>. 
  * <p>
  * The value returned by <code>level</code> will always be between zero and one, but you may find that the value 
  * returned is often smaller than you expect. The level is found by calculating the root-mean-squared amplitude of the 
  * samples in the buffer. First the samples are all squared, then the average (mean) of all the samples is taken (sum
  * and then divide by the number of samples), then the square root of the average is returned. This is why the range can be 
  * determined as [0, 1] because the largest value a squared sample can have is 1. However, in order for the RMS amplitude 
  * to equal 1, every sample must have either -1 or 1 as its value (amplitude). This is only going to be the case 
  * if your sound is a square wave at full amplitude. If your sound is a song or other complex sound source, 
  * the level is generally going to be much lower.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioPlayer groove;

void setup()
{
  size(200, 200, P3D);
  minim = new Minim(this);
  groove = minim.loadFile("groove.mp3");
  groove.loop();
  rectMode(CORNERS);
}

void draw()
{
  background(0);
  fill(255);
  // draw the current level of the left and right sample buffers
  // level() returns a value between 0 and 1, so we scale it up
  rect(0, height, width/2, height - groove.left.level()*1000);
  rect(width/2, height, width, height - groove.right.level()*1000);
}

void stop()
{
  // always close Minim audio classes when you finish with them
  groove.close();
  // always stop Minim before exiting
  minim.stop();
  
  super.stop();
}
