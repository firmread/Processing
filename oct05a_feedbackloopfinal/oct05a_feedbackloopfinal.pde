PFont nofont,txtfont;
int work,live,sleep;

int hr = hour();
int mi = minute();

void setup(){
  size(480,320);
  smooth();
  nofont = loadFont("HelveticaNeueLT-UltraLight-48.vlw"); 
  txtfont = loadFont("MyriadPro-Black-17.vlw");
}

void draw(){
  
  background(0);
  
  //blue circle
  fill(49,172,206);  
  noStroke();
  ellipse(width/2,height/2,height*.7,height*.7);
  fill(0);
  ellipse(width/2,height/2,height*.7*2/3,height*.7*2/3);
  
  //clock bar
  if(second()%2 == 0){
    stroke(2,73,89,150);
    fill(2,73,89,150);
    strokeWeight(2);
  }
  else{
    noStroke();
    fill(2,72,115,150);
  }
  rectMode(CENTER);
  rect(width/2,height*.92,
  (width/144*((hr*6)+(mi/10))),height*.075);
  
  noStroke();
  
  //triangle variables
  int work=75;
  int sleep=75;
  int live=75;
  
  
  
  //triangle
  
  //triangle(width/2,height/2-work,
  //width/2-live*cos(radians(30)), height/2+live*sin(radians(30)),
  //width/2+sleep*cos(radians(30)), height/2+sleep*sin(radians(30)));
  beginShape();
  //work
  //fill(127,185,62);
  vertex(width/2,height/2-work);
  //live
  fill(17,73,110);
  vertex(width/2-live*cos(radians(30)), height/2+live*sin(radians(30)));
  //sleep
  //fill(2,73,89);
  vertex(width/2+sleep*cos(radians(30)), height/2+sleep*sin(radians(30)));
  endShape();
  
  //circle grid
  fill(0,0);
  strokeWeight(0);
  stroke(255,100);
  ellipse(width/2,height/2,height*.7/3,height*.7/3);
  ellipse(width/2,height/2,height*.7*2/3,height*.7*2/3);
  ellipse(width/2,height/2,height*.7-1,height*.7-1);
  stroke(100,100);
  ellipse(width/2,height/2,height*.7*4/3,height*.7*4/3);
  stroke(80,100);
  ellipse(width/2,height/2,height*.7*5/3,height*.7*5/3);
  stroke(50,100);
  ellipse(width/2,height/2,height*.7*6/3,height*.7*6/3);
  stroke(30,100);
  ellipse(width/2,height/2,height*.7*7/3,height*.7*7/3);
  
  //radial grid
  stroke(100);
  line(width/2,height/2,width/2,0);
  line(width/2,height/2,0,height-height/15.238);
  line(width/2,height/2,width,height-height/15.238);
  
  // TEXT
  textAlign(LEFT);
  //text styling
  fill(255,200);
  
  //txtfont
  textFont(txtfont);
  text("day",25,29);
  
  //notext
  textAlign(LEFT);
  Date OldDate = new Date("08/09/1986"); // M/D/Y
  Date TodaysDate = new Date();
  long mills_per_day = 1000 * 60 * 60 * 24;
  long day_diff = ( TodaysDate.getTime() - OldDate.getTime() ) / mills_per_day;
  textFont(nofont);
  text(round(day_diff), 22, 70);
  
  //clock text
  textFont(nofont,19);
  textAlign(CENTER);
  text(hour()+":"+minute()+":"+second(),width/2,height*.94);
  
  //txtfont
  textFont(txtfont);
  textAlign(CENTER);
  fill(17,73,110);
  text("work",width/2,35);

  pushMatrix();
    translate(128,225);
    rotate(radians(-30));
    text("live",0,0);
  popMatrix();
  
  pushMatrix();
    translate(width-125,225);
    rotate(radians(30));
    text("sleep",0,0);
  popMatrix();
}


