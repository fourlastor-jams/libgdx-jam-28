package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.level.component.PlayerRequestComponent;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.road.RoadDrawable;
import io.github.fourlastor.game.level.road.Segment;
import io.github.fourlastor.harlequin.component.ActorComponent;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private static final float SPRITE_SCALE = 2;

    private final TextureAtlas textureAtlas;

    private final Stage stage;
    private final ShapeDrawer shapeDrawer;
    private final TextureRegion white;
    private final RoadCam roadCam;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas,
            Stage stage,
            ShapeDrawer shapeDrawer,
            @Named(AssetsModule.WHITE_PIXEL) TextureRegion white,
            RoadCam roadCam) {
        this.textureAtlas = textureAtlas;
        this.stage = stage;
        this.shapeDrawer = shapeDrawer;
        this.white = white;
        this.roadCam = roadCam;
    }

    public List<Entity> environment() {
        Entity sky = new Entity();
        Image skyImg = new Image(textureAtlas.findRegion("environment/sky"));
        skyImg.setScale(SPRITE_SCALE);
        sky.add(new ActorComponent(skyImg, Layer.SKY));
        Entity bg0 = new Entity();
        Image bg0Img = new Image(textureAtlas.findRegion("environment/background0"));
        bg0Img.setScale(SPRITE_SCALE);
        bg0.add(new ActorComponent(bg0Img, Layer.BG_0));
        Entity bg1 = new Entity();
        Image bg1Img = new Image(textureAtlas.findRegion("environment/background1"));
        bg1Img.setScale(SPRITE_SCALE);
        bg1.add(new ActorComponent(bg1Img, Layer.BG_1));
        Entity bg2 = new Entity();
        Image bg2Img = new Image(textureAtlas.findRegion("environment/background2"));
        bg2Img.setScale(SPRITE_SCALE);
        bg2.add(new ActorComponent(bg2Img, Layer.BG_2));
        Entity ground = new Entity();
        Image groundImg = new Image(textureAtlas.findRegion("environment/ground"));
        groundImg.setScale(SPRITE_SCALE);
        ground.add(new ActorComponent(groundImg, Layer.GROUND));
        return Arrays.asList(sky, bg0, bg1, bg2, ground);
    }

    public Entity player() {
        Entity entity = new Entity();
        Image image = new Image(white);
        image.setSize(50, 80);
        image.setPosition(stage.getWidth() / 2, 0, Align.center | Align.bottom);
        entity.add(new ActorComponent(image, Layer.PLAYER));
        entity.add(new PlayerRequestComponent());
        return entity;
    }

    public Entity road() {
        Entity entity = new Entity();
        Array<Segment> segments = new Array<>();
        int position = 0;
        position += addRoad(segments, position, 150, 150, 150, 1);
        position += addRoad(segments, position, 0, 25, 0, 0);
        addRoad(segments, position, 150, 150, 150, -2);
        ShapeDrawerDrawable drawable = new RoadDrawable(shapeDrawer, segments, roadCam);
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
