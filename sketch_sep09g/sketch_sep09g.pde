
int rectWidth = 10;
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
        translate(i+i*rectWidth,j+j*rectWidth);
          rect(0,0,rectWidth,rectWidth);
      popMatrix(); 
    }
  }
}
