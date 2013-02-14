/**
  * This sketch demonstrates how to use the <code>disableSignal(AudioSignal)</code> method of a <code>Polyphonic</code> class. 
  * Currently the only <code>Polyphonic</code> class in Minim is <code>AudioOutput</code>. 
  * This sketch adds a sine wave and a saw wave to the output and you can disable the saw wave by pressing any key.
  * Disabling a signal means that it will not be used when a <code>Polyphonic</code>
  * class is asked to produce a new buffer of audio, but the signal remains in the signal chain so that you can 
  * enable it again without having to re-add it to the chain.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
SineWave sine;
SawWave saw;
WaveformRenderer waveform;

void setup()
{
  size(512, 200, P3D);

  minim = new Minim(this);
  // this gets a stereo output
  out = minim.getLineOut();
    
  waveform = new WaveformRenderer();
  // see the example Recordable >> addListener for more about this
  out.addListener(waveform);
  
  // see the example AudioOutput >> SineWaveSignal for more about this
  sine = new SineWave(300, 0.2, out.sampleRate());
  // see the example AudioOutput >> SawWaveSignal for more about this
  saw = new SawWave(380, 0.05, out.sampleRate());
  // add the signal to out
  out.addSignal(sine);
  out.addSignal(saw);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  out.disableSignal(saw);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
