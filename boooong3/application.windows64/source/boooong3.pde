int blacksq = 220;
int shoutsq;

//,,,,,,,Fullscreen
import fullscreen.*; 
FullScreen fs; 

//,,,,,,,Movie
import codeanticode.gsvideo.*;
GSMovie bmwater,floodslide;
//,,,,,,,blackSQ
int sqalpha=240;
int frameNow;
int bmtint = 255;

//,,,,,,,Serial
import processing.serial.*;     // import the Processing serial library
Serial myPort;                  // The serial port

float bgcolor;			// Background color
float fgcolor;			// Fill color
float wtr1, wtr2;	        // Starting position of the ball

//,,,,,,,,Sound
import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioInput in;
AudioPlayer drown1,drown2;

float soundVolume;
float soundscale = 450;


//,,,,,,,,boolean
boolean startboolean,slide1,slide2,slide3,
        slide4,slide5,slide6,slide7,slide8,
        slide9,slide10,slide11,slide12,slide13,
        slide14,slide15,slide16,endboolean;
        






void setup() {
  size (680,510);
  
  background(0);
  smooth();
  frameRate(5);
  
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
  bmwater = new GSMovie(this, "bmwaterloopsmall.mov");
  bmwater.loop();
  floodslide = new GSMovie(this, "floodslide.mov");
  floodslide.play();
  
  shoutsq = 200;
  
  //,,,,,,,,Serial
  println(Serial.list());
  myPort = new Serial(this, Serial.list()[0], 9600);
  myPort.bufferUntil('\n');
  
  //,,,,,,,,Sound
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 2048);
  drown1 = minim.loadFile("523693_SOUNDDOGS__un.mp3");
  drown2 = minim.loadFile("523694_SOUNDDOGS__un.mp3");
  
  startboolean = true;
  //slide13 = true;
}

void movieEvent(GSMovie movie) {
  movie.read();
}






  
  
void draw() {
  noStroke();
  
  //,,,,,,,Movie
  tint(255, 250);
  
  imageMode(CENTER);
  tint(bmtint,250);
  image(bmwater, width/2, height/2,width,height);
  
  tint(255, 250);
  image(floodslide, width/2, height/2,blacksq,blacksq);
  frameNow = floodslide.frame();

  soundVolume = in.left.level()*soundscale;
  
  if (startboolean){
    bmwater.speed(0.52);
    bmwater.volume(0.52);
    if (frameNow > 60){ 
    //> 45 && frameNow < 60){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        startboolean = false;
        slide1 = true;
        floodslide.play();
      }
    }
  }  
  
  if (slide1){
    bmwater.speed(0.58);
    bmwater.volume(0.58);
    if (frameNow > 150){
    //&& frameNow < 150){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide1 = false;
        slide2 = true;
        floodslide.play();
      }
    }
  }
  
  if (slide2){
    bmwater.speed(0.6);
    bmwater.volume(0.6);
    if(frameNow > 240){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide2 = false;
        slide3 = true;
        floodslide.play();
      }
    }
  }
  if (slide3){
    bmwater.speed(0.62);
    bmwater.volume(0.62);
    if(frameNow > 330){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide3 = false;
        slide4 = true;
        floodslide.play();
      }
    }
  }
  if(slide4){
    bmwater.speed(0.64);
    bmwater.volume(0.64);
    if(frameNow > 420){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide4 = false;
        slide5 = true;
        floodslide.play();
      }
    }
  }
  if(slide5){
    bmwater.speed(0.66);
    bmwater.volume(0.66);
    if(frameNow > 510){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide5 = false;
        slide6 = true;
        floodslide.play();
      }
    }
  }
  if(slide6){
    bmwater.speed(0.68);
    bmwater.volume(0.68);
    if(frameNow > 600){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide6 = false;
        slide7 = true;
        floodslide.play();
      }
    }
  }
  if(slide7){
    bmwater.speed(0.7);
    bmwater.volume(0.7);
    if(frameNow > 690){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide7 = false;
        slide8 = true;
        floodslide.play();
      }
    }
  }
  if(slide8){
    bmwater.speed(0.72);
    bmwater.volume(0.72);
    if(frameNow > 780){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide8 = false;
        slide9 = true;
        floodslide.play();
      }
    }
  }
  if(slide9){
    bmwater.speed(0.74);
    bmwater.volume(0.74);
    if(frameNow > 870){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide9 = false;
        slide10 = true;
        floodslide.play();
      }
    }
  }
  if(slide10){
    bmwater.speed(0.76);
    bmwater.volume(0.76);
    if(frameNow > 960){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide10 = false;
        slide11 = true;
        floodslide.play();
      }
    }
  }  
  if(slide11){
    bmwater.speed(0.78);
    bmwater.volume(0.78);
    if(frameNow > 1050){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide11 = false;
        slide12 = true;
        floodslide.play();
      }
    }
  }  
  if(slide12){
    bmwater.speed(0.8);
    bmwater.volume(0.8);
    if(frameNow > 1140){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide12 = false;
        slide13 = true;
        floodslide.play();
      }
    }
  }
  if(slide13){
    bmwater.speed(1);  
    bmwater.volume(1);
    //floodslide.play();
    floodslide.jump(1220);
    drown1.loop();
    drown2.loop();
    
    //if(frameNow > 1230){
      floodslide.pause();

      if (soundVolume <100 && shoutsq > 10){
        shoutsq -=3;
      }
      if (soundVolume <200 && shoutsq >10){
        shoutsq -=2;
      }
      if (soundVolume > 100 ){
        if( shoutsq < 300){
          shoutsq += 2;
        }
        if( shoutsq < 600){
          shoutsq += 1;
        }
      }
      if (soundVolume > 300 ){
        if(shoutsq < 300){
          shoutsq += 3;
        }
        if(shoutsq < 600){
          shoutsq += 2;
        }
        if(shoutsq <1000){
          shoutsq += 1;
        }
      }
      if (wtr1>0 || wtr2>0){
        shoutsq += 2;
      }
      if (key == 'w'){
        shoutsq += 5;
      }    
      if(shoutsq>680 || key=='d'){
        
        slide13 = false;
        slide14 = true;
        floodslide.play();
        blacksq = 220;
        drown1.close();
        drown2.close();

      }
    //}
  }
  if(slide14){
    bmwater.speed(0.8);
    bmwater.volume(0.8);
    if(frameNow > 1320){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide14 = false;
        slide15 = true;
        floodslide.play();
      }
    }
  }
  if(slide15){
    bmwater.speed(0.8);
    bmwater.volume(0.8);
    if(frameNow > 1410){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='a'){
        slide15 = false;
        slide16 = true;
        floodslide.play();
      }
    }
  }
  if(slide16){
    bmwater.speed(0.8);
    bmwater.volume(0.8);
    if(frameNow > 1500){
      floodslide.pause();
      if(wtr1>0 || wtr2>0 || key=='s'){
        slide16 = false;
        endboolean = true;
        floodslide.play();
      }
    }
  }
  if(endboolean){
    bmwater.speed(0.8);
    bmwater.volume(0.8);
    if(frameNow > 1525  || key =='d'){
      floodslide.goToBeginning();
      endboolean = false;
      startboolean = true;
    }
  }
  
  rectMode(CENTER);
  fill(0,150);
  rect(width/2,height/2,shoutsq,shoutsq);
  
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



void stop()
{
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();
  
  super.stop();
}
