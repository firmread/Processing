int bg_value = 30;

void setup(){
  size(320,240);
}

void draw(){
  background(bg_value);
}

void mousePressed(){
  bg_value = 230;
}

void mouseReleased(){
  bg_value = 30;
}

void mouseDragged(){
  bg_value = 130;
}

void mouseMoved(){
}

void mouseClicked(){
}
