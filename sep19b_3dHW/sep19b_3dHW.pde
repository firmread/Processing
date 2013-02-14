//RGBCube for gradient space
//Light2 for lighting
float getMouseX,getMouseY;

void setup() 
{ 
  size(500, 500, P3D); 
  colorMode(RGB, 1); 
} 


void draw() 
{ 
  background(0);  
  pushMatrix(); 
  translate(width/2, height/2, -height/2); 
  scale(height/20);
  rotateY(radians(returnMouseX())); 
  rotateX(radians(returnMouseY()));
  
  getMouseX=map(mouseX,0,width,-.9,.9);
  getMouseY=map(mouseY,0,height,.9,-.9);
    // Yellow spotlight from the front
  pointLight(255, 255, 109, // Color
             getMouseX, getMouseY, 0); // Position
             
             
  noStroke(); 
  beginShape(QUADS);
  //back wall
//  fill(.5); 
//  vertex( 1,  1, -1);
//  vertex(-1,  1, -1);
//  vertex(-1, -1, -1);
//  vertex( 1, -1, -1);
  
//  fill(1, 1, 1); vertex( 1,  1,  1);
//  fill(1, 1, 0); vertex( 1,  1, -1);
//  fill(1, 0, 0); vertex( 1, -1, -1);
//  fill(1, 0, 1); vertex( 1, -1,  1);
//
//  fill(0, 1, 0); vertex(-1,  1, -1);
//  fill(0, 1, 1); vertex(-1,  1,  1);
//  fill(0, 0, 1); vertex(-1, -1,  1);
//  fill(0, 0, 0); vertex(-1, -1, -1);

  fill(0, 1, 0); vertex(-10,  10, -10);
  fill(1, 1, 0); vertex( 10,  10, -10);
  fill(1, 1, 1); vertex( 10,  10,  10);
  fill(0, 1, 1); vertex(-10,  10,  10);

  fill(0, 0, 0); vertex(-10, -10, -10);
  fill(1, 0, 0); vertex( 10, -10, -10);
  fill(1, 0, 1); vertex( 10, -10,  10);
  fill(0, 0, 1); vertex(-10, -10,  10);
 
  endShape(); 
  
  noFill();
  stroke(1);
  box(3);

  popMatrix(); 
  
}
  
float returnMouseX(){
  float myMouseX = map(mouseX,0, width,-100,100);
  return myMouseX;
}

float returnMouseY(){
  float myMouseY= map(mouseY,0, width,-20,20);
  return myMouseY;
}


  
