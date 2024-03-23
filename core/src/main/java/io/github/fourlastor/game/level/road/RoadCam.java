package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.math.Vector3;

public class RoadCam {

    /**
     * X is used when the cart is steering
     * Y is the height from the floor
     * Z is how far along the road the camera is
     */
    public final Vector3 position = new Vector3();

    /** How far the camera is from the screen. */
    public float depth;

    /** TODO: Hack for parallax. */
    public float speedPercent;

    public boolean died;

    public RoadCam(float initialY, float initialDepth) {
        position.y = initialY;
        depth = initialDepth;
    }
}
