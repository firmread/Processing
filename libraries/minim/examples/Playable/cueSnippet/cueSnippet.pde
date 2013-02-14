/**
  * This sketch demonstrates how to use the <code>cue</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also cue an <code>AudioSnippet</code>.
  * When you cue, it is always measured from the beginning of the recording. So <code>cue(100)</code> will 
  * set the "playhead" at 100 milliseconds from the beginning no matter where it currently is. 
  * Cueing a <code>Playable</code> object will not change the playstate, meaning that if it was already playing 
  * it will continue playing from the cue point, but if it was not playing, cueing will not start playback, it 
  * will simply set the point at which playback will begin. If an error occurs while trying to cue, 
  * the position will not change. If you try to cue to a negative position or try to cue past the end of the 
  * recording, the amount will be clamped to zero or length(). 
  * <p>
  * Press 'c' to cue to 5000 milliseconds.
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioSnippet groove;

void setup()
{
  size(512, 200, P3D);
  
  minim = new Minim(this);
  groove = minim.loadSnippet("groove.mp3");
  groove.loop();
}

void draw()
{
  background(0);
}

void keyPressed()
{
  if ( key == 'c' )
  {
    groove.cue(5000);
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
