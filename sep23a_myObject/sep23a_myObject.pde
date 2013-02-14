Ball myBall;


void setup(){
  size(300,700);
  smooth();

  color temp = color (59,166,245,50);


  myBall = new Ball(width/2, height/2, 40, 3, 1.2, temp );

  background(255);
}

void draw(){

  myBall.updateBall();
  myBall.drawBall();
}
