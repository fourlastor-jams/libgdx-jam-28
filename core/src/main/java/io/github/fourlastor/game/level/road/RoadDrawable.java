package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.level.Setup;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class RoadDrawable extends ShapeDrawerDrawable {

    private final RoadCam cam;

    private final Array<Segment> segments;

    public RoadDrawable(ShapeDrawer shapeDrawer, Array<Segment> segments, RoadCam cam) {
        super(shapeDrawer);
        this.segments = segments;
        this.cam = cam;
    }

    private int findSegmentIndex(float z) {
        return Math.max(0, MathUtils.ceil(z / Setup.SEGMENT_LENGTH) % segments.size);
    }

    private final Rectangle screenRectangle = new Rectangle();

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        shapeDrawer.setColor(Color.BROWN);
        shapeDrawer.filledRectangle(x, y, width, height);
        screenRectangle.set(x, y, width, height);
        int segmentIndex = findSegmentIndex(cam.position.z);
        float percentRemaining = cam.position.z % Setup.SEGMENT_LENGTH / Setup.SEGMENT_LENGTH;
        Segment initial = segments.get(segmentIndex);
        float dCurve = -initial.curve * percentRemaining;
        float totalCurve = 0;
        for (int i = segmentIndex, counter = 0; i < segments.size && counter < Setup.SEGMENTS_VISIBLE; i++, counter++) {
            Segment segment = segments.get(i);
            segment.project(
                    cam.position.x,
                    cam.position.y,
                    cam.position.z,
                    totalCurve,
                    dCurve,
                    cam.depth,
                    width,
                    height,
                    width / 2,
                    x,
                    y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
            totalCurve += dCurve;
            dCurve += segment.curve;
        }
    }
}
