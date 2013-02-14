/**
  *  This sketch demonstrates how it is possible to put two signals on one AudioOutput.<br />
  *  You do need to be aware that the AudioOuput will simply sum all of the signals that are attached to it.
  *  This means it is your responsibility to make sure the total signal doesn't clip. Clipping occurs when
  *  a sample value is greater than 1 or less than -1 and will happen if you have two signals whose amplitudes 
  *  are both 1, for instance. If the signal exceeds the range [-1, 1] it will be limited by the system, so you 
  *  won't get nasty sounding distortion but your signal will sound slightly different.
  *  <p>
  *  Use the knobs to adjust the frequency, amplitude and pan of the two signals. In particular, notice what 
  *  happens when you put the frequencies very close together.
  */

import ddf.minim.*;
import ddf.minim.signals.*;
import controlP5.*;

Minim minim;
AudioOutput out;
SineWave sine;
TriangleWave tri;
ControlP5 gui;

public float triAmp = 0.5;
public float triPan = 0;
public float triFreq = 880;

public float sinAmp = 0.5;
public float sinPan = 0.5;
public float sinFreq = 440;

void setup()
{
  size(512, 400, P3D);
  minim = new Minim(this);
  // get a stereo line out from Minim with a 2048 sample buffer, default sample rate is 44100, bit depth is 16
  out = minim.getLineOut(Minim.STEREO, 2048);
  // create a sine wave Oscillator, set to 440 Hz, at 0.5 amplitude, sample rate 44100 to match the line out
  sine = new SineWave(440, 0.5, out.sampleRate());
  // set the portamento speed on the oscillator to 200 milliseconds
  sine.portamento(200);
  // add the oscillator to the line out
  out.addSignal(sine);
  // create a triangle wave  Oscillator
  tri = new TriangleWave(880, 0.5, out.sampleRate());
  tri.portamento(200);
  out.addSignal(tri);
  
  // set up the gui
  gui = new ControlP5(this);
  gui.addKnob("triFreq", 40, 5000, 10, 250, 60);
  gui.addKnob("triAmp", 0, 1, 0.5, 85, 250, 60);
  gui.addKnob("triPan", -1, 1, 0, 165, 250, 60);
  gui.addKnob("sinFreq", 40, 5000, 280, 250, 60);
  gui.addKnob("sinAmp", 0, 1, 0.5, 355, 250, 60);
  gui.addKnob("sinPan", -1, 1, 0, 430, 250, 60);
}

void draw()
{
  background(0);
  gui.draw();
  stroke(255);
  // draw the waveforms
  for(int i = 0; i < out.bufferSize()-1; i++)
  {
    float x1 = map(i, 0, out.bufferSize(), 0, width);
    float x2 = map(i+1, 0, out.bufferSize(), 0, width);
    line(x1, 50 + out.left.get(i)*50, x2, 50 + out.left.get(i+1)*50);
    line(x1, 150 + out.right.get(i)*50, x2, 150 + out.right.get(i+1)*50);
  }
  tri.setFreq(triFreq);
  tri.setAmp(triAmp);
  tri.setPan(triPan);
  sine.setFreq(sinFreq);
  sine.setAmp(sinAmp);
  sine.setPan(sinPan);
}

void stop()
{
  out.close();
  minim.stop();
  
  super.stop();
}
