/**
  * This sketch demonstrates how to use the <code>enableSignal(int)</code> method of a <code>Polyphonic</code> class. 
  * Currently the only <code>Polyphonic</code> class in Minim is <code>AudioOutput</code>. 
  * This sketch adds four sine waves to the output and then disables all of them. 
  * You can enable them again by pressing '1', '2', '3', and '4'. 
  * Signals are indexed starting from zero, so if you want to enable the third signal you'd call 
  * <code>enableSignal(2)</code>. Enabling a signal means that it will be used when a <code>Polyphonic</code>
  * class is asked to produce a new buffer of audio.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
SineWave sine200;
SineWave sine400;
SineWave sine450;
SineWave sine1200;
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
  
  // see the example AudioOutput >> SineWaveSignal for more about these
  sine200 = new SineWave(200, 0.2, out.sampleRate());
  sine400 = new SineWave(400, 0.2, out.sampleRate());
  sine450 = new SineWave(450, 0.2, out.sampleRate());
  sine1200 = new SineWave(1200, 0.2, out.sampleRate());
  // add the signal to out
  out.addSignal(sine200);
  out.addSignal(sine400);
  out.addSignal(sine450);
  out.addSignal(sine1200);
  out.noSound();
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == '1' ) out.enableSignal(0);
  if ( key == '2' ) out.enableSignal(1);
  if ( key == '3' ) out.enableSignal(2);
  if ( key == '4' ) out.enableSignal(3);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
