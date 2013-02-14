// using variables!!!
int rectWidth= 25;
int num= 100;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  noStroke();
  fill(75, 178, 224);
  
  for(int i=0; i<num; i++){
    pushMatrix();
        rect(i+i*rectWidth,height/2,rectWidth,rectWidth);
    popMatrix();
  }
  
}
