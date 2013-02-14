/**
  * This sketch demonstrates how to use the <code>pause</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also pause an <code>AudioSnippet</code>.
  * Pausing a <code>Playable</code> causes it to cease playback but not change position, so that when you 
  * resume playback it will start from where you last paused it. Press 'p' to pause the player.
  *
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer groove;
WaveformRenderer waveform;

void setup()
{
  size(512, 200, P3D);

  minim = new Minim(this);
  groove = minim.loadFile("groove.mp3", 2048);
  
  waveform = new WaveformRenderer();
  // see the example Recordable >> addListener for more about this
  groove.addListener(waveform);
  groove.loop();
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == 'p' ) groove.pause();
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
