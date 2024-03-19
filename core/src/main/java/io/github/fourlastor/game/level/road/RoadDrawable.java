package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.level.Setup;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class RoadDrawable extends ShapeDrawerDrawable {

    private static final int CAM_X = 0;

    /**
     * Height from the floor
     */
    private float camY = Setup.INITIAL_CAMERA_Y;

    /** How far along the road. */
    private float camZ = 0;

    /**
     * Camera distance from screen
     */
    private float cameraDepth = Setup.INITIAL_CAMERA_DEPTH;

    private final Array<Segment> segments;

    public RoadDrawable(ShapeDrawer shapeDrawer, Array<Segment> segments) {
        super(shapeDrawer);
        this.segments = segments;
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
        int segmentIndex = findSegmentIndex(camZ);
        float percentRemaining = camZ % Setup.SEGMENT_LENGTH / Setup.SEGMENT_LENGTH;
        Segment initial = segments.get(segmentIndex);
        float dCurve = -initial.curve * percentRemaining;
        float totalCurve = 0;
        for (int i = segmentIndex, counter = 0; i < segments.size && counter < Setup.SEGMENTS_VISIBLE; i++, counter++) {
            Segment segment = segments.get(i);
            segment.project(CAM_X, camY, camZ, totalCurve, dCurve, cameraDepth, width, height, width / 2, x, y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
            totalCurve += dCurve;
            dCurve += segment.curve;
        }
        // end drawing code

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
            camY = Setup.INITIAL_CAMERA_Y;
            cameraDepth = Setup.INITIAL_CAMERA_DEPTH;
        }
    }
}
