package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.actor.ScaledAnimatedImage;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.component.AnimatedImageComponent;
import io.github.fourlastor.game.level.component.PlayerComponent;
import io.github.fourlastor.game.level.road.Road;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.road.Segment;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import javax.inject.Inject;

public class Base extends InputState {

    private final Dependencies dependencies;

    @Inject
    public Base(Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    protected Animation<Drawable> animation(String name) {
        Array<TextureAtlas.AtlasRegion> regions = dependencies.atlas.findRegions("player/" + name);
        Array<Drawable> drawables = new Array<>(regions.size);
        for (TextureAtlas.AtlasRegion region : regions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        return new FixedFrameAnimation<>(0.1f, drawables, Animation.PlayMode.LOOP);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        float delta = getDelta();
        PlayerComponent player = players().get(entity);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.speed += Setup.PLAYER_BREAKING * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.speed += Setup.PLAYER_ACCELERATION * delta;
        } else {
            player.speed += Setup.PLAYER_DECELERATION * delta;
        }
        player.speed = MathUtils.clamp(player.speed, 0, Setup.PLAYER_MAX_SPEED);
        float speedPercent = player.speed / Setup.PLAYER_MAX_SPEED;
        ScaledAnimatedImage image = images().get(entity).image;
        image.setSpeed(speedPercent);
        RoadCam cam = cam();
        cam.position.z += player.speed;

        Road road = dependencies.road;
        Segment currentSegment = road.segments.get(road.findSegmentIndex(cam.position.z));
        cam.position.x = cam.position.x - Math.abs(currentSegment.dCurve) * speedPercent * currentSegment.curve * 3f;
        cam.position.x = MathUtils.clamp(cam.position.x, -500, 500);

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

    protected ComponentMapper<AnimatedImageComponent> images() {
        return dependencies.images;
    }

    protected ComponentMapper<PlayerComponent> players() {
        return dependencies.players;
    }

    protected RoadCam cam() {
        return dependencies.cam;
    }

    public static class Dependencies {

        private final RoadCam cam;

        private final TextureAtlas atlas;

        private final ComponentMapper<PlayerComponent> players;
        private final ComponentMapper<AnimatedImageComponent> images;
        private final Road road;

        @Inject
        public Dependencies(
                RoadCam cam,
                TextureAtlas atlas,
                ComponentMapper<PlayerComponent> players,
                ComponentMapper<AnimatedImageComponent> images,
                Road road) {
            this.cam = cam;
            this.atlas = atlas;
            this.players = players;
            this.images = images;
            this.road = road;
        }
    }
}
