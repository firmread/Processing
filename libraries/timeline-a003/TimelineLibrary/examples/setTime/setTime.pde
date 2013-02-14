import timeline.*;

Timeline timeline;

void setup() {
  size(400, 400);
  timeline = new Timeline(this); 
  
  noStroke();
  fill(255, 0, 255);
  smooth();
}

void draw() {
  background(0);

  // draw an ellipse, with the y-location specified by a timeline variable
  ellipse(width / 2.0f, timeline.getValue("y") + height / 2.0f, 40, 40);
}

void mousePressed() {
  // When the mouse is pressed,
  // restart the animation
  timeline.setTime("y", 0.0f);
}
