package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.fourlastor.game.level.road.EnvironmentParallaxImage;

public class GroundComponent implements Component {
    public final EnvironmentParallaxImage image;
    public final TextureRegion ground;
    public final TextureRegion lava;

    public GroundComponent(EnvironmentParallaxImage image, TextureRegion ground, TextureRegion lava) {
        this.image = image;
        this.ground = ground;
        this.lava = lava;
    }
}
