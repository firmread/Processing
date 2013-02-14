int blacksq = 250;

//,,,,,,,Fullscreen
import fullscreen.*; 
FullScreen fs; 

//,,,,,,,Movie
import codeanticode.gsvideo.*;
GSMovie movie;

//,,,,,,,Serial
import processing.serial.*;     // import the Processing serial library
Serial myPort;                  // The serial port

float bgcolor;			// Background color
float fgcolor;			// Fill color
float wtr1, wtr2;	        // Starting position of the ball

//,,,,,,,,Sound
import ddf.minim.*;

Minim minim;
AudioInput in;

float soundVolume;

//,,,,,,,,boolean
boolean startboolean,slide1,slide2,slide3,
        slide4,slide5,slide6,slide7,slide8,
        slide9,slide10,slide11,slide12,slide13,
        slide14,slide15,slide16;
//,,,,,,,,Image
PImage p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p13bg,p13tx,p14,p15,p16;


void setup() {
  size (680,510);
  //(1024,544); 
  //(1024,768);
  //(512, 384);
  //(480,360);
  //
  //(640,480);
  //(512, 272);
  
  background(0);
  smooth();
  frameRate(22);
  
  //,,,,,,,Fullscreen
  // Create the fullscreen object
  fs = new FullScreen(this); 
  // enter fullscreen mode
  fs.enter(); 
  fs.setResolution(width, height);
  //cmd+f = fullscreen on/off
  fs.setShortcutsEnabled(true);
  
  
  //,,,,,,,Movie
  // Load and play the video in a loop
  movie = new GSMovie(this, "bmwater.mov");
  //1024768
  movie.loop();
  
  //,,,,,,,,Serial
  println(Serial.list());
  myPort = new Serial(this, Serial.list()[0], 9600);
  myPort.bufferUntil('\n');
  
  //,,,,,,,,Sound
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 2048);
  
  //,,,,,,,,Image
  p1 = loadImage("1.jpg");
  p2 = loadImage("2.jpg");
  p3 = loadImage("3.jpg");
  p4 = loadImage("4.jpg");
  p5 = loadImage("5.jpg");
  p6 = loadImage("6.jpg");
  p7 = loadImage("7.jpg");
  p8 = loadImage("8.jpg");
  p9 = loadImage("9.jpg");
  p10 = loadImage("10.jpg");
  p11 = loadImage("11.jpg");
  p12 = loadImage("12.jpg");
  p13 = loadImage("13.jpg");
  p14 = loadImage("14.jpg");
  p15 = loadImage("15.jpg");
  p16 = loadImage("16.jpg");
  
  
  slide1 = true;
}

void movieEvent(GSMovie movie) {
  movie.read();
}

void draw() {
  noStroke();
  //,,,,,,,Movie
  tint(255, 250);
  image(movie, 0, 0,movie.width*2,movie.height*2);
  //,screen.width,screen.height);
  //768*1024/544,768);
  //,width,height
  //1024,768
  
  //fill(0);
  //rectMode(CENTER);
  //rect(width/2,height/2,blacksq,blacksq);
  
  
  if (slide1){
    movie.speed(0.5);
    movie.volume(0.5);
    imageMode(CENTER);
    image(p1,width/2,height/2,blacksq,blacksq); 
    if(wtr1>0 && wtr2>0 || key=='a'){
      for(int i=0; i<255 ; i++){
        tint(255-i,255);
        delay(20);
      }
      slide1 = false;
      slide2 = true;
    }
  }
  
  if (slide2){
    movie.speed(0.6);
    movie.volume(0.6);
    imageMode(CENTER);
    image(p2,width/2,height/2,blacksq,blacksq); 
    if(wtr1>0 && wtr2>0){
      slide2 = false;
      slide3 = true;
    }
  }
  if (slide3){
    movie.speed(0.7);
    movie.volume(0.7);
    imageMode(CENTER);
    image(p3,width/2,height/2,blacksq,blacksq);
    if(wtr1>0 && wtr2>0){
      slide3 = false;
      slide4 = true;
    }
  }
}

void serialEvent(Serial myPort) { 
  // read the serial buffer:
  String myString = myPort.readStringUntil('\n');
  // if you got any bytes other than the linefeed:
  myString = trim(myString);
 
  // split the string at the commas
  // and convert the sections into integers:
  int sensors[] = int(split(myString, ','));

  // print out the values you got:
  for (int sensorNum = 0; sensorNum < sensors.length; sensorNum++) {
    print("Sensor " + sensorNum + ": " + sensors[sensorNum] + "\t"); 
  }
  // add a linefeed after all the sensor values are printed:
  println();
  if (sensors.length > 1) {
    wtr1 = map(sensors[0], 0,10,0,255);
    wtr2 = map(sensors[1], 0,6,0,20);
    fgcolor = sensors[2];
  }
  // send a byte to ask for more data:
  myPort.write("A");
}
