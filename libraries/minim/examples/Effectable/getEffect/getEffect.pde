/**
  * This sketch demonstrates how to use the <code>getEffect(int)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also get effects from <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. Effects added to an <code>Effectable</code> are 
  * stored in the order they are added. They are indexed starting from zero. So if you want to get the third 
  * effect in the chain you'd call <code>getEffect(2)</code>. This sketch adds four low pass filters to 
  * the player. You can then get one by pressing '1', '2', '3', or '4', the cutoff frequency of the most recently 
  * gotten filter will be displayed on the screen. 
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer groove;
LowPassFS lpf1;
LowPassFS lpf2;
LowPassFS lpf3;
LowPassFS lpf4;
LowPassFS lpf;
WaveformRenderer waveform;

void setup()
{
  size(512, 200, P3D);
  textMode(SCREEN);
  
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
  
  textFont(createFont("Arial", 12));
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  if ( lpf != null )
  {
    text("The low pass filter has a cutoff frequency of " + lpf.frequency() + " Hz.", 5, 15);
  }  
}

void keyPressed()
{
  // getEffect returns an AudioEffect
  // so we want to assign it to a LowPassFS
  // we have to cast it to that class
  if ( key == '1' ) lpf = (LowPassFS)groove.getEffect(0);
  if ( key == '2' ) lpf = (LowPassFS)groove.getEffect(1);
  if ( key == '3' ) lpf = (LowPassFS)groove.getEffect(2);
  if ( key == '4' ) lpf = (LowPassFS)groove.getEffect(3);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
