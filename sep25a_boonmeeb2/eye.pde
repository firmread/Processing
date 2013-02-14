//from Processing's 'Arctangent' example
class Eye 
{
  int ex, ey,mkx,mky,mksc;
  int size;
  float angle = 0.0;
  
  Eye(int _x, int _y, int _s,int _mkx, int _mky, float _mksc) {
    this.ex = _x;
    this.ey = _y;
    this.size = _s;
 }

  void update(int mx, int my) {
    angle = atan2(((my-this.ey)*mksc)-mky, ((mx-this.ex)*mksc)-mkx);
  }
  
  void display() { 
    pushMatrix();
    translate(this.ex, this.ey);
    fill(0);
    ellipse(0, 0, size, size);
    rotate(angle);
    fill(255,0,0);
    ellipse(size/4, 0, size/2, size/2);
    popMatrix();
  }
}

