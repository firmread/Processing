Monkey myMonkey;

void setup(){
  size(500,500);
  smooth();

  myMonkey = new Monkey(100,100,.5);
  background(255);
}

void draw(){
  myMonkey.drawMonkey();

}
