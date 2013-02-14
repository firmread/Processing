package timeline;

import java.util.List;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class BezierSpline implements TimelineShape {
    public List<BezierVertex> vertices = new ArrayList<BezierVertex>();
    List<PVector> values = new ArrayList<PVector>();

    private PVector minimumValue = new PVector();
    private PVector maximumValue = new PVector();

    private static final float timeStep = 0.001f;

    void calculateValues() {
        values = new ArrayList<PVector>();

        for (int i = 1; i < vertices.size(); i++) {
            BezierVertex v_prev = vertices.get(i - 1);
            BezierVertex v = vertices.get(i);

            // calculate the constants
            PVector c = new PVector(3 * (v_prev.t2.x - v_prev.a.x),
                              3 * (v_prev.t2.y - v_prev.a.y));

            PVector b = new PVector(3 * (v.t1.x - v_prev.t2.x) - c.x,
                              3 * (v.t1.y - v_prev.t2.y) - c.y);

            PVector a = new PVector(v.a.x - v_prev.a.x - c.x - b.x,
                              v.a.y - v_prev.a.y - c.y - b.y);

            // calculate the points
            float x = 0;
            float y = 0;
            for (float t = 0; t <= 1.0; t += timeStep) {
                x = a.x * t * t * t + b.x * t * t + c.x * t + v_prev.a.x;
                y = a.y * t * t * t + b.y * t * t + c.y * t + v_prev.a.y;
                values.add(new PVector(x, y));
            }
        }
    }

    void calculateMinAndMax() {
        if (vertices.size() > 0) {
            PVector anchor = vertices.get(0).a;
            minimumValue.x = anchor.x;
            minimumValue.y = anchor.y;
            maximumValue.x = anchor.x;
            maximumValue.y = anchor.y;
        }

        for (PVector p : values) {
            if (p.x > maximumValue.x) {
                maximumValue.x = p.x;
            }

            if (p.y > maximumValue.y) {
                maximumValue.y = p.y;
            }

            if (p.x < minimumValue.x) {
                minimumValue.x = p.x;
            }

            if (p.y < minimumValue.y) {
                minimumValue.y = p.y;
            }
        }
    }

    public BezierSpline() {

    }

    // use when anything changes in the spline and values must be recalculated
    public void update() {
        calculateValues();
        calculateMinAndMax();
    }


    public void drawOnApplet(PApplet applet) {
        applet.stroke(0);
        applet.beginShape();

        // first vertex is just a vertex, not a BezierVertex
        if (vertices.size() > 0) {
            BezierVertex v = vertices.get(0);
            applet.vertex(v.a.x, v.a.y);
            for (int i = 1; i < vertices.size(); i++) {
                BezierVertex v_prev = vertices.get(i - 1);
                v = vertices.get(i);

                // bezierVertex represents a segment of a Processing bezier curve
                // args: (starting tangent, ending tangent, end point)
                applet.bezierVertex(v_prev.t2.x, v_prev.t2.y, v.t1.x, v.t1.y, v.a.x, v.a.y);
            }


        }

        applet.endShape();
    }

    // TODO
    public void drawBoundingBox(PApplet applet) {

    }

    public void addVertex(float x, float y) {
        // make sure this vertex is to the right of the previous one
        if (vertices.size() == 0 || x > vertices.get(vertices.size() - 1).a.x) {
            vertices.add(new BezierVertex(x, y));
            update();
        }
    }

    private PVector calculateSymmetricTangent(PVector anchor, PVector tangent) {
        PVector symmetricTangent = new PVector(tangent.x, tangent.y);

        symmetricTangent.sub(anchor);
        symmetricTangent.mult(-1);
        symmetricTangent.add(anchor);

        /*symmetricTangent.x = tangent.x - anchor.x;
        symmetricTangent.y = tangent.y - anchor.y;
        symmetricTangent.x *= -1;
        symmetricTangent.y *= -1;
        symmetricTangent.x += anchor.x;
        symmetricTangent.y += anchor.y;*/

        return symmetricTangent;
    }

    public void updateTangent1(int index, float x, float y) {
        BezierVertex vertex = vertices.get(index);

        updateTangent1Only(index, x, y);

        PVector symmetricTangent = calculateSymmetricTangent(vertex.a, vertex.t1);
        updateTangent2Only(index, symmetricTangent.x, symmetricTangent.y);
    }

    public void updateTangent2(int index, float x, float y) {
        BezierVertex vertex = vertices.get(index);

        updateTangent2Only(index, x, y);

        PVector symmetricTangent = calculateSymmetricTangent(vertex.a, vertex.t2);
        updateTangent1Only(index, symmetricTangent.x, symmetricTangent.y);
    }

    public void updateTangent1Only(int index, float x, float y) {
        BezierVertex vertex = vertices.get(index);
        vertex.t1.x = x;
        vertex.t1.y = y;
    }

    public void updateTangent2Only(int index, float x, float y) {
        BezierVertex vertex = vertices.get(index);
        vertex.t2.x = x;
        vertex.t2.y = y;
    }

    public void updateLastTangent(float x, float y) {
        BezierVertex vertex = vertices.get(vertices.size() - 1);
        if (x <= vertex.a.x) {
            x = vertex.t2.x;
        }

        if (vertices.size() > 0) {
            BezierVertex vertex_prev = vertices.get(vertices.size() - 2);
            if (x - vertex.a.x > vertex.a.x - vertex_prev.a.x) {
                x = vertex.t2.x;
            }
        }

        updateTangent2(vertices.size() - 1, x, y);
    }

    // TODO: finish
    public void updateLastTangentOnly(float x, float y) {
        BezierVertex vertex = vertices.get(vertices.size() - 1);
    }

    public int numVertices() {
        return vertices.size();
    }

    public float getValue(float x) {
        // first see if the point is to the left of the spline
        if (x <= minimumValue.x) {
            return values.get(0).y; // take the value of the leftmost point
        } else if (x >= maximumValue.x) { // see if the point is to the right of the spline
            return values.get(values.size() - 1).y; // take the value of the rightmost choice
        } else { // otherwise it's on the spline and we need to look for the closest match
            PVector previousValue = null;
            for(PVector value : values) {
                if (value.x > x) { // once we pass the point we can determine an approximation
                    if (previousValue == null) { // if we have no previous value, the best we
                                                 // can do is return the current value (shouldn't get here)
                        return value.y;
                    } else { // interpolate between the cur value and the previous value
                        return (previousValue.y - value.y) / (previousValue.x - value.x)
                                    * (x - value.x) + previousValue.y;
                    }
                }
                previousValue = value;
            }
        }
        return 0;
    }

    public float getValue2(float x_goal) {

        // first see if the point is to the left of the spline
        if (x_goal <= minimumValue.x) {
            return values.get(0).y; // take the value of the leftmost point
        } else if (x_goal >= maximumValue.x) { // see if the point is to the right of the spline
            return values.get(values.size() - 1).y; // take the value of the rightmost choice
        } else {
            PVector previousValue = new PVector(0,0);
            boolean foundPreviousValue = false;
            for (int i = 1; i < vertices.size(); i++) {
                BezierVertex v_prev = vertices.get(i - 1);
                BezierVertex v = vertices.get(i);

                // calculate the constants
                PVector c = new PVector(3 * (v_prev.t2.x - v_prev.a.x),
                                  3 * (v_prev.t2.y - v_prev.a.y));

                PVector b = new PVector(3 * (v.t1.x - v_prev.t2.x) - c.x,
                                  3 * (v.t1.y - v_prev.t2.y) - c.y);

                PVector a = new PVector(v.a.x - v_prev.a.x - c.x - b.x,
                                  v.a.y - v_prev.a.y - c.y - b.y);

                // calculate the points
                float x = 0;
                float y = 0;
                for (float t = 0; t <= 1.0; t += timeStep) {
                    x = a.x * t * t * t + b.x * t * t + c.x * t + v_prev.a.x;
                    // TODO: don't actually need to calculate y every time
                    y = a.y * t * t * t + b.y * t * t + c.y * t + v_prev.a.y;

                    if (x > x_goal) {
                        if (previousValue == null) { // if we have no previous value, the best we
                                                     // can do is return the current value (shouldn't get here)
                            return y;
                        } else { // interpolate between the cur value and the previous value
                            return (previousValue.y - y) / (previousValue.x - x)
                                        * (x_goal - x) + previousValue.y;
                        }
                    }
                    previousValue.x = x;
                    previousValue.y = y;
                    foundPreviousValue = true;
                }
            }
        }

        return 0; // shouldn't ever get here
    }
}
