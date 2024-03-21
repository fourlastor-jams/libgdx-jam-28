package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.game.actor.ScaledAnimatedImage;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.component.PlayerComponent;
import io.github.fourlastor.harlequin.animation.Animation;
import javax.inject.Inject;

public class Turning extends Base {

    private final Animation<Drawable> animation;

    private float timer = 0;
    private boolean animSet = false;

    @Inject
    public Turning(Dependencies dependencies) {
        super(dependencies);
        animation = animation("turning");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timer = 0;
        animSet = false;
        //        images().get(entity).image.setAnimation(animation);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        ScaledAnimatedImage image = images().get(entity).image;
        if (!animSet) {
            timer += getDelta();
            if (timer >= Setup.PLAYER_STEERING_ANIMATION_DELAY) {
                image.setAnimation(animation);
                animSet = true;
            }
        }

        PlayerComponent player = players().get(entity);
        float turnSpeed = 0;
        float speedPercent = player.speed / Setup.PLAYER_MAX_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            turnSpeed = -Setup.PLAYER_STEERING * speedPercent;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            turnSpeed = Setup.PLAYER_STEERING * speedPercent;
        } else {
            player.stateMachine.changeState(player.straight);
        }
        cam().position.x += turnSpeed;
        if (turnSpeed >= 0) {
            image.setScaleX(Setup.SPRITE_SCALE);
        } else {
            image.setScaleX(-Setup.SPRITE_SCALE);
        }
    }
}
