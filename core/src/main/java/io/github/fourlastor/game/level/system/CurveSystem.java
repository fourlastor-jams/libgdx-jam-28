package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.EntitySystem;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.road.Road;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.road.Segment;
import javax.inject.Inject;

public class CurveSystem extends EntitySystem {

    private final Road road;
    private final RoadCam cam;

    @Inject
    public CurveSystem(Road road, RoadCam cam) {
        this.road = road;
        this.cam = cam;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int segmentIndex = road.findSegmentIndex(cam.position.z);
        float percentRemaining = cam.position.z % Setup.SEGMENT_LENGTH / Setup.SEGMENT_LENGTH;
        Segment initial = road.segments.get(segmentIndex);
        float dCurve = -initial.curve * percentRemaining;
        float totalCurve = 0;
        for (int i = segmentIndex, counter = 0;
                i < road.segments.size && counter < Setup.SEGMENTS_VISIBLE;
                i++, counter++) {
            Segment segment = road.segments.get(i);
            segment.dCurve = dCurve;
            segment.totalCurve = totalCurve;
            totalCurve += dCurve;
            dCurve += segment.curve;
        }
    }
}
