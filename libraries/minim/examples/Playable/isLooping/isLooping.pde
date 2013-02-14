/**
  * This sketch demonstrates how to use the <code>isLooping</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>. You can also check the loop status of an <code>AudioSnippet</code>,
  * but it will return true whether the snippet is looping or just playing because there is no way to query 
  * an <code>Clip</code>s loop status. Press 'p' to play the file and 'l' to loop the file. Text on the screen 
  * will indicate whether or not the file is looping.
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
  
  textFont(createFont("Arial", 12));
  textMode(SCREEN);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  
  if ( groove.isLooping() )
  {
    text("The player is looping.", 5, 15);
  }
  else
  {
    text("The player is not looping.", 5, 15);
  }
}

void keyPressed()
{
  if ( key == 'p' )
  {
    groove.play();
  }
  if ( key == 'l' )
  {
    groove.loop();
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
