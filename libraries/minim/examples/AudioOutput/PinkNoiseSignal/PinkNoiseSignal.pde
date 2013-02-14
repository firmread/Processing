/**
  * This sketch demonstrates the difference between pink noise and white noise.<br />
  * Move the mouse up and down to change the amplitude of the noise, left and right to change the panning.<br />
  * Type 'w' to toggle between white noise and pink noise. The sketch starts playing pink noise.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
PinkNoise pn;
WhiteNoise wn;
boolean useWhite;

void setup()
{
  size(512, 200);
  
  minim = new Minim(this);
  out = minim.getLineOut();
  // make a pink noise signal with an amplitude of 0.5
  pn = new PinkNoise(0.5);
  // make a white noise signal with an amplitude of 0.5
  wn = new WhiteNoise(0.5);
  // add the pink noise signal to the output
  out.addSignal(pn);
  // add the white noise signal to the output
  out.addSignal(wn);
  // and disable it
  out.disableSignal(wn);
  // boolean used to toggle between pink and white noise
  useWhite = false;
  
  textFont(createFont("Arial", 12));
}

void draw()
{
  background(0);
  stroke(255);
  // draw the waveforms
  for(int i = 0; i < out.bufferSize()-1; i++)
  {
    float x1 = map(i, 0, out.bufferSize(), 0, width);
    float x2 = map(i+1, 0, out.bufferSize(), 0, width);
    line(x1, 50 + out.left.get(i)*50, x2, 50 + out.left.get(i+1)*50);
    line(x1, 150 + out.right.get(i)*50, x2, 150 + out.right.get(i+1)*50);
  }
  // draw 0 lines
  stroke(255, 0, 0);
  line(0, 50, width, 50);
  line(0, 150, width, 150);
  
  if ( out.isEnabled(wn) ) 
  {
    text("White noise.", 5, 15);
  }
  else
  {
    text("Pink noise.", 5, 15);
  }
}

void mouseMoved()
{
  float amp = map(mouseY, 0, height, 1, 0);
  float pan = map(mouseX, 0, width, -1, 1);
  wn.setAmp(amp);
  wn.setPan(pan);
  pn.setAmp(amp);
  pn.setPan(pan);
}

void keyReleased()
{
  // here you can see how it is possible to interactively add and remove signals from an AudioOutput
  if ( key == 'w' ) 
  {
    useWhite = !useWhite;
    println("Use white: " + useWhite);
    if ( useWhite )
    {
      out.disableSignal(pn);
      out.enableSignal(wn);
    }
    else
    {
      out.disableSignal(wn);
      out.enableSignal(pn);
    }
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting
  minim.stop();
  
  super.stop();
}
