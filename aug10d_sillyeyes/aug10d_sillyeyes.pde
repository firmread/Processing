void setup(){
  size(400,400);
  noStroke();
  smooth();
}

void draw(){
  background(121,181,200);
  crazyEye(140,200);
  crazyEye(250,200);
}

void crazyEye(int circX, int circY){
  noStroke();
  fill(255);
  ellipse(circX,circY,60,60);
  
  fill(0);
  ellipse(circX+10,circY,30,30);
  
  fill(255);
  ellipse(circX+16,circY-5,6,6);
  
  strokeWeight(5);
  stroke(0);
  noFill();
  ellipse(circX,circY,5,5);
}
