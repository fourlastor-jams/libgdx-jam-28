package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.level.Setup;

public class Road {
    public final Array<Segment> segments;
    public final float totalLength;

    public boolean floorIsLava = false;

    public Road(Array<Segment> segments) {
        this.segments = segments;
        this.totalLength = Setup.SEGMENT_LENGTH * segments.size;
    }

    public int findSegmentIndex(float z) {
        return Math.max(0, MathUtils.ceil(z / Setup.SEGMENT_LENGTH) % segments.size);
    }
}
