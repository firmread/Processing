//RGBCube for gradient space
//Light2 for lighting
int boxno = 10;

float boxY[] = new float[boxno];
float vely[] = new float[boxno];


void setup() 
{ 
  size(500, 500, P3D); 
  colorMode(RGB, 1); 
  for(int i=0; i<boxno; i++){
    boxY[i]=0;
    vely[i]=random(.5,3);
  }
} 


void draw() 
{ 
  background(0);  
  pushMatrix(); 
    translate(width/2, height/2, -height/2); 
    scale(height/25);
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
    
      fill(0.5); 
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
      for (int i=0;i<boxno;i++){
        boxY[i]+=vely[i];
        translate(0,boxY[i],0);
        noFill();
        stroke(0,1,1);
        box(boxY[i]/10);
        if((boxY[i]<-9)||(boxY[i]>9)){
          vely[i]=vely[i]*-1;
        }
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


  
