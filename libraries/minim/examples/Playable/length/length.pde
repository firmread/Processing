/**
  * This sketch demonstrates how to use the <code>length</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also get the length of an <code>AudioSnippet</code>.
  * The length is how long the recording being played by the player is in milliseconds (1 ms = 1/1000 of a second).
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
  
  textFont(createFont("Arial", 12));
  textMode(SCREEN);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  
  text("The recording is " + groove.length() + " milliseconds long.", 5, 15);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
