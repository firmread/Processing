// same result but using translates
int rectWidth= 150;
int num= 100;

void setup() {
  size(800, 800, P3D);
}

void draw() {
  noStroke();
  fill(75, 178, 224);
  
  for(int i=0; i<num; i++){
    pushMatrix();
      //rotateX(radians(20));
      rotateY(radians(45));
      rotateZ(radians(30));
      // rotateZ is rotating around Z axis
      translate(i+i*rectWidth,0);
        rect(0,0,rectWidth,rectWidth);
    popMatrix();
  }
  
}
