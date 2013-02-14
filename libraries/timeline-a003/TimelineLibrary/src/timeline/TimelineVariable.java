/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package timeline;

import processing.core.PApplet;
import timeline.BezierSpline;

public class TimelineVariable {
    public String name = "";
    public BezierSpline spline = new BezierSpline();


    public TimelineVariable(String name) {
        this.name = name;
    }

    public void draw(PApplet applet) {
        spline.drawOnApplet(applet);
    }

    public void mousePressed(int x, int y) {
        spline.addVertex(x, y);
    }

    public void mouseDragged(int x, int y) {
        spline.updateLastTangent(x, y);
    }
}
