//function have 3 parts
// return type
// 
//
//void setup> draw> custom fn
//
//arguments eg, rgb h w 
//parameter is fn passing certain value to the arguments


//example

//void draw(){
//  drawPinkCircle(40,40,12,color(0,138,253);
//}
//
//void drawPinkCircle(int circX,int circY, int diameter,color c){
//  noStroke;
//  fill(c);
//  ellipse(circX,circY,diameter,diameter);
//}
  
  
//setup never loop
//draw always loop
//
// wanna change the flow? try no loop



void setup(){
  size(500,500);
  smooth();
}

void draw(){
  background(0);
  drawCircle(20,20,32,color(250,0,119));
  drawCircle(80,80,80,color(0,130,255));
  drawCircle(mouseX,mouseY,40,color(150));
}

void drawCircle(int circX,int circY,int circdia,color c){
  noStroke();
  fill(c);
  ellipse(circX,circY,circdia,circdia);
}
