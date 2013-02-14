/**
  * This sketch demonstrates how to play a file from the web with Minim. The one caveat is that if you are running 
  * your sketch as an applet, it must be signed to be able to load files from remote hosts. 
  * If your applet is running from www.foo.com and you want to access an audio file at www.bar.com, 
  * you must sign the applet. If you are just trying to load a file from www.foo.com/file, you shouldn't need to sign 
  * the applet.
  */

import ddf.minim.*;

Minim minim;
AudioPlayer player;

void setup()
{
  size(512, 200, P3D);
  minim = new Minim(this);
  // load a file, give the AudioPlayer buffers that are 2048 samples long
  player = minim.loadFile("http://code.compartmental.net/minim/examples/AudioPlayer/marcus_kellis_theme.mp3", 2048);
  // play the file
  player.play();
}

void draw()
{
  background(0);
  stroke(255);
  // draw the waveforms
  // the values returned by left.get() and right.get() will be between -1 and 1,
  // so we need to scale them up to see the waveform
  // note that if the file is MONO, left.get() and right.get() will return the same value
  for(int i = 0; i < player.bufferSize()-1; i++)
  {
    float x1 = map(i, 0, player.bufferSize(), 0, width);
    float x2 = map(i+1, 0, player.bufferSize(), 0, width);
    line(x1, 50 + player.left.get(i)*50, x2, 50 + player.left.get(i+1)*50);
    line(x1, 150 + player.right.get(i)*50, x2, 150 + player.right.get(i+1)*50);
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  player.close();
  // always close Minim before exiting
  minim.stop();
  
  super.stop();
}
