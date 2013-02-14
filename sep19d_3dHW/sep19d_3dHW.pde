//RGBCube for gradient space
//Light2 for lighting
float boxY;
float vely = .01;
int num=15;

void setup() 
{ 
  size(500, 500, P3D); 
  colorMode(RGB); 
} 


void draw() 
{ 
  background(255);  
  
  for(int i=-15; i<num; i++){
    pushMatrix(); 
      translate(width/2, height/2, -height/2); 
      scale(height/25);
      
      pushMatrix();
        rotateY(radians(returnMouseX())); 
        rotateX(radians(returnMouseY()));     
                   
        noStroke(); 
        beginShape(QUADS);
      
        fill(113); 
        vertex(-10,  10, -10);
        vertex( 10,  10, -10);
        vertex( 10,  10,  10);
        vertex(-10,  10,  10);
      
        vertex(-10, -10, -10);
        vertex( 10, -10, -10);
        vertex( 10, -10,  10);
        vertex(-10, -10,  10);
       
        endShape(); 
       
        
        pushMatrix();
          boxY+=vely;
          translate(i,boxY,boxY);
          noFill();
          stroke(0,((i+15)*4),((i+30)*3.5));
          box(boxY/7);
          if((boxY<-9)||(boxY>9)){
            vely=vely*-1;
          }
        popMatrix();
      popMatrix(); 
    popMatrix(); 
  }    
}
  
float returnMouseX(){
  float myMouseX = map(mouseX,0, width,-180,180);
  return myMouseX;
}

float returnMouseY(){
  float myMouseY= map(mouseY,0, width,20,-20);
  return myMouseY;
}


  
