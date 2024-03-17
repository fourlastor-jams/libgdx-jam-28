package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    private static final int INITIAL_CAMERA_DEPTH = 10;
    private static final int INITIAL_CAMERA_Y = 180;

    /**
     * Height from the floor
     */
    private float camY = INITIAL_CAMERA_Y;

    /** How far along the road. */
    private float camZ = 0;

    /**
     * Camera distance from screen
     */
    private float cameraDepth = INITIAL_CAMERA_DEPTH;

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

    private final Rectangle screenRectangle = new Rectangle();

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        shapeDrawer.setColor(Color.BROWN);
        shapeDrawer.filledRectangle(x, y, width, height);
        screenRectangle.set(x, y, width, height);
        int segmentIndex = findSegmentIndex(camZ);
        for (int i = segmentIndex; i < segments.size && i - segments.size < 1; i++) {
            Segment segment = segments.get(i);
            segment.project(CAM_X, camY, camZ, cameraDepth, width, height, width / 2, x, y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camZ -= 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camZ += 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                camY += 30;
            } else {
                camY += 10;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                camY -= 30;
            } else {
                camY -= 10;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cameraDepth += 1f;
            } else {
                cameraDepth += 0.1f;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cameraDepth -= 1f;
            } else {
                cameraDepth -= 0.1f;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            camZ = 0;
            camY = INITIAL_CAMERA_Y;
            cameraDepth = INITIAL_CAMERA_DEPTH;
        }
    }
}
