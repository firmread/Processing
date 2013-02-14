/**
  * This sketch is an example of how to use the <code>get</code> method of an <code>AudioBuffer</code> to get the 
  * value of a sample in one of an <code>AudioSource</code>'s sample buffers. The classes in Minim that extend <code>AudioSource</code> 
  * and therefore inherit the <code>left</code>, <code>right</code>, and <code>mix</code> buffers of that class, are 
  * <code>AudioInput</code>, <code>AudioOutput</code>, <code>AudioSample</code>, and <code>AudioPlayer</code>. 
  * Not coincidentally, these are also all of the classes in Minim that are <code>Recordable</code>. 
  * <p>
  * The value returned by <code>get</code> will always be between -1 and 1, unless you are using an <code>AudioOutput</code>
  * whose signals mix together to produce sample values outside of this range. If that is the case you will notice 
  * it right away because the audio will sound distorted. You can use <code>get</code> to draw the waveform of the 
  * audio in an <code>AudioBuffer</code>, but it is not the best choice for doing so. See the toArray example for more 
  * about this.
  */

import ddf.minim.*;

Minim minim;
AudioPlayer groove;

void setup()
{
  size(512, 200, P3D);
  minim = new Minim(this);
  groove = minim.loadFile("groove.mp3");
  groove.loop();
}

void draw()
{
  background(0);
  stroke(255);
  // we multiply the values returned by get by 50 so we can see the waveform
  for ( int i = 0; i < groove.bufferSize() - 1; i++ )
  {
    float x1 = map(i, 0, groove.bufferSize(), 0, width);
    float x2 = map(i+1, 0, groove.bufferSize(), 0, width);
    line(x1, height/4 - groove.left.get(i)*50, x2, height/4 - groove.left.get(i+1)*50);
    line(x1, 3*height/4 - groove.right.get(i)*50, x2, 3*height/4 - groove.right.get(i+1)*50);
  }
}

void stop()
{
  // always close Minim audio classes when you finish with them
  groove.close();
  // always stop Minim before exiting
  minim.stop();

  super.stop();
}
