// mouse drawing
// background is on the back not being render while mouse move
//void setup (){
//  size(500,500);
//  background(255,217,0);
//  noStroke();
//
//}
//void draw(){
//  rect(mouseX,mouseY,10,10);
//}


//mouse interaction
//background are being drawn as the rect move
void setup (){
  size(500,500);
  frameRate(60);
  noStroke();

}
void draw(){
  background(255,217,0);
  fill(0);
  triangle(mouseX,mouseY-30,mouseX-15,mouseY+15,mouseX+15,mouseY+15);
  fill(255);
  rect(mouseX,mouseY,30,30);
  fill(20,20,20,20);
  ellipse(mouseX+30,mouseY+30,20,20);
}
