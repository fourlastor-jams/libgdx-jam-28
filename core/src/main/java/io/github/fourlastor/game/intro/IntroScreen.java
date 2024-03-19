package io.github.fourlastor.game.intro;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.di.modules.AssetsModule;
import javax.inject.Inject;
import javax.inject.Named;

import io.github.fourlastor.game.level.road.RoadDrawable;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class IntroScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(Color.BLACK);

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;

    @Inject
    public IntroScreen(InputMultiplexer inputMultiplexer, @Named(AssetsModule.WHITE_PIXEL) TextureRegion white) {
        this.inputMultiplexer = inputMultiplexer;
        viewport = new FitViewport(640, 320);
        Batch batch = new PolygonSpriteBatch();
        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(processor);
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            System.out.println("Go to level");
            return true;
        }
    };

    @Override
    public void render(float delta) {
        ScreenUtils.clear(CLEAR_COLOR, true);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }
}
