/**
  * This sketch demonstrates how to use the <code>getSignal(int)</code> method of a <code>Polyphonic</code> class. 
  * Currently the only <code>Polyphonic</code> class in Minim is <code>AudioOutput</code>. 
  * This sketch adds four sine waves to the output. You can get one by pressing '1', '2', '3', or '4'. 
  * The frequency of the sine wave will be shown on the screen. 
  * Signals are indexed starting from zero, so if you want to get the third signal you'd call 
  * <code>getSignal(2)</code>. Signals are returned from <code>getSignal</code> as <code>AudioSignals</code>, so 
  * if you want assign the returned signal to a variable of a derived type, you have to cast to that type first. 
  * See the code for details. 
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
SineWave sine200;
SineWave sine400;
SineWave sine450;
SineWave sine1200;
SineWave selected;
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
  selected = null;
  // add the signal to out
  out.addSignal(sine200);
  out.addSignal(sine400);
  out.addSignal(sine450);
  out.addSignal(sine1200);
  
  textFont(createFont("Arial", 12));
  textMode(SCREEN);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  if ( selected != null )
  {
    text("The currently selected sine wave has a frequency of " + selected.frequency() + " Hz.", 5, 15);
  }
  else
  {
    text("There is currently no selected sine wave.", 5, 15);
  }
}

void keyPressed()
{
  // in order to assign the AudioSignal returned by getSignal to selected
  // we have to cast it to the type of selected. note that if
  // the signal returned by getSignal was not a SineWave, 
  // attempting to cast it to one would result in a ClassCastException
  if ( key == '1' ) selected = (SineWave)out.getSignal(0);
  if ( key == '2' ) selected = (SineWave)out.getSignal(1);
  if ( key == '3' ) selected = (SineWave)out.getSignal(2);
  if ( key == '4' ) selected = (SineWave)out.getSignal(3);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
