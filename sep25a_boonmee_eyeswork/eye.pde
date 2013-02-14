//from Processing's 'Arctangent' example
class Eye 
{
  int ex, ey;
  int size;
  float angle = 0.0;
  
  Eye(int _x, int _y, int _s) {
    this.ex = _x;
    this.ey = _y;
    this.size = _s;
 }

  void update(int mx, int my,int mkx, int mky, float mksc) {
    angle = atan2(my-((this.ey*mksc)+mky), mx-((this.ex*mksc)+mkx));
  }
  
  void display() { 
    pushMatrix();
    translate(this.ex, this.ey);
    fill(10);
    noStroke();
    ellipse(0, 0, size, size);
    rotate(angle);
    fill(255,0,0);
    ellipse(size/4, 0, size/2, size/2);
    popMatrix();
  }
}

