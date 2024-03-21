package io.github.fourlastor.game.actor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

public class ScaledAnimatedImage extends AnimatedImage {

    private float speed = 1;

    public ScaledAnimatedImage(Animation<? extends Drawable> animation) {
        super(animation);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void act(float delta) {
        super.act(delta * speed);
    }
}
