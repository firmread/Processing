package timeline;

import processing.core.PApplet;

public interface TimelineShape {
    public void drawOnApplet(PApplet applet);
    public float getValue(float time);
}
