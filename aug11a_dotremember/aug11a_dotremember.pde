int num=10;
int[] x=new int[num];
int[] y=new int[num];

void setup(){
  size(500,500);
  noFill();
  stroke(0);
  smooth();
  //move background to another void to see what will happen
  background(255);
  for(int i=0; i<num; i++){
    x[i]=0;
    y[i]=0;
  }
}
void draw(){
  //comment out draw and see what array is working on,,,
  for(int i=1;i<num;i++){
    x[i-1]=x[i];
    y[i-1]=y[i];
  }
}
void mouseClicked(){
  for(int i=1; i<num;i++){
    x[i]=mouseX;
    y[i]=mouseY;
    
    line(x[i-1],y[i-1],x[i],y[i]);
    ellipse(mouseX,mouseY,5,5);
  }
}

// bouncing crazy shit using this array thing
