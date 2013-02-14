// order of using 
// rotate(), translate(), scale()
// would give a different results


int rectWidth = 20;
int num = 100;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  colorMode(HSB);
  noStroke();
  
  for(int i=0; i<num; i++){
    for(int j=0; j<num; j++){
      
      fill(i*4+60, j*3+25, 160);
      
      pushMatrix();
        rotateY(radians(20));
        rotateX(radians(-20));
        pushMatrix();
          translate(i+i*rectWidth*1.2,j+j*rectWidth+j*1.2);
            rect(0,0,rectWidth,rectWidth);
        popMatrix();
      popMatrix(); 
    }
  }
}
