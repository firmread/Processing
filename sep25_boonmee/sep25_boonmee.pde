Monkey myMonkey;

void setup(){
  size(800,800);
  smooth();

  myMonkey = new Monkey(0,0,1);
  background(255);
}

void draw(){
  myMonkey.drawMonkey();

}
