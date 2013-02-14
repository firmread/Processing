/**
  * This sketch demonstrates how to use the <code>isSounding</code> method of a <code>Polyphonic</code> class. 
  * Currently the only <code>Polyphonic</code> class in Minim is <code>AudioOutput</code>. 
  * This sketch adds four sine waves to the output and you can disable/enable them by pressing '1', '2', '3', and '4'. 
  * Text will be displayed on the screen stating whether or not the output is making sound.
  */

import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioOutput out;
SineWave sine200;
SineWave sine400;
SineWave sine450;
SineWave sine1200;
WaveformRenderer waveform;

void setup()
{
  size(512, 200, P3D);

  minim = new Minim(this);
  // this gets a stereo output
  out = minim.getLineOut();
    
  waveform = new WaveformRenderer();
  // see the example Recordable >> addListener for more about this
  out.addListener(waveform);
  
  // see the example AudioOutput >> SineWaveSignal for more about these
  sine200 = new SineWave(200, 0.2, out.sampleRate());
  sine400 = new SineWave(400, 0.2, out.sampleRate());
  sine450 = new SineWave(450, 0.2, out.sampleRate());
  sine1200 = new SineWave(1200, 0.2, out.sampleRate());
  // add the signal to out
  out.addSignal(sine200);
  out.addSignal(sine400);
  out.addSignal(sine450);
  out.addSignal(sine1200);
  
  textFont(createFont("Arial", 12));
  textMode(SCREEN);
}

void draw()
{
  background(0);
  // see waveform.pde for an explanation of how this works
  waveform.draw();
  
  if ( out.isSounding() )
  {
    text("The output is making sound.", 5, 15);
  }
  else
  {
    text("The output is not making sound.", 5, 15);
  }
}

void keyPressed()
{
  if ( key == '1' ) 
  {
    if ( out.isEnabled(sine200) )
    {
      out.disableSignal(sine200);
    }
    else
    {
      out.enableSignal(sine200);
    }
  }
  if ( key == '2' ) 
  {
    if ( out.isEnabled(sine400) )
    {
      out.disableSignal(sine400);
    }
    else
    {
      out.enableSignal(sine400);
    }
  }
  if ( key == '3' ) 
  {
    if ( out.isEnabled(sine450) )
    {
      out.disableSignal(sine450);
    }
    else
    {
      out.enableSignal(sine450);
    }
  }
  if ( key == '4' ) 
  {
    if ( out.isEnabled(sine1200) )
    {
      out.disableSignal(sine1200);
    }
    else
    {
      out.enableSignal(sine1200);
    }
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
