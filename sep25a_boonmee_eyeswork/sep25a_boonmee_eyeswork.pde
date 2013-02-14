Monkey m1,m2,m3,m4;
int screensize = 800;

void setup(){
  size(screensize,screensize,P3D);
  //smooth();

  m1 = new Monkey(0,550,.5);
  m2 = new Monkey(-100,-200,2);
  m3 = new Monkey(330,550,.5);
  m4 = new Monkey(550,570,.5);

}

void draw(){
  background(10,143,255);
  m2.drawMonkey();
  fill(255,20);
  rect(0,0,width,height);
  pushMatrix();
  translate(0,0,10);
  m1.drawMonkey();

  m3.drawMonkey();
  m4.drawMonkey();
  popMatrix();

}

