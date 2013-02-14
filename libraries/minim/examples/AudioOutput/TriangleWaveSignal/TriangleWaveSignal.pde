/**
  *  This sketch demonstrates how to use a TriangleWave with an AudioOutput.<br />
  *  Move the mouse up and down to change the frequency, left and right to change the panning.<br />
  *  <br />
  *  TriangleWave is a subclass of Oscillator, which is an abstract class that implements the interface AudioSignal.<br />
  *  This means that it can be added to an AudioOutput and the AudioOutput will call one of the two
  *  generate functions, depending on whether the AudioOutput is STEREO or MONO. Since it is an 
  *  abstract class, it can't be directly instantiated, it merely provides the functionality of 
  *  smoothly changing frequency, amplitude and pan. In order to have an Oscillator that actually 
  *  produces sound, you have to extend Oscillator and define the value function. This function 
  *  takes a <b>step</b> value and returns a sample value between -1 and 1.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
TriangleWave tri;

void setup()
{
  size(512, 200, P3D);
  minim = new Minim(this);
  // get a line out from Minim, default sample rate is 44100, bit depth is 16
  out = minim.getLineOut(Minim.STEREO, 2048);
  // create a sine wave Oscillator, set to 440 Hz, at 0.5 amplitude, sample rate to match the output
  tri = new TriangleWave(440, 0.3, out.sampleRate());
  // set the portamento speed on the oscillator to 200 milliseconds
  tri.portamento(200);
  // add the oscillator to the line out
  out.addSignal(tri);
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
  // with portamento on the frequency will change smoothly
  float freq = map(mouseY, 0, height, 1500, 60);
  tri.setFreq(freq);
  // pan always changes smoothly to avoid crackles getting into the signal
  // note that we could call setPan on out, instead of on sine
  // this would sound the same, but the waveforms in out would not reflect the panning
  float pan = map(mouseX, 0, width, -1, 1);
  tri.setPan(pan);
}

void stop()
{
  out.close();
  minim.stop();
  
  super.stop();
}
