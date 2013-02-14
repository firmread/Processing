

//declaring variable
float circlex=0;
float circley=0;
float vely = 10;
float velx = 10;
float g = 1;
float x;
float y;

void setup(){
  background(0);
  smooth();
  frameRate(60);
  size(1000,180);
}

void draw(){

  fill(random(150,200),random(150,200),0,random(100,255));
  //noStroke();
  strokeWeight(1);
  circlex+=velx;
  vely+=g;
  circley+=vely;
   // the same as circley=circley+vel
  ellipse(x,y,random(40,50),random(40,50));
  if((circlex<0)||(circlex>width)){
    velx=velx*-1;
  }
  if((circley<0)||(circley>height)){
    vely=vely*-1;
  }
  if (mousePressed && (mouseButton == LEFT)) {
    x=(mouseX);
  } 
  else {
    x=circlex;
  }
  if (mousePressed && (mouseButton == LEFT)) {
    y=(mouseY);
  } 
  else {
    y=circley;
  }
  
}
   //same as vel=vel
   //inserting picture
   
