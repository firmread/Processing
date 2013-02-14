//simple time bar
int time=0;
void setup(){
  size(500,500);
}

void draw(){
  if(millis()>3000){
    time++;
  }
  rect(time,0,width-10,10);
}
