//RGBCube for gradient space
//Light2 for lighting
float getMouseX,getMouseY;

void setup() 
{ 
  size(500, 500, P3D); 
  noStroke(); 
  colorMode(RGB, 1); 
} 


void draw() 
{ 
  background(0);  
  pushMatrix(); 
  translate(width/2, height/2, -height/2); 
  scale(height/2);
  
    
  getMouseX=map(mouseX,0,width,.9,-.9);
  getMouseY=map(mouseY,0,height,.9,-.9);
    // Yellow spotlight from the front
  pointLight(255, 255, 109, // Color
             getMouseX, getMouseY, 0); // Position
  
  beginShape(QUADS);
  //back wall
  fill(.5); 
  vertex( 1,  1, -1);
  vertex(-1,  1, -1);
  vertex(-1, -1, -1);
  vertex( 1, -1, -1);
  
  fill(1, 1, 1); vertex( 1,  1,  1);
  fill(1, 1, 0); vertex( 1,  1, -1);
  fill(1, 0, 0); vertex( 1, -1, -1);
  fill(1, 0, 1); vertex( 1, -1,  1);

  fill(0, 1, 0); vertex(-1,  1, -1);
  fill(0, 1, 1); vertex(-1,  1,  1);
  fill(0, 0, 1); vertex(-1, -1,  1);
  fill(0, 0, 0); vertex(-1, -1, -1);

  fill(0, 1, 0); vertex(-1,  1, -1);
  fill(1, 1, 0); vertex( 1,  1, -1);
  fill(1, 1, 1); vertex( 1,  1,  1);
  fill(0, 1, 1); vertex(-1,  1,  1);

  fill(0, 0, 0); vertex(-1, -1, -1);
  fill(1, 0, 0); vertex( 1, -1, -1);
  fill(1, 0, 1); vertex( 1, -1,  1);
  fill(0, 0, 1); vertex(-1, -1,  1);
 
  endShape(); 


noFill();
stroke(255);
pushMatrix();
scale(1/(height/2));
translate(500, height*0.35, -200);
sphere(280);
popMatrix();


  popMatrix(); 
  
}
  


  
