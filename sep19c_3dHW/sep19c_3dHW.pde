//RGBCube for gradient space
//Light2 for lighting
float boxY;
float vely = 1;


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
    scale(height/22);
        // Yellow spotlight from the front
    pointLight(255, 255, 109, // Color
                0, 0, 2); // Position
    fill(.8);
    pushMatrix();
      translate(0,0,-15);
      rectMode(CENTER);
      rect(0,0,500,500);
    popMatrix();
    
    pushMatrix();
      rotateY(radians(returnMouseX())); 
      rotateX(radians(returnMouseY()));     
                 
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
      
      pushMatrix();
        boxY+=vely;
        translate(0,boxY,0);
        noFill();
        stroke(0,1,1);
        box(boxY/10);
        if((boxY<-9)||(boxY>9)){
          vely=vely*-1;
        }
      popMatrix();
      
    popMatrix(); 
  popMatrix(); 
  
}
  
float returnMouseX(){
  float myMouseX = map(mouseX,0, width,-180,180);
  return myMouseX;
}

float returnMouseY(){
  float myMouseY= map(mouseY,0, width,20,-20);
  return myMouseY;
}


  
