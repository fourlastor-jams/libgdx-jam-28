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

public class Straight extends Base {

    private final Animation<Drawable> animation;

    @Inject
    public Straight(Dependencies dependencies) {
        super(dependencies);
        animation = animation("straight");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        ScaledAnimatedImage image = images().get(entity).image;
        image.setAnimation(animation);
        image.setScaleX(Setup.SPRITE_SCALE);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        PlayerComponent player = players().get(entity);
        if (player.speed > 0 && (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
            player.stateMachine.changeState(player.turning);
        }
    }
}
