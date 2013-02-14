import timeline.*;
Timeline timeline;

void setup() {
  timeline = new Timeline(this);
  stroke(255, 0, 255);
}

void draw() {
  background(0);
  float pointHeightValue = timeline.getValue("pointHeight");
  point(width/2.0f, pointHeightValue);
}
