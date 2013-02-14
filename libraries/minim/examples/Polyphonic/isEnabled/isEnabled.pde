/**
  * This sketch demonstrates how to use the <code>isEnabled(AudioSignal)</code> method of a <code>Polyphonic</code> class. 
  * Currently the only <code>Polyphonic</code> class in Minim is <code>AudioOutput</code>. 
  * This sketch creates an output and adds a sine wave to it. Text is displayed on the screen telling you whether 
  * the sine wave is enabled or not. Press 'e' to enable the sine wave and 'd' to disable it.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
SineWave sine;
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
  out.addSignal(sine);
  
  textFont(createFont("Arial", 12));
  textMode(SCREEN);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  if ( out.isEnabled(sine) )
  {
    text("The sine wave is enabled.", 5, 15);
  }
  else
  {
    text("The sine wave is disabled.", 5, 15);
  }
}

void keyPressed()
{
  if ( key == 'e' )
  {
    out.enableSignal(sine);
  }
  if ( key == 'd' )
  {
    out.disableSignal(sine);
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
