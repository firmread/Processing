class Ball{
  //VARIABLE OF THE CLASS BALL
  int x,y,r;
  float speedX,speedY;
  color ballColor;
  
  //CONSTRUCTOR
  /*underscore use to says that the integer 
  *would be passed from some where ease*/
  Ball(int _x, int _y, int _r, float _sx, float _sy, color _c){
    this.x = _x;
    //the x of THIS OBJECT
    //not x in the other object,,,
    this.y = _y;
    this.r = _r;
    
    this.speedX = _sx;
    this.speedY = _sy;
    this.ballColor = _c;
    
  }
  
  //method
  void drawBall(){
    
    strokeWeight(2);
    stroke(0);
    fill(this.ballColor);
    ellipse(this.x,this.y,this.r,this.r);
    
  }
  
  void updateBall(){    
    if(this.x >= width - this.r/2 || this.x <= 0 + this.r/2) 
      //this.speedX += random(1.2,2.4);
      this.speedX *= -1;
    if(this.y >= height - this.r/2 || this.y <= 0 + this.r/2)
      //this.speedY += random(1.2,2.4);
      this.speedY *= -1;      
    //this.x++ also work but speedX useful to change speed
    
    this.x += speedX;
    this.y += speedY;
    

    
  
  
  }
}
