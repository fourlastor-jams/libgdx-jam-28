package io.github.fourlastor.game.intro;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.di.modules.AssetsModule;
import javax.inject.Inject;
import javax.inject.Named;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class IntroScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(Color.GRAY);

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;

    @Inject
    public IntroScreen(InputMultiplexer inputMultiplexer, @Named(AssetsModule.WHITE_PIXEL) TextureRegion white) {
        this.inputMultiplexer = inputMultiplexer;

        viewport = new FitViewport(640, 480);
        Batch batch = new PolygonSpriteBatch();
        stage = new Stage(viewport, batch);
        ShapeDrawer shapeDrawer = new ShapeDrawer(batch, white);
        ShapeDrawerDrawable drawable = new MyDrawable(shapeDrawer);
        Image bg = new Image(new TextureRegionDrawable(white).tint(Color.DARK_GRAY));
        bg.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(bg);
        Image image = new Image(drawable);
        image.setSize(stage.getWidth(), stage.getHeight());
        //        image.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        stage.addActor(image);
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

    private static class MyDrawable extends ShapeDrawerDrawable {

        private static final int SEGMENT_LENGTH = 1500;
        private final Array<Segment> segments;

        public MyDrawable(ShapeDrawer shapeDrawer) {
            super(shapeDrawer);
            int segmentsCount = 200;
            int rumbleLength = 15;
            segments = new Array<>(segmentsCount);
            for (int i = 0; i < segmentsCount; i++) {
                int segmentLength = SEGMENT_LENGTH;
                Segment segment = new Segment(
                        i * segmentLength,
                        (i + 1) * segmentLength,
                        (i / rumbleLength) % 2 == 0 ? Color.GRAY : Color.LIGHT_GRAY);
                segments.add(segment);
            }
        }

        private int findSegmentIndex(float z) {
            return MathUtils.ceil(z / SEGMENT_LENGTH) % segments.size;
        }

        private float camZ = 0;

        @Override
        public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
            //            shapeDrawer.setColor(Color.LIME);
            //            shapeDrawer.filledRectangle(x,y, width, height);

            //            shapeDrawer.setColor(Color.RED);
            int segmentIndex = findSegmentIndex(camZ);
            for (int i = segmentIndex; i < segments.size && i - segments.size < 10; i++) {
                Segment segment = segments.get(i);
                segment.project(0, 180, camZ, 140, width, height, width / 4, x + width / 2, y + width / 2);
                segment.render(shapeDrawer);
            }
            //            for (Segment segment : segments) {
            //                segment.project(
            //                        0,80,camZ,
            //                        140, width, height,
            //                        width / 4,
            //                        x + width / 2, y + width / 2
            //                );
            //                segment.render(shapeDrawer);
            //            }
            camZ += 300;
            if (camZ >= 100000) {
                camZ = 0;
            }
        }
    }
}
