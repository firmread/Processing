/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package timeline;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

public class TimelineData {
    public List<TimelineVariable> timelineVariables = new ArrayList<TimelineVariable>();
    public int fps = 15;
    public enum PlaybackType { TIME_BASED, FRAME_BASED }
    public PlaybackType playbackMode = PlaybackType.TIME_BASED;

    void draw(PApplet applet) {
        for (TimelineVariable timelineVariable : timelineVariables) {
            timelineVariable.spline.drawOnApplet(applet);
        }
    }
    
    void mousePressed(int x, int y) {
        for (TimelineVariable timelineVariable : timelineVariables) {
            timelineVariable.mousePressed(x, y);
        }
    }
    
    void mouseDragged(int x, int y) {
        for (TimelineVariable timelineVariable : timelineVariables) {
            timelineVariable.mouseDragged(x, y);
        }
    }

    void addVariable(String varName) {
        timelineVariables.add(new TimelineVariable(varName));
    }

}
