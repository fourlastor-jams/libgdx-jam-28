package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.actor.ScaledAnimatedImage;
import io.github.fourlastor.game.level.Message;
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
        return new FixedFrameAnimation<>(Setup.PLAYER_ANIMATION_FRAME_TIME, drawables, Animation.PlayMode.LOOP);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        float delta = getDelta();
        PlayerComponent player = players().get(entity);
        RoadCam cam = cam();
        //        if (cam.died) {
        //            return;
        //        }
        Road road = dependencies.road;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.speed += Setup.PLAYER_BREAKING * delta;
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.speed += Setup.PLAYER_ACCELERATION * delta;
            } else {
                player.speed += Setup.PLAYER_DECELERATION * delta;
            }
        }
        boolean offRoad = cam.position.x < -Setup.PLAYER_MAX_OFF_ROAD_POSITION / 2
                || cam.position.x > Setup.PLAYER_MAX_OFF_ROAD_POSITION / 2;
        float currentMaxSpeed = Setup.PLAYER_MAX_SPEED;
        if (offRoad) {
            if (road.floorIsLava) {
                cam.speedPercent = 0;
                dependencies.dispatcher.dispatchMessage(Message.DIED_BY_LAVA.ordinal());
                return;
            } else {
                currentMaxSpeed = MathUtils.clamp(player.speed, 0, Setup.PLAYER_MAX_SPEED)
                        + Setup.PLAYER_OFF_ROAD_DECELERATION * 3 * delta;
                currentMaxSpeed = Math.max(currentMaxSpeed, Setup.PLAYER_MAX_SPEED_OFF_ROAD);
            }
        }
        player.speed = MathUtils.clamp(player.speed, 0, currentMaxSpeed);
        float speedPercent = player.speed / Setup.PLAYER_MAX_SPEED;
        ScaledAnimatedImage image = images().get(entity).image;
        image.setSpeed(speedPercent);
        cam.position.z += player.speed;

        Segment currentSegment = road.segments.get(road.findSegmentIndex(cam.position.z));
        cam.position.x = cam.position.x
                - Math.abs(currentSegment.dCurve)
                        * speedPercent
                        * currentSegment.curve
                        * Setup.PLAYER_CENTRIFUGAL_FORCE;
        cam.position.x = MathUtils.clamp(
                cam.position.x, -Setup.PLAYER_MAX_OFF_ROAD_POSITION, Setup.PLAYER_MAX_OFF_ROAD_POSITION);

        cam.speedPercent = speedPercent;
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
        private final MessageDispatcher dispatcher;

        @Inject
        public Dependencies(
                RoadCam cam,
                TextureAtlas atlas,
                ComponentMapper<PlayerComponent> players,
                ComponentMapper<AnimatedImageComponent> images,
                Road road,
                MessageDispatcher dispatcher) {
            this.cam = cam;
            this.atlas = atlas;
            this.players = players;
            this.images = images;
            this.road = road;
            this.dispatcher = dispatcher;
        }
    }
}
