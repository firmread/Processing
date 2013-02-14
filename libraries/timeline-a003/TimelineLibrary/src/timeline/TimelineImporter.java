package timeline;

import java.io.File;
import java.io.InputStream;
import timeline.TimelineData;

public class TimelineImporter {
    static TimelineDataParser parser = null;

    public static TimelineData importTimeline(String filename) {
        if (parser == null) {
            parser = new TimelineDataParser(processing.core.PApplet.createInput(new File(filename)));
        } else {
            parser.ReInit(processing.core.PApplet.createInput(new File(filename)));
        }
        try {
            Goal g = parser.Goal();
                TimelineImporterVisitor importerVisitor = new TimelineImporterVisitor();
                g.accept(importerVisitor, null);
                return importerVisitor.timeline;

        } catch(ParseException pe) {
            System.out.println("Error: Bad timeline data file");
            return null;
       }
    }
}
