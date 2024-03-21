package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.component.AnimatedImageComponent;
import io.github.fourlastor.game.level.road.RoadCam;
import javax.inject.Inject;

public class Base extends InputState {

    private final RoadCam cam;

    private float speed = 0;

    private final ComponentMapper<AnimatedImageComponent> images;

    @Inject
    public Base(RoadCam cam, ComponentMapper<AnimatedImageComponent> images) {
        this.cam = cam;
        this.images = images;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        float delta = getDelta();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            speed += Setup.PLAYER_BREAKING * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            speed += Setup.PLAYER_ACCELERATION * delta;
        } else {
            speed += Setup.PLAYER_DECELERATION * delta;
        }
        speed = MathUtils.clamp(speed, 0, Setup.PLAYER_MAX_SPEED);
        float speedPercent = speed / Setup.PLAYER_MAX_SPEED;
        images.get(entity).image.setSpeed(speedPercent);
        cam.position.z += speed;


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= Setup.PLAYER_STEERING * speedPercent;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += Setup.PLAYER_STEERING * speedPercent;
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
