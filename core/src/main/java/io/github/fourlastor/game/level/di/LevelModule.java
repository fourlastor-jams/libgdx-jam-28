package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.level.Layer;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.input.PlayerInputSystem;
import io.github.fourlastor.game.level.road.RoadCam;
import io.github.fourlastor.game.level.system.ClearScreenSystem;
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
            PlayerInputSystem playerInputSystem, StageSystem stageSystem, ClearScreenSystem clearScreenSystem) {
        Engine engine = new Engine();
        engine.addSystem(playerInputSystem);
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
