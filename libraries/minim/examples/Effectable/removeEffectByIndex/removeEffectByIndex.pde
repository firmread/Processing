/**
  * This sketch demonstrates how to use the <code>removeEffect(int)</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also remove effects from <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. Effects added to an <code>Effectable</code> are 
  * stored in the order they are added. They are indexed starting from zero. So if you want to remove the third 
  * effect in the chain you'd call <code>removeEffect(2)</code>. This sketch adds four low pass filters to 
  * the player. You can then remove them one at a time by pressing any key. Removing an effect means that it is completely 
  * removed from the <code>Effectable</code> you added it to. You will need to use <code>addEffect</code> to 
  * add it back, but keep in mind that it will be added at the end of the effects chain. It is not possible 
  * to insert an effect anywhere in the chain.
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
  lpf1 = new LowPassFS(500, groove.sampleRate());
  lpf2 = new LowPassFS(1000, groove.sampleRate());
  lpf3 = new LowPassFS(2000, groove.sampleRate());
  lpf4 = new LowPassFS(3000, groove.sampleRate());
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
  if ( groove.effectCount() > 0 )
  {
    groove.removeEffect(0);
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  minim.stop();
  
  super.stop();
}
