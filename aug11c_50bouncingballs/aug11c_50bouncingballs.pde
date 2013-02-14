//50 bouncing balls
//differnce kind of bounce
//basic particle system
int ballno=50;

//declaring variable
float circlex[]= new float[ballno];
float circley[]= new float[ballno];
float vely[] = new float[ballno];
float velx[] = new float[ballno];
float g = 0;

void setup(){
  smooth();
  frameRate(60);
  size(500,600);
  //initialize the array
  for(int i=0;i<ballno;i++){
    circlex[i]=random(10,width-10);
    circley[i]=random(10,height-10);
    vely[i]=random(3);
    velx[i]=random(2);
  }
      
}

void draw(){
  background(0);
  //fill(0,random(150,255),random(150,255),random(100,255));
    float r = map(pmouseX,0,width,150,255);
    float g = map(pmouseY,0,height,100,250);   
    fill(r,g,0);
    rect(0,0,width,height);
    noStroke();
    fill(255-r,100,255-g,random(100,110));
    strokeWeight(1);
    for (int i=0;i<ballno;i++){
    circlex[i]+=velx[i];
    circley[i]+=vely[i];
   

     
  ellipse(circlex[i],circley[i],random(140,150),random(140,150));
  if((circlex[i]<0)||(circlex[i]>width)){
    velx[i]=velx[i]*-1;
  }
  if((circley[i]<0)||(circley[i]>height)){
    vely[i]=vely[i]*-1;
  }
  
}
   //same as vel=vel
   //inserting picture
}
