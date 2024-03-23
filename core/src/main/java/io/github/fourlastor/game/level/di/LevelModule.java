package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.level.Layer;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.input.PlayerInputSystem;
import io.github.fourlastor.game.level.road.Road;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.road.Segment;
import io.github.fourlastor.game.level.system.ClearScreenSystem;
import io.github.fourlastor.game.level.system.CurveSystem;
import io.github.fourlastor.game.level.system.LapSystem;
import io.github.fourlastor.harlequin.system.StageSystem;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Named;
import javax.inject.Qualifier;
import space.earlygrey.shapedrawer.ShapeDrawer;

@Module
public class LevelModule {

    @Provides
    @ScreenScoped
    public Engine engine(
            PlayerInputSystem playerInputSystem,
            StageSystem stageSystem,
            ClearScreenSystem clearScreenSystem,
            CurveSystem curveSystem,
            LapSystem lapSystem) {
        Engine engine = new Engine();
        engine.addSystem(curveSystem);
        engine.addSystem(playerInputSystem);
        engine.addSystem(lapSystem);
        engine.addSystem(clearScreenSystem);
        engine.addSystem(stageSystem);
        return engine;
    }

    @Provides
    public StageSystem stageSystem(Stage stage, @Layers Class<? extends Enum<?>> layers) {
        return new StageSystem(stage, layers);
    }

    @Provides
    @Layers
    public Class<? extends Enum<?>> layersEnum() {
        return Layer.class;
    }

    @Provides
    @ScreenScoped
    public Viewport viewport() {
        return new FitViewport(640, 360);
    }

    @Provides
    @ScreenScoped
    public RoadCam roadCam() {
        return new RoadCam(Setup.INITIAL_CAMERA_Y, Setup.INITIAL_CAMERA_DEPTH);
    }

    @Provides
    @ScreenScoped
    public Road road() {
        Array<Segment> segments = new Array<>();
        int position = 0;
        position += addRoad(segments, position, 0, Setup.SEGMENTS_VISIBLE, 0, 0);

        position += addRoad(segments, position, 150, 150, 150, -2);
        position += addRoad(segments, position, 150, 150, 150, 1);
        position += addRoad(segments, position, 0, 25, 0, 0);
        position += addRoad(segments, position, 50, 100, 75, -3);
        position += addRoad(segments, position, 0, 25, 0, 0);
        position += addRoad(segments, position, 150, 150, 75, 2);
        position += addRoad(segments, position, 150, 150, 150, -2);
        position += addRoad(segments, position, 0, 25, 0, 0);

        addRoad(segments, position, 0, Setup.SEGMENTS_VISIBLE, 0, 0);

        return new Road(segments);
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

    @Provides
    @ScreenScoped
    public Batch batch() {
        return new SpriteBatch();
    }

    @Provides
    @ScreenScoped
    public ShapeDrawer shapeDrawer(Batch batch, @Named(AssetsModule.WHITE_PIXEL) TextureRegion white) {
        return new ShapeDrawer(batch, white);
    }

    @Provides
    @ScreenScoped
    public Stage stage(Viewport viewport, Batch batch) {
        return new Stage(viewport, batch);
    }

    @Provides
    @ScreenScoped
    public Camera camera(Viewport viewport) {
        return viewport.getCamera();
    }

    @Provides
    @ScreenScoped
    @Gravity
    public Vector2 gravity() {
        return new Vector2(0f, -10f);
    }

    @Provides
    @ScreenScoped
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Layers {}
}
