package io.github.fourlastor.game.intro;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

class RoadDrawable extends ShapeDrawerDrawable {

    private static final int CAM_X = 0;

    /**
     * Height from the floor
     */
    private static final int CAM_Y = 150;

    /** How far along the road. */
    private float camZ = -1000;

    /**
     * Camera distance from screen
     */
    private static final int CAMERA_DEPTH = 40;

    /** Length of a single segment. */
    private static final int SEGMENT_LENGTH = 1000;

    /** How many road segments */
    private static final int SEGMENTS_COUNT = 10;

    /** How many segments form a single patch of road. */
    private static final int RUMBLE_LENGTH = 1;

    private final Array<Segment> segments;

    public RoadDrawable(ShapeDrawer shapeDrawer) {
        super(shapeDrawer);
        segments = new Array<>(SEGMENTS_COUNT);
        for (int i = 0; i < SEGMENTS_COUNT; i++) {
            int segmentLength = SEGMENT_LENGTH;
            Segment segment = new Segment(
                    i * segmentLength,
                    (i + 1) * segmentLength,
                    (i / RUMBLE_LENGTH) % 2 == 0 ? Color.GRAY : Color.LIGHT_GRAY);
            segments.add(segment);
        }
    }

    private int findSegmentIndex(float z) {
        return MathUtils.ceil(z / SEGMENT_LENGTH) % segments.size;
    }

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        //            int segmentIndex = findSegmentIndex(camZ);
        //            for (int i = segmentIndex; i < segments.size && i - segments.size < 10; i++) {
        //                Segment segment = segments.get(i);
        //                segment.project(0, 5, camZ, 250, width, height, width / 4, x + width / 2, y + width / 2);
        //                segment.render(shapeDrawer);
        //            }
        for (Segment segment : segments) {
            segment.project(CAM_X, CAM_Y, camZ, CAMERA_DEPTH, width, height, width / 4);
            segment.render(shapeDrawer);
        }
        //            camZ += 300;
        //            if (camZ >= 100000) {
        //                camZ = 0;
        //            }
    }
}
