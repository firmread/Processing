import processing.core.*; 
import processing.xml.*; 

import processing.serial.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class oct14a_arduinoSerialCallResASC extends PApplet {

//Processing code to run with this example:

// This example code is in the public domain.

     // import the Processing serial library
Serial myPort;                  // The serial port

float bgcolor;			// Background color
float fgcolor;			// Fill color
float xpos, ypos;	        // Starting position of the ball

public void setup() {
  size(640,480);

  // List all the available serial ports
  println(Serial.list());

  // I know that the first port in the serial list on my mac
  // is always my  Arduino module, so I open Serial.list()[0].
  // Change the 0 to the appropriate number of the serial port
  // that your microcontroller is attached to.
  myPort = new Serial(this, Serial.list()[0], 9600);

  // read bytes into a buffer until you get a linefeed (ASCII 10):
  myPort.bufferUntil('\n');
  
  // draw with smooth edges:
  smooth();
}

public void draw() {
  background(bgcolor);
  fill(fgcolor);
  // Draw the shape
  ellipse(xpos, ypos, 20, 20);
}

// serialEvent  method is run automatically by the Processing applet
// whenever the buffer reaches the  byte value set in the bufferUntil() 
// method in the setup():

public void serialEvent(Serial myPort) { 
  // read the serial buffer:
  String myString = myPort.readStringUntil('\n');
  // if you got any bytes other than the linefeed:
    myString = trim(myString);
 
    // split the string at the commas
    // and convert the sections into integers:
    int sensors[] = PApplet.parseInt(split(myString, ','));

    // print out the values you got:
    for (int sensorNum = 0; sensorNum < sensors.length; sensorNum++) {
      print("Sensor " + sensorNum + ": " + sensors[sensorNum] + "\t"); 
    }
    // add a linefeed after all the sensor values are printed:
    println();
    if (sensors.length > 1) {
      xpos = map(sensors[0], 0,255,0,width);
      ypos = map(sensors[1], 0,255,0,height);
      fgcolor = sensors[2];
    }
    // send a byte to ask for more data:
    myPort.write("A");
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "oct14a_arduinoSerialCallResASC" });
  }
}
