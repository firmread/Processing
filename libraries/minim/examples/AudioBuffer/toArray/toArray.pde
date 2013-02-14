/**
  * This sketch is an example of how to use the <code>toArray</code> method of an <code>AudioBuffer</code> to get a 
  * copy of all the samples in one of an <code>AudioSource</code>'s sample buffers. The classes in Minim that extend <code>AudioSource</code> 
  * and therefore inherit the <code>left</code>, <code>right</code>, and <code>mix</code> buffers of that class, are 
  * <code>AudioInput</code>, <code>AudioOutput</code>, <code>AudioSample</code>, and <code>AudioPlayer</code>. 
  * Not coincidentally, these are also all of the classes in Minim that are <code>Recordable</code>. 
  * <p>
  * The float array returned by <code>toArray</code> will always be the same length as the buffer's size. The values in 
  * the array will always be between -1 and 1, unless you are using an <code>AudioOutput</code>
  * whose signals mix together to produce sample values outside of this range. If that is the case you will notice 
  * it right away because the audio will sound distorted. You can use <code>toArray</code> to draw the waveform of the 
  * audio in an <code>AudioBuffer</code> and it is the preferred method for doing so. The reason for this is due to 
  * threading. The actual audio I/O happens in its own thread and calls back into the main thread (your sketch) when it 
  * has a new buffer of samples. Because of this, when using <code>get</code> to draw the waveform, it is possible 
  * (in fact highly likely) that the samples in the buffer will be changed while you are in the middle of drawing the 
  * waveform, which will result in a waveform that seems to have discontinuities. When you use <code>toArray</code> you 
  * are given a copy of the current contents of the buffer and it is guaranteed, thanks to synchronization, that 
  * the entire array is created without the samples changing in the process.
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
  float[] left = groove.left.toArray();
  float[] right = groove.right.toArray();
  // we only loop to left.length - 1 because we are accessing index i+1 in the loop
  for ( int i = 0; i < left.length - 1; i++ )
  {
    float x1 = map(i, 0, groove.bufferSize(), 0, width);
    float x2 = map(i+1, 0, groove.bufferSize(), 0, width);
    // we multiply the values returned by get by 50 so we can see the waveform
    line(x1, height/4 - left[i]*50, x2, height/4 - left[i+1]*50);
    line(x1, 3*height/4 - right[i]*50, x2, 3*height/4 - right[i+1]*50);
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
