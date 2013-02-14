/**
  * This sketch demonstrates how to use the <code>skip</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also skip an <code>AudioSnippet</code>.
  * When you skip, it is always measured from the current position of the recording. So <code>skip(100)</code> will 
  * set the "playhead" at 100 milliseconds from the current position. A sort of fast-forward. It is also possible 
  * to skip in a negative direction. So <code>skip(-200)</code> will set the "playhead" to 200 milliseconds before 
  * the current position. Using <code>skip</code> will not change the play state of <code>Playable</code>, 
  * meaning that if it was already playing it will continue playing from the new position, but if it was not playing, 
  * skipping will not start playback, it will simply set the point at which playback will begin. 
  * If an error occurs while trying to skip, the position will not change. 
  * If you try to skip to a position that is less than zero or try to skip past the end of the 
  * recording, the position will be clamped to zero or <code>length()</code>. 
  * <p>
  * Press 'f' to skip by 100 milliseconds.<br />
  * Press 'r' to skip by -500 milliseconds.
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
  if ( key == 'f' )
  {
    groove.skip(100);
  }
  if ( key == 'r' )
  {
    groove.skip(-500);
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
