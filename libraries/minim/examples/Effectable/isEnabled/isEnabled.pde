/**
  * This sketch demonstrates how to use the <code>isEnabled(AudioEffect)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can use this method on <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. This sketch adds a low pass filter to 
  * the player. You can enable the filter by pressing 'e' and disable it by pressing 'd'. The text on the screen
  * will inform you if the filter is enabled or not (and so will your ears).
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer groove;
LowPassFS lpfilter;
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
  
  // see the example AudioEffect >> LowPassFSFilter for more about this
  lpfilter = new LowPassFS(300, groove.sampleRate());
  // add the effect to the player
  groove.addEffect(lpfilter);
  
  textFont(createFont("Arial", 14));
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  if ( groove.hasEffect(lpfilter) )
  {
    text("The groove has the effect.", 5, 35);
  }
  if ( groove.isEnabled(lpfilter) )
  {
    text("The filter is enabled.", 5, 15);
  }
  else
  {
    text("The filter is not enabled.", 5, 15);
  }
}

void keyPressed()
{
  if ( key == 'e' ) groove.enableEffect(lpfilter);
  if ( key == 'd' ) groove.disableEffect(lpfilter);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
