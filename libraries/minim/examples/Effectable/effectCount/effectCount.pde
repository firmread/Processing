/**
  * This sketch demonstrates how to use the <code>effectCount</code> method of an <code>Effectable</code> class. 
  * The class used here is <code>AudioOutput</code>, but you can also get the effect count of <code>AudioInput</code>, 
  * <code>AudioPlayer</code>, and <code>AudioSample</code> objects. The effect count is simply how many effects 
  * are currently attached to the <code>Effectable</code>.
  */

import ddf.minim.*;
import ddf.minim.effects.*;

Minim minim;
AudioOutput out;
LowPassFS lpf1;
LowPassFS lpf2;
LowPassFS lpf3;
LowPassFS lpf4;

void setup()
{
  size(512, 200, P3D);
  textMode(SCREEN);
  textFont(createFont("Courier", 12));

  minim = new Minim(this);
  
  out = minim.getLineOut();
  
  // see the example AudioEffect >> LowPassFSFilter for more about this effect
  lpf1 = new LowPassFS(3000, out.sampleRate());
  lpf2 = new LowPassFS(2000, out.sampleRate());
  lpf3 = new LowPassFS(1000, out.sampleRate());
  lpf4 = new LowPassFS(500,  out.sampleRate());
  // add the effects to the player
  out.addEffect(lpf1);
  out.addEffect(lpf2);
  out.addEffect(lpf3);
  out.addEffect(lpf4);
}

void draw()
{
  background(0);
  fill(255);
  text("The output has " + out.effectCount() + " effects attached to it.", 5, 15);
}

void stop()
{
  // always close Minim audio classes when you are done with them
  out.close();
  minim.stop();
  
  super.stop();
}
