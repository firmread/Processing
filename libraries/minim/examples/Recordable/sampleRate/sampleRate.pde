/**
  * This sketch demonstrates how to use the <code>sampleRate</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioOutput</code>, but you can also get the sample rate of <code>AudioInput</code>, 
  * <code>AudioPlayer</code>, and <code>AudioSample</code> objects. The <code>sampleRate</code> method returns 
  * the sample rate of the audio that the <code>Recordable</code> is working with.
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
  // a sample rate of 22010 and a bit depth of 16
  out = minim.getLineOut(Minim.MONO, 1024, 22010);
}

void draw()
{
  background(0);
  text("The sample rate of output is " + out.sampleRate() + ".", 5, 15);
  if ( out.sampleRate() != 22010 )
  {
    text("However, this is totally not the sample rate I asked for.", 5, 30);
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
