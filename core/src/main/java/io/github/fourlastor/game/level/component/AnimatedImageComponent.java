package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.actor.ScaledAnimatedImage;

public class AnimatedImageComponent implements Component {

    public final ScaledAnimatedImage image;

    public AnimatedImageComponent(ScaledAnimatedImage image) {
        this.image = image;
    }
}
