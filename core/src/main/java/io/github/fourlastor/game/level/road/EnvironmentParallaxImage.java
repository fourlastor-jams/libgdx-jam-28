package io.github.fourlastor.game.level.road;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import java.util.Objects;

/**
 * An {@link Actor} which draws the image according to the parallax factor.
 * A factor of 1 means there is no parallax.
 * Smaller factors make the image move slower.
 */
public class EnvironmentParallaxImage extends Actor {

    private final float factor;

    private float currentDelta = 0;
    private final TiledDrawable drawable;
    private final RoadCam cam;
    private final Road road;

    public EnvironmentParallaxImage(TextureRegion textureRegion, float factor, RoadCam cam, Road road) {
        super();
        this.cam = cam;
        this.road = road;
        TiledDrawable drawable = new TiledDrawable(textureRegion);
        setBounds(0f, 0f, drawable.getMinWidth(), drawable.getMinHeight());
        setPosition(0f, 0f);
        this.drawable = drawable;
        this.factor = factor;
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        drawable.setScale(scaleXY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Segment segment = road.segments.get(road.findSegmentIndex(cam.position.z));
        currentDelta += -segment.curve * factor * cam.speedPercent;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Camera camera = Objects.requireNonNull(getStage()).getCamera();

        float targetWidth = getWidth() * getScaleX();
        float targetHeight = getHeight() * getScaleY();
        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;
        float maxX = camera.viewportWidth + startX;
        float maxY = camera.viewportHeight + startY;
        float dX = currentDelta % targetWidth;
        float x = startX + dX - targetWidth;
        float y = startY - targetHeight;
        drawable.draw(batch, x, y, maxX - x, maxY - y);
    }
}
