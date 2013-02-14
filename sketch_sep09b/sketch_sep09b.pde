// for loop with matrix

void setup() {
  size(800, 800, P3D);
}

void draw() {
  noStroke();
  fill(75, 178, 224);
  
  for(int i=0; i<300; i++){
    rect(i+i*10,height/2,10,10);
  }
  
}
