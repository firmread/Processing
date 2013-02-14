//KEYPRESSED

void setup(){
  size (500,500);  
}

void draw(){
}

//= use for declaring,,, == use for checking if it equal
void keyPressed(){
  if (key == 'a'){
    println("a");
  }
  else if (key == 'b'){
    println("b");
  }
  else if (keyCode == ENTER){
    println();
  }
  else if (keyCode == UP){
    println("up");
  }
  if((key == 'a') || (key == '0')){
    ellipse(mouseX,mouseY,10,10);
  }
}

//there's API of instagram that 
//you can pixelate the picture uploaded automatically 

