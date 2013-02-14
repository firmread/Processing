/**
  * This sketch demonstrates how to use the <code>position</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also get the position of an <code>AudioSnippet</code>.
  * The position is the current position of the "playhead" in milliseconds. In other words, it's how much of the 
  * recording has been played. This sketch demonstrates how you could use the <code>position</code> method to 
  * visualize where in the recording playback is.
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
  
  float x = map(groove.position(), 0, groove.length(), 0, width);
  stroke(255, 0, 0);
  line(x, height/2 - 30, x, height/2 + 30);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
