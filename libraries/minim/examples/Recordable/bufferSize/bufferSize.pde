/**
  * This sketch demonstrates how to use the <code>bufferSize</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioOutput</code>, but you can also get the buffer size of <code>AudioInput</code>, 
  * <code>AudioPlayer</code>, and <code>AudioSample</code> objects. The <code>bufferSize</code> method returns 
  * the length of the buffer it is using to receive or send samples. You can usually indicate the buffer size that 
  * you want when you get an audio class from <code>Minim</code>. 
  */

import ddf.minim.*;

Minim minim;
AudioOutput out;

void setup()
{
  size(500, 100, P3D);
  textFont(loadFont("CourierNewPSMT-12.vlw"));
  textMode(SCREEN);

  minim = new Minim(this);
  // this should give us a mono output with a 1024 sample buffer, 
  // a sample rate of 44100 and a bit depth of 16
  out = minim.getLineOut(Minim.MONO, 1024);
}

void draw()
{
  background(0);
  text("The buffer size of output is " + out.bufferSize() + ".", 5, 15);
  if ( out.bufferSize() != 1024 )
  {
    text("However, this is totally not the buffer size I asked for.", 5, 30);
  }
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  // always stop Minim before exiting.
  minim.stop();
  
  super.stop();
}
