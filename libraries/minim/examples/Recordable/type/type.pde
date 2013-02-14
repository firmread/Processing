/**
  * This sketch demonstrates how to use the <code>type</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioOutput</code>, but you can also get the type of <code>AudioInput</code>, 
  * <code>AudioPlayer</code>, and <code>AudioSample</code> objects. The <code>type</code> method returns 
  * the number of channels the <code>Recordable</code> has: either 1 or 2. For code clarity you can compare these 
  * to the static constants <code>Minim.MONO</code> and <code>Minim.STEREO</code>.
  *
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
  // this should give us a stereo output with a 1024 sample buffer, 
  // a sample rate of 44100 and a bit depth of 16
  out = minim.getLineOut();
}

void draw()
{
  background(0);
  if ( out.type() == Minim.MONO )
  {
    text("The output is mono.", 5, 15);
  }
  else if ( out.type() == Minim.STEREO )
  {
    text("The output is stereo.", 5, 30);
  }  
  else
  {
    text("The output has " + out.type() + " channels, which is totally not allowed.", 5, 45);
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
