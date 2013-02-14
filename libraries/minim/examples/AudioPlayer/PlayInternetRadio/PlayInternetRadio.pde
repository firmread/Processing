/**
  * This sketch demonstrates how to play an internet radio stream with Minim. 
  * You load the stream like any other file and then just use <code>play()</code> 
  * to start playing it. You can still use the other methods of a <code>Playable</code> 
  * object and it shouldn't break anything, but it also won't accomplish much. For example, 
  * rewinding streaming audio simply reloads the stream, there's no TIVO-like buffer 
  * that is kept. Not all internet radio streams will work in an applet. Another 
  * limitation of applets is that they must be signed to be able to access remote hosts. 
  * If your applet is running from www.foo.com and you want to access the mp3 stream 
  * at www.bar.com, you must sign the applet. If you are just trying to load a file 
  * from www.foo.com/file, you shouldn't need to sign it.
  * <p>
  * The stream you are listening to is Limbik Frequencies.
  */

import ddf.minim.*;

Minim minim;
AudioPlayer radio;

void setup()
{
  size(512, 200, P3D);
  minim = new Minim(this);
  // load a URL
  radio = minim.loadFile("http://205.188.215.225:8018/", 2048);
  // play the file
  radio.play();
}

void draw()
{
  background(0);
  stroke(255);
  // draw the waveforms
  // the values returned by left.get() and right.get() will be between -1 and 1,
  // so we need to scale them up to see the waveform
  // note that if the file is MONO, left.get() and right.get() will return the same value
  for(int i = 0; i < radio.bufferSize()-1; i++)
  {
    float x1 = map(i, 0, radio.bufferSize(), 0, width);
    float x2 = map(i+1, 0, radio.bufferSize(), 0, width);
    line(x1, 50 + radio.left.get(i)*50, x2, 50 + radio.left.get(i+1)*50);
    line(x1, 150 + radio.right.get(i)*50, x2, 150 + radio.right.get(i+1)*50);
  }
}

void stop()
{
  radio.close();
  minim.stop();
  
  super.stop();
}
