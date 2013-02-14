/**
  * This sketch demonstrates how to use the <code>removeListener</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also remove listeners from <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. The class defined in waveform.pde implements 
  * the <code>AudioListener</code> interface and can therefore be removed as a listener to <code>groove</code>.
  * The waveform renderer starts off not added to the player, so you will initially not see any waveforms drawn.
  * <p>
  * Press 'a' to add the waveform renderer to the player.<br />
  * Press 'r' to remove the waveform renderer from the player.<br />
  * <br />
  * You'll see that when you remove the waveform renderer as a listener of the player, the waveform freezes. This is 
  * because the waveform is drawing the last buffer of samples that it received from the player.
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
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
}

void keyPressed()
{
  if ( key == 'a' )
  {
    groove.addListener(waveform);
  }
  if ( key == 'r' )
  {
    groove.removeListener(waveform);
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
