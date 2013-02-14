import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;

import processing.core.*;

/**
 * This is an example of a path generator based on Lissajous figures.
 * 
 * @author Peter Lager
 *
 */
public class Lissajous implements I_PathGen {

  private int xCoef, yCoef, zCoef;


  public Lissajous() {
    this.xCoef = 1;
    this.yCoef = 3;
    this.zCoef = 2;
  }

  public Lissajous(int xCoef, int yCoef, int zCoef) {
    this.xCoef = xCoef;
    this.yCoef = yCoef;
    this.zCoef = zCoef;
  }

  public float x(float t) {
    float X = 300 * PApplet.sin(xCoef * t * PApplet.TWO_PI);
    return X;
  }

  public float y(float t) {
    float Y = 160 * PApplet.sin(yCoef * t * PApplet.TWO_PI + PApplet.PI/2);
    return Y;
  }

  public float z(float t) {
    float Z = 300 * PApplet.sin(zCoef * t * PApplet.TWO_PI);
    ;
    return Z;
  }
}

