/**
  * This sketch demonstrates how to use the <code>hasEffect(AudioEffect)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can use this method on <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. This sketch creates a low pass filter in setup. 
  * You can add the filter to the player by pressing 'a' and remove it by pressing 'r'. The text on the screen
  * will inform you if the player has the effect on it or not (and so will your ears).
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
  
  textFont(createFont("Arial", 14));
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  if ( groove.hasEffect(lpfilter) )
  {
    text("The filter is attached to the player.", 5, 15);
  }
  else
  {
    text("The filter is not attached to the player.", 5, 15);
  }
}

void keyPressed()
{
  // only add the effect if it's not already there
  if ( key == 'a' && !groove.hasEffect(lpfilter) ) 
  {
    groove.addEffect(lpfilter);
  }
  if ( key == 'r' ) 
  {
    // if the effect isn't on the player, this does nothing
    groove.removeEffect(lpfilter);
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
