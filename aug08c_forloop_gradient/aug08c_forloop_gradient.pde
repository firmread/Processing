//gradient color using for-loop

void setup(){
  size(500,500);
  background(0);
}

void draw(){
  for(int i=0;i<255;i++){
    for(int j=0; j<255; j++){
//    strokeWeight(5);
//    stroke(i);
//    line(0, i, width, i);
    stroke(100,i,j);
    point(i,j);
    }
  }
}
