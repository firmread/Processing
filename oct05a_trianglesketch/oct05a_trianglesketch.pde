void setup(){
  size(480,320);
  smooth();
}

void draw(){
  background(0);
  
  createGradient(width/2, height/2, height, color(17,73,110), color(127,185,62));
  
  int work=75;
  int sleep=75;
  int live=75;
  noStroke();
  
  beginShape();
  //work
  //fill(127,185,62);
  vertex(width/2,height/2-work);
  //live
  fill(17,73,110);
  vertex(width/2-live*cos(radians(30)), height/2+live*sin(radians(30)));
  //sleep
  //fill(2,73,89);
  vertex(width/2+sleep*cos(radians(30)), height/2+sleep*sin(radians(30)));
  endShape();
  
}

void createGradient (float x, float y, float radius, color c1, color c2){
  float px = 0, py = 0, angle = 0;

  // calculate differences between color components 
  float deltaR = red(c2)-red(c1);
  float deltaG = green(c2)-green(c1);
  float deltaB = blue(c2)-blue(c1);
  // hack to ensure there are no holes in gradient
  // needs to be increased, as radius increases
  float gapFiller = 8.0;

  for (int i=0; i< radius; i++){
    for (float j=0; j<360; j+=1.0/gapFiller){
      px = x+cos(radians(angle))*i;
      py = y+sin(radians(angle))*i;
      angle+=1.0/gapFiller;
      color c = color(
      (red(c1)+(i)*(deltaR/radius)),
      (green(c1)+(i)*(deltaG/radius)),
      (blue(c1)+(i)*(deltaB/radius)) 
        );
      set(int(px), int(py), c);      
    }
  }
  // adds smooth edge 
  // hack anti-aliasing
  noFill();
  strokeWeight(3);
  ellipse(x, y, radius*2, radius*2);
}

