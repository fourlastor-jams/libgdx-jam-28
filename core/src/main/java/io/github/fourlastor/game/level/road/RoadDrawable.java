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
        int segmentIndex = road.findSegmentIndex(cam.position.z);
        for (int i = segmentIndex, counter = 0;
                i < road.segments.size && counter < Setup.SEGMENTS_VISIBLE;
                i++, counter++) {
            Segment segment = road.segments.get(i);
            segment.project(cam.position.x, cam.position.y, cam.position.z, cam.depth, width, height, width / 2, x, y);
            if (segment.inside(screenRectangle)) {
                segment.render(shapeDrawer);
            }
        }
    }
}
