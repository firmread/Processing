// modulus % = 'sade' in dividing 2 no.
// boolean - true or false

//variable
//int a;
//(datatype name)

//int x;
//float y;
//boolean b;

//x=50;


//can't declare things with the same name twice
//logical
//&& and
//|| or
//! not

// ! (not) can only apply to the boolean

void setup() {
  size(800, 400);
  noFill();
  smooth();
  frameRate(10);
}

void draw() {
  background(255, 0, 127);
  line(width/2, 0, width/2, height);
  line(0,height/2,width,height/2);
  
  fill(255);
  float posX=random(width);
  float posY=random(height);
  float v=random(10,200);
  if((mouseX<width/2)&&(mouseY<height/2)){
    ellipse(width/2,height/2,v,v);
  }
  else{
    rect(posX,posY,50,50);
  }
}

//random can always got only float
