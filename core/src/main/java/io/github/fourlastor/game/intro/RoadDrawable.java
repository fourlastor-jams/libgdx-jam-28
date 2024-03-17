package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

class RoadDrawable extends ShapeDrawerDrawable {

    private static final Color[] COLORS = new Color[] {
        new Color(Color.RED).lerp(Color.GRAY, 0.5f),
        new Color(Color.GREEN).lerp(Color.GRAY, 0.5f),
        new Color(Color.BLUE).lerp(Color.GRAY, 0.5f),
        new Color(Color.YELLOW).lerp(Color.GRAY, 0.5f),
        new Color(Color.ORANGE).lerp(Color.GRAY, 0.5f),
        new Color(Color.PINK).lerp(Color.GRAY, 0.5f),
        new Color(Color.PURPLE).lerp(Color.GRAY, 0.5f)
    };

    private static final int CAM_X = 0;

    /**
     * Height from the floor
     */
    private static final int CAM_Y = 180;

    /** How far along the road. */
    private float camZ = 0;

    /**
     * Camera distance from screen
     */
    private static final int CAMERA_DEPTH = 10;

    /** Length of a single segment. */
    private static final int SEGMENT_LENGTH = 500;

    /** How many road segments */
    private static final int SEGMENTS_COUNT = 100;

    /** How many segments form a single patch of road. */
    private static final int RUMBLE_LENGTH = 1;

    private final Array<Segment> segments;

    public RoadDrawable(ShapeDrawer shapeDrawer) {
        super(shapeDrawer);
        segments = new Array<>(SEGMENTS_COUNT);
        for (int i = 0; i < SEGMENTS_COUNT; i++) {
            int segmentLength = SEGMENT_LENGTH;
            Color color = COLORS[(i / RUMBLE_LENGTH) % COLORS.length];
            Segment segment = new Segment(i * segmentLength, (i + 1) * segmentLength, color);
            segments.add(segment);
        }
    }

    private int findSegmentIndex(float z) {
        return Math.max(0, MathUtils.ceil(z / SEGMENT_LENGTH) % segments.size);
    }

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        int segmentIndex = findSegmentIndex(camZ);
        for (int i = segmentIndex; i < segments.size && i - segments.size < 10; i++) {
            Segment segment = segments.get(i);
            segment.project(CAM_X, CAM_Y, camZ, CAMERA_DEPTH, width, height, width / 2);
            segment.render(shapeDrawer);
        }
        //            camZ += 300;
        //            if (camZ >= 100000) {
        //                camZ = 0;
        //            }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camZ -= 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camZ += 100;
        }
    }
}
