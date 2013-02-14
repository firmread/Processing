package timeline;

import processing.core.PApplet;

public class Timeline {
    PApplet applet;
    timeline.TimelineData timeline;
    boolean realtime = true;
    int fps;

    public Timeline(PApplet applet) {
        this.applet = applet;
        load();
    }

    public Timeline(PApplet applet, int fps) {
        this.applet = applet;
        this.fps = fps;
        realtime = false;
        load();
    }

    /**
     * Create a timeline object by importing the data file
     */
    private void load() {
        timeline = TimelineImporter.importTimeline(applet.createInput("timeline-data.txt"));
    }

    /**
     * Sets the current time of a specific timeline variable
     *
     * @param varName The name of the variable to set the current time
     * @param time The time (in seconds) the variable should set to
     */
     public void setTime(String varName, float time) {
        // TODO: switch to a hash map
        for (TimelineVariable variable : timeline.timelineVariables) {
            if (variable.name.equals(varName)) {

                // the current time depends on which mode we're in
                float currentTime;
                if (realtime) {
                    currentTime = applet.millis();
                } else {
                    currentTime = (float) applet.frameCount / fps * 1000.0f;
                }

                variable.setTimeOffset(currentTime - time * 1000.0f);
            }
        }
     }

    /**
     * Look at a cached set of spline points, and linearly interpolate between
     * the two closest matches.
     * 
     * @param varName
     * @return The current value of the variable named varName
     */
    public float getValue(String varName) {
        // TODO: switch to a hash map
        for (TimelineVariable variable : timeline.timelineVariables) {
            if (variable.name.equals(varName)) {

                float currentTime;
                if (realtime) {
                    currentTime = applet.millis() - variable.getTimeOffset();
                } else { // rendered
                    currentTime = (float) applet.frameCount / fps * 1000.0f - variable.getTimeOffset();
                }

                //System.out.println("offset: " + variable.getTimeOffset() + " " + "cur time: " + currentTime);

                // note: 1 second = 100 pixels
                //       To go from millis to pixels, divide by 10
                return variable.spline.getValue(currentTime / 10.0f);
            }
        }

        // TODO: throw exception instead of silently returning 0 when the
        //       variable was not found
        return 0;
    }

    // non-cached version
    // (used for testing)
    public float getValue2(String varName) {
        // TODO: switch to a hash map
        for (TimelineVariable variable : timeline.timelineVariables) {
            if (variable.name.equals(varName)) {
                float currentTime;
                if (realtime) {
                    currentTime = applet.millis() - variable.getTimeOffset();
                } else { // rendered
                    currentTime = (float) applet.frameCount / fps * 1000.0f - variable.getTimeOffset();
                }

                //System.out.println("offset: " + variable.getTimeOffset() + " " + "cur time: " + currentTime);

                // note: 1 second = 100 pixels
                //       To go from millis to pixels, divide by 10
                return variable.spline.getValue2(currentTime / 10.0f);
            }
        }

        return 0;
    }
}
