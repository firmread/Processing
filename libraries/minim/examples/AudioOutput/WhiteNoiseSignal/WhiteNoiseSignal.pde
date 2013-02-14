/**
  *  Here's how to make some white noise. Move the mouse up and down to change the amplitude of the noise, left and 
  * right to change the panning.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
WhiteNoise wn;

void setup()
{
  size(512, 200, P3D);
  
  minim = new Minim(this);
  out = minim.getLineOut();
  // makes a WhiteNoise signal with an amplitude of 0.2
  wn = new WhiteNoise(0.2);
  // adds the signal to the output
  out.addSignal(wn);
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
}

void mouseMoved()
{
  float amp = map(mouseY, 0, height, 1, 0);
  wn.setAmp(amp);
  // note that we could call setPan on out, instead of on wn
  // this would sound the same, but the waveforms in out would not reflect the panning
  float pan = map(mouseX, 0, width, -1, 1);
  wn.setPan(pan);
}

void stop()
{
  out.close();
  minim.stop();
  
  super.stop();
}
