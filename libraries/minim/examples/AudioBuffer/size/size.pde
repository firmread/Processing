/**
  * This sketch is an example of how to use the <code>size</code> method of an <code>AudioBuffer</code> to get the 
  * size of one of an <code>AudioSource</code>'s sample buffers. The classes in Minim that extend <code>AudioSource</code> 
  * and therefore inherit the <code>left</code>, <code>right</code>, and <code>mix</code> buffers of that class, are 
  * <code>AudioInput</code>, <code>AudioOutput</code>, <code>AudioSample</code>, and <code>AudioPlayer</code>. 
  * Not coincidentally, these are also all of the classes in Minim that are <code>Recordable</code>. 
  * <p>
  * The value returned by <code>size</code> is the number of samples stored in the buffer. It will always be 
  * equal to the <code>bufferSize()</code> of the <code>Recordable</code> you are working with. It is often a value 
  * that you specify when you create the <code>Recordable</code> such as with <code>loadFile</code> or <code>getLineIn</code>.
  */

import ddf.minim.*;

Minim minim;
AudioOutput out;

void setup()
{
  size(350, 100, P3D);
  textMode(SCREEN);
  minim = new Minim(this);
  out = minim.getLineOut();
  textFont(loadFont("courier12.vlw"));
}

void draw()
{
  background(0); 
  text("The size of the left buffer is " + out.left.size() + " samples.", 5, 15);
  text("The size of the right buffer is " + out.right.size() + " samples.", 5, 30);
  text("The size of the mix buffer is " + out.mix.size() + " samples.", 5, 45);
  text("The buffer size of the player is " + out.bufferSize() + " samples.", 5, 60);
}

void stop()
{
  // always close Minim audio classes when you finish with them
  out.close();
  // always stop Minim before exiting
  minim.stop();
  
  super.stop();
}
