package io.github.fourlastor.game.level;

import com.badlogic.gdx.graphics.Color;

public interface Setup {

    Color[] COLORS = new Color[] {
        new Color(Color.FOREST).lerp(Color.BLACK, 0.5f), new Color(Color.FOREST).lerp(Color.BLACK, 0.3f),
    };

    int INITIAL_CAMERA_DEPTH = 2;
    int INITIAL_CAMERA_Y = 380;
    int SEGMENTS_VISIBLE = 100;

    /** Length of a single segment. */
    int SEGMENT_LENGTH = 100;

    /** How many segments form a single patch of road. */
    int RUMBLE_LENGTH = 8;
}