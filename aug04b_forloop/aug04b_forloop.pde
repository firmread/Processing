void setup(){
  
  size(600,200);
  //colorMode(HSB,600);
  noStroke();

}

void draw(){
  for(int i=0;i<width;i+=60){
    for(int j=0;j<height;j+=30){
      fill(random(0,600),600,600);
      
      rect(i,j,random(50,60),30);
    }
  }
}


//j=j+30
//j+=30
//does the same thing


