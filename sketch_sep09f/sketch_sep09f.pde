
int rectWidth = 10;
int num = 20;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  noStroke();
  fill(75, 178, 224);
  
  for(int i=0; i<num; i++){
    for(int j=0; j<num; j++){
      pushMatrix();
        translate(i+i*rectWidth,j+j*rectWidth);
          rect(0,0,rectWidth,rectWidth);
      popMatrix(); 
    }
  }
}
