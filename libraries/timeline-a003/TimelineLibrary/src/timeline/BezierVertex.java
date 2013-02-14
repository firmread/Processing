package timeline;

import processing.core.PVector;

public class BezierVertex {
    public PVector a;  // anchor
    public PVector t1; // tangent 1 (incoming)
    public PVector t2; // tangent 2 (leaving)

    public BezierVertex(PVector a, PVector t1, PVector t2) {
        this.a = a;
        this.t1 = t1;
        this.t2 = t2;
    }

    BezierVertex(float x, float y) {
        a = new PVector(x, y);
        t1 = new PVector(x, y);
        t2 = new PVector(x, y);
    }
}
