
int rectWidth = 20;
int num = 100;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  //change color mode to HSB
  colorMode(HSB);
  noStroke();
  background(0);
  
  //Nested For loop
  for(int i=0; i<num; i++){
    for(int j=0; j<num; j++){
      
      // related to location if for loop
      fill(i*4+60, j*3+25, 160);
      
      //layers of space/environment 
      pushMatrix();
        
        //rotate the whole matrix using mouseX and mouseY
        rotateY(radians(returnMouseX()));
        //rotateX(radians(returnMouseY()));        
        rotateX(radians(returnMouseY()));
        
        pushMatrix();
          translate(i+i*rectWidth*1.2,j+j*rectWidth+j*1.2);
          rotateX(radians(returnMouseY()));
            rect(0,0,rectWidth,rectWidth);
        popMatrix();
      popMatrix(); 
    }
  }
}

float returnMouseX(){
  float myMouseX = map(mouseX,0, width,-20,20);
  return myMouseX;
}

float returnMouseY(){
  float myMouseY= map(mouseY,0, width,-50,50);
  return myMouseY;
}
