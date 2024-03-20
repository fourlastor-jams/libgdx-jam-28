package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.road.RoadCam;
import javax.inject.Inject;

public class Base extends InputState {

    private final RoadCam cam;

    @Inject
    public Base(RoadCam cam) {
        this.cam = cam;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.z -= 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.z += 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cam.position.y += 30;
            } else {
                cam.position.y += 10;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cam.position.y -= 30;
            } else {
                cam.position.y -= 10;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cam.depth += 1f;
            } else {
                cam.depth += 0.1f;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cam.depth -= 1f;
            } else {
                cam.depth -= 0.1f;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            cam.position.z = 0;
            cam.position.y = Setup.INITIAL_CAMERA_Y;
            cam.depth = Setup.INITIAL_CAMERA_DEPTH;
        }
    }
}
