//for loop = counter!!!!

//syntax:
//for(init; test; update){
// statements
//}
//example
//for(i=0, i<20, i+1){
//

//SIMPLE FOR LOOP
//MULTIPLE OF 5
//for(int i=0; i<105; i+=5){
//  
// println(1);
//}
//



void setup(){
  size(500,500);
  //noStroke();
  stroke(255,0,255);
  smooth();
  strokeWeight(1);
}

void draw(){
  background(255);
  fill(255);
  
  for(int i=0; i<width+5; i+=25){
    for(int j=0; j<height+5; j+=25){
      ellipse(i,j,25,25);
      triangle(i,j+20,i+10,j,i+20,j+20);
    }
  }
}
