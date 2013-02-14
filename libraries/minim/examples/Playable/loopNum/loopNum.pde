/**
  * This sketch demonstrates how to use the <code>loop(int)</code> method of a <code>Playable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also loop an <code>AudioSnippet</code>.
  * When you call <code>loop(int)</code> it will make the <code>Playable</code> loop for the number of times 
  * you specify. So, <code>loop(3)</code> will loop the recording three times, which will result in the recording 
  * being played 4 times. This may seem odd, but it is consistent with the behavior of a JavaSound <code>Clip</code>.
  * If you want to make it stop looping you can call <code>play()</code> and it will finish the current loop 
  * and then stop. Press any of the number keys to make the player loop that many times. Text will be displayed 
  * on the screen indicating your most recent choice.
  *
  */

import ddf.minim.*;

Minim minim;
AudioPlayer groove;
WaveformRenderer waveform;
int loopcount;

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
  
  text("The player has " + groove.loopCount() + " loops left. Is playing: " + groove.isPlaying() + ", Is looping: " + groove.isLooping(), 5, 15);
}

void keyPressed()
{
  String keystr = String.valueOf(key);
  int num = int(keystr);
  if ( num > 0 && num < 10 )
  {
    groove.loop(num);
    loopcount = num;
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
