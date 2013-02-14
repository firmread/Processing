void setup(){
  size(500,500);
  smooth();
  
}

void draw(){
  background(255);
  for(int i=0; i<width ;i++){
    drawCircle(i*i,i*i,i*i,color(255,0,119));
  }
}

void drawCircle (int circX,int circY,int circD, color c){
  noFill();
  stroke(c);
  ellipse(circX,circY,circD,circD);
}
