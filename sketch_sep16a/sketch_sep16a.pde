// 3 for loop

int rectWidth = 10;
int num = 20;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  //change color mode to HSB
  colorMode(HSB);
  //noStroke();
  background(0);
  translate (width/3,height/3,0);
  //Nested For loop
  for(int i=0; i<num; i++){
    for(int j=0; j<num; j++){
      for(int k=0; k<num; k++){
        
        // related to location if for loop
        fill(i*4+60, j*3+25, 160);
        
        //layers of space/environment 
        pushMatrix();
          
          //rotate the whole matrix using mouseX and mouseY
          rotateY(radians(returnMouseX()));
          //rotateX(radians(returnMouseY()));        
          rotateX(radians(returnMouseY()));
          
          pushMatrix();
            translate(i+i*rectWidth*1.2,j+j*rectWidth+j*1.2, k+k*rectWidth+k*1.2);
              rect(0,0,rectWidth,rectWidth);
          popMatrix();
        popMatrix(); 
      }
    }
  }
}

float returnMouseX(){
  float myMouseX = map(mouseX,0, width,-180,180);
  return myMouseX;
}

float returnMouseY(){
  float myMouseY= map(mouseY,0, width,-180,180);
  return myMouseY;
}
