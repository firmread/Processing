/**
  * This sketch demonstrates how to use the <code>effects</code> and <code>noEffects</code> methods of an 
  * <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also toggle effects on <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. These two methods operate just like <code>stroke</code>
  * and <code>noStroke</code>. If you call <code>effects</code>, then all effects attached to the <code>Effectable</code>
  * will be enabled. If you call <code>noEffects</code>, they will all be disabled. The effects in this sketch start out 
  * enabled, as do all effects when you add them to an <code>Effectable</code>.
  * <p>
  * Press 'e' to call <code>effects()</code>.<br />
  * Press 'n' to call <code>noEffects()</code>.
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
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == 'e' ) groove.effects();
  if ( key == 'n' ) groove.noEffects();
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
