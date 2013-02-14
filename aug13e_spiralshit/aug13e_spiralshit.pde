int time_off = 500; //time between flashes
int time_on = 1000; //how much time to stay on
int last_saved_time;//last time the light was on or off 
Boolean light_is_on = false;//boolean to see if the light is on or off

void setup() {
  smooth();
  size(400, 400);
  last_saved_time = millis();
}

void draw() {
  background(0);
  translate(width * .5, height * .5); //rotate from the center of the screen
  rotate(millis() * .0012); //rotation in radians
  scale(millis() * .0001); //scale up slowly. scale(1) is 100%, scale(2) is 200%, etc...
  
  //by traditional programming standards, the above code is sloppy from a memory perspective,
  //but heck, I'm in art school, so who cares!!
  
  for (int i = 0; i<200; i++) {
    //I'm using this for loop to draw 120 bulbs in a spiral by rotating each bulb 
    //slightly and scaling it slightly. Past rotate() and scale() commands stack on each other
    //so they rotate based on the last rotation. 
    rotate(PI * .10); 
    scale(.95); //smaller and smaller scale every iteration of this for loop 
    drawCir(int(width * .5), int(height * .5));
  }
}

void drawCir(int center_x, int center_y) {
  //change the color of the light
  stroke(random(200,255),random(200,255),random(0,20),200);
  strokeWeight(10);
  if (light_is_on == true) {
    fill(255, 80);
    if (time_on < millis() - last_saved_time) {
        light_is_on = false; 
        last_saved_time = millis();
    } 
  } else {
    fill(20,80);
    if (time_off < millis() - last_saved_time) {
      light_is_on = true;
      last_saved_time = millis();
    }
  }
  //draw the bulb
  ellipse(center_x, center_y, random(90,100), random(90,100));
  
}
