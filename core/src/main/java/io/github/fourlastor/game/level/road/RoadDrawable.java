package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.math.Rectangle;
import io.github.fourlastor.game.level.Setup;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class RoadDrawable extends ShapeDrawerDrawable {

    private final RoadCam cam;

    private final Road road;

    public RoadDrawable(ShapeDrawer shapeDrawer, Road road, RoadCam cam) {
        super(shapeDrawer);
        this.road = road;
        this.cam = cam;
    }

    private final Rectangle screenRectangle = new Rectangle();

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        screenRectangle.set(x, y, width, height);
        int initialSegmentIndex = road.findSegmentIndex(cam.position.z);
        for (int i = initialSegmentIndex, counter = 0; counter < Setup.SEGMENTS_VISIBLE; i++, counter++) {
            int currentSegmentIndex = i % road.segments.size;
            boolean looped = currentSegmentIndex < initialSegmentIndex;
            Segment segment = road.segments.get(currentSegmentIndex);
            float cameraZ = (cam.position.z % road.totalLength) - (looped ? road.totalLength : 0);
            segment.project(cam.position.x, cam.position.y, cameraZ, cam.depth, width, height, width / 2, x, y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
        }
    }
}
