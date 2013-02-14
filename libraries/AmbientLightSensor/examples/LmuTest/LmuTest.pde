import lmu.*;


int lmu_left;
int lmu_right;
float multi;
 
void setup()
{
  size(800,800);
  frameRate(20);
 
  // initial sensor values
  int[] lmu_start = LmuTracker.getLMUArray();
  lmu_left  = lmu_start[0];
  lmu_right = lmu_start[1];
  multi = 255.0 / (lmu_left);
} 
 
void draw()
{
  // get current sensor values
  int[] vals = LmuTracker.getLMUArray();
  int li = (int)(vals[0] * multi);
  int re = (int)(vals[1] * multi);
 
  background(255);
 
  // left sensor
  fill(li);
  rect(0, 0, width/2, height);
 
  // right sensor
  fill(re);
  rect(width/2, 0, width/2, height);
}
