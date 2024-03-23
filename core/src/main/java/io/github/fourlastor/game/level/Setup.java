package io.github.fourlastor.game.level;

import com.badlogic.gdx.graphics.Color;

public interface Setup {

    float SPRITE_SCALE = 2;

    Color[] ROAD_COLORS = new Color[] {
        new Color(0x46857bff), new Color(0x3b6166ff),
    };

    int INITIAL_CAMERA_DEPTH = 2;
    int INITIAL_CAMERA_Y = 380;
    int SEGMENTS_VISIBLE = 100;

    /** Length of a single segment. */
    int SEGMENT_LENGTH = 100;

    /** How many segments form a single patch of road. */
    int RUMBLE_LENGTH = 8;

    float PLAYER_MAX_SPEED = 100;
    float PLAYER_MAX_SPEED_OFF_ROAD = PLAYER_MAX_SPEED / 2;
    float PLAYER_ACCELERATION = 40;
    float PLAYER_DECELERATION = -20;
    float PLAYER_BREAKING = -50;
    float PLAYER_OFF_ROAD_DECELERATION = PLAYER_BREAKING * 3;
    float PLAYER_STEERING = 5;
    float PLAYER_STEERING_ANIMATION_DELAY = 0.15f;
    float PLAYER_MAX_OFF_ROAD_POSITION = 700;
    float PLAYER_CENTRIFUGAL_FORCE = 3f;
    float PLAYER_ANIMATION_FRAME_TIME = 0.1f;
}
