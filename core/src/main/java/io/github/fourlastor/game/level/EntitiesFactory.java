package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.actor.ScaledAnimatedImage;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.AnimatedImageComponent;
import io.github.fourlastor.game.level.component.PlayerRequestComponent;
import io.github.fourlastor.game.level.road.EnvironmentParallaxImage;
import io.github.fourlastor.game.level.road.Road;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.road.RoadDrawable;
import io.github.fourlastor.game.level.road.Segment;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import io.github.fourlastor.harlequin.component.ActorComponent;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final TextureAtlas textureAtlas;

    private final Stage stage;
    private final ShapeDrawer shapeDrawer;
    private final RoadCam roadCam;
    private final Road road;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas, Stage stage, ShapeDrawer shapeDrawer, RoadCam roadCam, Road road) {
        this.textureAtlas = textureAtlas;
        this.stage = stage;
        this.shapeDrawer = shapeDrawer;
        this.roadCam = roadCam;
        this.road = road;
    }

    public List<Entity> environment() {
        Entity sky = new Entity();
        Actor skyImg = new EnvironmentParallaxImage(textureAtlas.findRegion("environment/sky"), 0f, roadCam, road);
        skyImg.setScale(Setup.SPRITE_SCALE);
        sky.add(new ActorComponent(skyImg, Layer.SKY));
        Entity stars = new Entity();
        Actor starsImg =
                new EnvironmentParallaxImage(textureAtlas.findRegion("environment/stars"), 1.2f, roadCam, road);
        starsImg.setScale(Setup.SPRITE_SCALE);
        stars.add(new ActorComponent(starsImg, Layer.STARS));
        Entity bg0 = new Entity();
        Actor bg0Img =
                new EnvironmentParallaxImage(textureAtlas.findRegion("environment/background0"), 3f, roadCam, road);
        bg0Img.setScale(Setup.SPRITE_SCALE);
        bg0.add(new ActorComponent(bg0Img, Layer.BG_0));
        Entity bg1 = new Entity();
        Actor bg1Img =
                new EnvironmentParallaxImage(textureAtlas.findRegion("environment/background1"), 3.4f, roadCam, road);
        bg1Img.setScale(Setup.SPRITE_SCALE);
        bg1.add(new ActorComponent(bg1Img, Layer.BG_1));
        Entity bg2 = new Entity();
        Actor bg2Img =
                new EnvironmentParallaxImage(textureAtlas.findRegion("environment/background2"), 3.8f, roadCam, road);
        bg2Img.setScale(Setup.SPRITE_SCALE);
        bg2.add(new ActorComponent(bg2Img, Layer.BG_2));
        Entity ground = new Entity();
        Actor groundImg =
                new EnvironmentParallaxImage(textureAtlas.findRegion("environment/ground"), 4f, roadCam, road);
        groundImg.setScale(Setup.SPRITE_SCALE);
        ground.add(new ActorComponent(groundImg, Layer.GROUND));
        return Arrays.asList(sky, stars, bg0, bg1, bg2, ground);
    }

    public Entity player() {
        Entity entity = new Entity();
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions("player/straight");
        Array<Drawable> drawables = new Array<>(regions.size);
        for (TextureAtlas.AtlasRegion region : regions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        FixedFrameAnimation<Drawable> animation = new FixedFrameAnimation<>(0.1f, drawables, Animation.PlayMode.LOOP);
        ScaledAnimatedImage image = new ScaledAnimatedImage(animation);
        image.setScale(Setup.SPRITE_SCALE);
        image.setOrigin(Align.center | Align.bottom);
        image.setPosition(stage.getWidth() / 2, 0, Align.center | Align.bottom);
        entity.add(new ActorComponent(image, Layer.PLAYER));
        entity.add(new PlayerRequestComponent());
        entity.add(new AnimatedImageComponent(image));
        return entity;
    }

    public Entity road() {
        Entity entity = new Entity();
        ShapeDrawerDrawable drawable = new RoadDrawable(shapeDrawer, road, roadCam);
        Image image = new Image(drawable);
        image.setSize(stage.getWidth(), stage.getHeight());
        entity.add(new ActorComponent(image, Layer.ROAD));
        return entity;
    }

    private void addSegment(Array<Segment> segments, int position, float curve) {
        Color color = Setup.ROAD_COLORS[(position / Setup.RUMBLE_LENGTH) % Setup.ROAD_COLORS.length];
        Segment segment =
                new Segment(position * Setup.SEGMENT_LENGTH, (position + 1) * Setup.SEGMENT_LENGTH, color, curve);
        segments.add(segment);
    }

    private int addRoad(Array<Segment> segments, int basePosition, int enter, int hold, int leave, float curve) {
        for (int i = 0; i < enter; i++) {
            addSegment(segments, i + basePosition, Interpolation.pow2In.apply(0, curve, (float) i / enter));
        }

        for (int i = 0; i < hold; i++) {
            addSegment(segments, i + enter + basePosition, curve);
        }
        for (int i = 0; i < leave; i++) {
            addSegment(
                    segments,
                    i + enter + hold + basePosition,
                    Interpolation.pow2Out.apply(curve, 0, (float) i / leave));
        }

        return enter + hold + leave;
    }
}
