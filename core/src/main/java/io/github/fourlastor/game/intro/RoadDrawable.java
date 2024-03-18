package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

class RoadDrawable extends ShapeDrawerDrawable {

    private static final Color[] COLORS = new Color[] {
        new Color(Color.FOREST).lerp(Color.BLACK, 0.5f), new Color(Color.FOREST).lerp(Color.BLACK, 0.3f),
    };

    private static final int CAM_X = 0;
    private static final int INITIAL_CAMERA_DEPTH = 2;
    private static final int INITIAL_CAMERA_Y = 380;
    private static final int SEGMENTS_SEEN = 100;

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
    private static final int SEGMENT_LENGTH = 100;

    /** How many road segments */
    private static final int SEGMENTS_COUNT = 500;

    /** How many segments form a single patch of road. */
    private static final int RUMBLE_LENGTH = 8;

    private final Array<Segment> segments;

    public RoadDrawable(ShapeDrawer shapeDrawer) {
        super(shapeDrawer);
        segments = new Array<>(SEGMENTS_COUNT);
        int position = 0;
        position += addRoad(position, 150, 150, 150, 1);
        position += addRoad(position, 0, 25, 0, 0);
        addRoad(position, 150, 150, 150, -2);
    }

    private void addSegment(int position, float curve) {
        Color color = COLORS[(position / RUMBLE_LENGTH) % COLORS.length];
        Segment segment = new Segment(position * SEGMENT_LENGTH, (position + 1) * SEGMENT_LENGTH, color, curve);
        segments.add(segment);
    }

    private int addRoad(int basePosition, int enter, int hold, int leave, float curve) {
        for (int i = 0; i < enter; i++) {
            addSegment(i + basePosition, Interpolation.pow2In.apply(0, curve, (float) i / enter));
        }

        for (int i = 0; i < hold; i++) {
            addSegment(i + enter + basePosition, curve);
        }
        for (int i = 0; i < leave; i++) {
            addSegment(i + enter + hold + basePosition, Interpolation.pow2Out.apply(curve, 0, (float) i / leave));
        }

        return enter + hold + leave;
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
        float percentRemaining = camZ % SEGMENT_LENGTH / SEGMENT_LENGTH;
        Segment initial = segments.get(segmentIndex);
        float dCurve = -initial.curve * percentRemaining;
        float totalCurve = 0;
        for (int i = segmentIndex, counter = 0; i < segments.size && counter < SEGMENTS_SEEN; i++, counter++) {
            Segment segment = segments.get(i);
            segment.project(CAM_X, camY, camZ, totalCurve, dCurve, cameraDepth, width, height, width / 2, x, y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
            totalCurve += dCurve;
            dCurve += segment.curve;
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
