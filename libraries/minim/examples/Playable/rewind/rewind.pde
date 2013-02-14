/**
  * This sketch demonstrates how to use the <code>rewind</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also rewind an <code>AudioSnippet</code>.
  * Rewinding a <code>Playable</code> sets the position to zero, the beginning. Rewinding doesn't change 
  * the play state of a <code>Playable</code> so if it is playing or looping when you rewind, it will 
  * continue to play or loop after you rewind it. Press 'r' to rewind the player.
  *
  */

import ddf.minim.*;

Minim minim;
AudioPlayer groove;
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
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == 'r' ) groove.rewind();
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
