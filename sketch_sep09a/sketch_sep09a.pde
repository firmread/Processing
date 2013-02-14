void setup() {
  size(800, 800, P3D);
  // there is P3D is to render in 3D
  // P2D is 2D
}

void draw() {
  noStroke();
  fill(75, 178, 224);
  
  //begin matrix
  pushMatrix();
    translate(width/2, height/2);
    //always make it flexible with the size changing!
    rect(0, 0, 25, 25);
    
  // end matrix
  popMatrix();
    rect(10, 10, 25, 25);
    //this rectangle is on another matrix
    // > another space > another rule
}

