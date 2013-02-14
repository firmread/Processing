int ballno=10;
 

float circlex[]= new float[ballno];
float circley[]= new float[ballno];
float vely[] = new float[ballno];
float velx[] = new float[ballno];
float g = 0;
 
void setup(){
  smooth();
  frameRate(30);
  size(500,500);
  //initialize the array
  for(int i=0;i<ballno;i++){
    circlex[i]=random(width);
    //random(10,width-10);
    circley[i]=random(height);
    //random(10,height-10);
    vely[i]=random(5);
    velx[i]=random(5);
      background(255);
  }
      
}
 
void draw(){
      float r = map(mouseX,0,width,0,155);
    float g = map(mouseY,0,height,0,100); 
    fill(r+g,r+g,r+g,10);
    rect(0,0,width,height);
    

    strokeWeight(3);
 
    for (int i=0;i<ballno;i++){
    circlex[i]+=velx[i];
    circley[i]+=vely[i];
    

    float cx = map(circlex[i],0,width,0,255);
    float cy = map(circley[i],0,height,0,255);
    fill(cx,cy,100,100);
    
    stroke(0,200);
    float s = map(second()%5,0,5,10,100);
  ellipse(circlex[i],circley[i],s+random(5),s+random(5));
  if((circlex[i]<0)||(circlex[i]>width)){
    velx[i]=velx[i]*-1;
  }
    if((circley[i]<0)||(circley[i]>height)){
      vely[i]=vely[i]*-1;
    }

  
  }
}

