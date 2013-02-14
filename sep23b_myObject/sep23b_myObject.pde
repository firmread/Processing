//Ball myBall;
Ball balls[] = new Ball[10];

void setup(){
  size(300,700);
  smooth();
  
  for(int i =0; i<balls.length; i++){
    color tempC = color (round (random(100,255)),round (random(100,155)),round (random(80,200)),50);
    int tempW = round (random(0,width));
    int tempH = round (random(0,height));
    int tempR = round (random(20,60));
    float tempSX = random(2,4);
    float tempSY = random(2,4);
    
    balls[i] = new Ball(tempW,tempH,tempR,tempSX,tempSY,tempC);

  }
//  myBall = new Ball(width/2, height/2, 40, 3, 1.2, temp );
  
  background(255);
}

void draw(){
  
//  myBall.updateBall();
//  myBall.drawBall();  

  for (int i= 0 ; i<balls.length; i++){
    balls[i].updateBall();
    balls[i].drawBall();
}
}
