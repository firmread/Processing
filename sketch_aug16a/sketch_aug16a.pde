



void setup(){
  size(1350,500);
  background(0);
  smooth();

}




/*
drawing between sky and earth
gradient background changing dramatically,, time-based
or using something else to modulate time

define depth and space of the screen
*/



int alpha = 200;
void draw() {
  //making delays with fill alpha
  noStroke();
  
  //fill(255);
  fill(229,219,184,alpha);
  rect(0,0,width*2,height*2);

  
  //uppershape
  //  float r1 = map(mouseY,0,height,61,170);
  //  float g1 = map(mouseY,0,height,90,191);
  //  float b1 = map(mouseY,0,height,115,209);
  fill(255,211,33,alpha);
  beginShape();
  float x1 = map(mouseX,0,width,-21,490);
  vertex(x1, 0);
  float x2 = map(mouseX,0,width,136,545);
  float y2 = map(mouseX,0,width,141,70);
  vertex(x2, y2);
  float x3 = map(mouseX,0,width,792,1200);
  float y3 = map(mouseX,0,width,70,140);
  vertex(x3, y3);
  float x4 = map(mouseX,0,width,846,1360);
  vertex(x4, 0);
  float x5 = map(mouseX,0,width,1083,1639);
  vertex(x5, 0);
  float x6 = map(mouseX,0,width,978,1441);
  float y6 = map(mouseX,0,width,64,212);
  vertex(x6, y6);
  float x7 = map(mouseX,0,width,-104,360);
  float y7 = map(mouseX,0,width,212,64);
  vertex(x7,y7);
  float x8 = map(mouseX,0,width,-300,287);
  float y8 = map(mouseX,0,width,-15,0);
  vertex(x8,y8);
  endShape(CLOSE);
  
  //lowershape
  //  float r2 = map(mouseY,0,height,115,175);
  //  float g2 = map(mouseY,0,height,85,135);
  //  float b2 = map(mouseY,0,height,60,101);
  fill(0,alpha);
  beginShape();
  float x9 = map(mouseX,0,width,180,-350);
  vertex(x9, 500);
  float x10 = map(mouseX,0,width,266,-78);
  float y10 = map(mouseX,0,width,413,295);
  vertex(x10, y10);
  float x11 = map(mouseX,0,width,1415,1070);
  float y11 = map(mouseX,0,width,294,414);
  vertex(x11,y11);
  float x12 = map(mouseX,0,width,1685,1158);
  vertex(x12, 500);
  float x13 = map(mouseX,0,width,1338,865);
  vertex(x13, 500);
  float x14 = map(mouseX,0,width,1183,813);
  float y14 = map(mouseX,0,width,358,445);
  vertex(x14 ,y14);
  float x15 = map(mouseX,0,width,524,154);
  float y15 = map(mouseX,0,width,445,358);
  vertex(x15,y15);
  float x16 = map(mouseX,0,width,471,0);
  vertex(x16,500);
  endShape(CLOSE);
  
  //define brush size
  int siz = second()%10 * 20;
  rectMode(CENTER);
  fill(155,28,8);
  rect(mouseX,mouseY,siz,siz);
}


