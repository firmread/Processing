/**
  * This sketch demonstrates how to use the <code>enableEffect(int)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also enable effects on <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. Effects added to an <code>Effectable</code> are 
  * stored in the order they are added. They are indexed starting from zero. So if you want to enable the third 
  * effect in the chain you'd call <code>enableEffect(2)</code>. This sketch adds four low pass filters to 
  * the player and disables them all. You can then enable them in any order by pressing '1', '2', '3', and '4'. 
  * If you want to hear the sound change with each enable, then enable them in order, 1 - 4.
  * Enabling an effect means it will be included in effect processing when the <code>Effectable</code> 
  * you added it to processes a buffer of samples.
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer groove;
LowPassFS lpf1;
LowPassFS lpf2;
LowPassFS lpf3;
LowPassFS lpf4;
WaveformRenderer waveform;

void setup()
{
  size(512, 200, P3D);
  
  minim = new Minim(this);
  
  groove = minim.loadFile("groove.mp3", 2048);
  groove.loop();
  
  waveform = new WaveformRenderer();
  // see the example Recordable >> addListener for more about this
  groove.addListener(waveform);
  
  // see the example AudioEffect >> LowPassFSFilter for more about this effect
  lpf1 = new LowPassFS(3000, groove.sampleRate());
  lpf2 = new LowPassFS(2000, groove.sampleRate());
  lpf3 = new LowPassFS(1000, groove.sampleRate());
  lpf4 = new LowPassFS(500, groove.sampleRate());
  // add the effects to the player
  groove.addEffect(lpf1);
  groove.addEffect(lpf2);
  groove.addEffect(lpf3);
  groove.addEffect(lpf4);
  // disable all effects
  groove.noEffects();
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == '1' ) groove.enableEffect(0);
  if ( key == '2' ) groove.enableEffect(1);
  if ( key == '3' ) groove.enableEffect(2);
  if ( key == '4' ) groove.enableEffect(3);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
