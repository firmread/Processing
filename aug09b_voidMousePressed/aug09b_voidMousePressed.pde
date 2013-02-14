void setup(){
  size(500,500);
  smooth();
}
//
////MAP FUNCTION = scale the data!!!
//void draw(){
//  float r=map(second(),0,60,0,255);
//  float g=map(minute(),0,60,0,255);
//  float b=map(hour(),0,60,0,255);
//  background(r,g,b);
//  println(g);
//  
//}

//// MOUSE INTERACTIVITY + MAP FN
//void draw(){
//  background(150);
//  float rx=map(mouseX,0,width,0,255);
//  float gy=map(mouseY,0,height,0,255);
//  fill(rx,gy,250);
//  noStroke();
//  ellipse(width/2,height/2,width,height);
//  println(mouseX + " " + "my mouseY coordinates are"+ mouseY);
//}

////MOUSE PRESSED IS TRUE/FALSE 
//void draw(){
// if(mousePressed == true) {
//   fill(0,200,255);
// }
// else{
//   fill(255,0,0);
// }
// ellipseMode(CORNER);
// ellipse(0,0,width,height);
// println(mousePressed);
//}

//mousePressed void for another layer of interaction
void draw(){
  fill(random(150,255),random(30,100),30,random(0,200));
  triangle(mouseX-random(5,50),mouseY-random(5,50),mouseX-random(5,50),mouseY+random(5,50),mouseX+random(5,50),mouseY+random(5,50));
}

void mousePressed(){
 if(mouseButton == LEFT) {
   fill(0,200,255);
 }
 else{
   fill(255,0,200);// try right click
 }
 ellipseMode(CORNER);
 ellipse(0,0,width,height);
}


