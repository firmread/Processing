package timeline;
import timeline.BezierVertex;
import processing.core.PVector;


public class TimelineImporterVisitor extends GJVoidDepthFirst<VisitorInfo> {
    TimelineData timeline = new TimelineData();

    public void visit(Variable n, VisitorInfo info) {
        String name = n.identifier.nodeToken.tokenImage;
        TimelineVariable timelineVariable = new TimelineVariable(name);
        n.nodeList.accept(this, new VisitorInfo(timelineVariable));
        timelineVariable.spline.update();
        timeline.timelineVariables.add(timelineVariable);
    }

    public void visit(Vertex n, VisitorInfo info) {
        info.timelineVariable.spline.vertices.add(
                new BezierVertex(
                    new PVector(fpLiteralToFloat(n.floatingPointLiteral),
                                fpLiteralToFloat(n.floatingPointLiteral1)),
                    new PVector(fpLiteralToFloat(n.floatingPointLiteral2),
                                fpLiteralToFloat(n.floatingPointLiteral3)),
                    new PVector(fpLiteralToFloat(n.floatingPointLiteral4),
                                fpLiteralToFloat(n.floatingPointLiteral5))  ));
    }

    public void visit(PlaybackChoiceFrames n, VisitorInfo info) {
        timeline.playbackMode = TimelineData.PlaybackType.FRAME_BASED;
    }

    public void visit(PlaybackChoiceTime n, VisitorInfo info) {
        timeline.playbackMode = TimelineData.PlaybackType.TIME_BASED;
    }

    float fpLiteralToFloat(FloatingPointLiteral fp) {
        return Float.parseFloat(fp.nodeToken.tokenImage);
    }


}

class VisitorInfo {
    public TimelineVariable timelineVariable;
    VisitorInfo(TimelineVariable variable) {
        this.timelineVariable = variable;
    }
}
