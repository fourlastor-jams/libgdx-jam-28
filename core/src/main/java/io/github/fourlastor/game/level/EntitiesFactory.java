package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.road.RoadDrawable;
import io.github.fourlastor.game.level.road.Segment;
import io.github.fourlastor.harlequin.component.ActorComponent;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

import javax.inject.Inject;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private static final float SCALE_XY = 1f / 32f;
    private final TextureAtlas textureAtlas;

    private final Stage stage;
    private final ShapeDrawer shapeDrawer;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas,
            Stage stage,
            ShapeDrawer shapeDrawer
    ) {
        this.textureAtlas = textureAtlas;
        this.stage = stage;
        this.shapeDrawer = shapeDrawer;
    }

    public Entity road() {
        Entity entity = new Entity();
        Array<Segment> segments = new Array<>();
        int position = 0;
        position += addRoad(segments, position, 150, 150, 150, 1);
        position += addRoad(segments, position, 0, 25, 0, 0);
        addRoad(segments, position, 150, 150, 150, -2);
        ShapeDrawerDrawable drawable = new RoadDrawable(shapeDrawer, segments);
        Image image = new Image(drawable);
        image.setSize(stage.getWidth(), stage.getHeight());
        entity.add(new ActorComponent(image, Layer.ROAD));
        return entity;
    }

    private void addSegment(Array<Segment> segments, int position, float curve) {
        Color color = Setup.COLORS[(position / Setup.RUMBLE_LENGTH) % Setup.COLORS.length];
        Segment segment = new Segment(position * Setup.SEGMENT_LENGTH, (position + 1) * Setup.SEGMENT_LENGTH, color, curve);
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
            addSegment(segments, i + enter + hold + basePosition, Interpolation.pow2Out.apply(curve, 0, (float) i / leave));
        }

        return enter + hold + leave;
    }
}
