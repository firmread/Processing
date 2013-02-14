/**
  * This sketch demonstrates how to use the <code>removeEffect(AudioEffect)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also remove effects from <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. This sketch adds a low pass filter to 
  * the player which you can then remove by pressing any key. Removing an effect means that it is completely 
  * removed from the <code>Effectable</code> you added it to. You will need to use <code>addEffect</code> to 
  * add it back, but keep in mind that it will be added at the end of the effects chain. It is not possible 
  * to insert an effect anywhere in the chain.
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
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  groove.removeEffect(lpfilter);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
