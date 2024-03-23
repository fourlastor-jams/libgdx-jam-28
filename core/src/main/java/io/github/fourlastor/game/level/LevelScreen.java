package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.route.Router;
import javax.inject.Inject;

public class LevelScreen extends ScreenAdapter implements Telegraph {

    private final InputMultiplexer multiplexer;
    private final Router router;

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;
    private final MessageDispatcher dispatcher;
    private Entity player;
    private boolean died;
    private InputAdapter processor;

    @Inject
    public LevelScreen(
            InputMultiplexer multiplexer,
            Router router,
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            MessageDispatcher dispatcher) {
        this.multiplexer = multiplexer;
        this.router = router;
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.dispatcher = dispatcher;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void show() {
        engine.addEntity(entitiesFactory.road());
        player = entitiesFactory.player();
        engine.addEntity(player);
        engine.addEntity(entitiesFactory.lapCounter());
        for (Entity entity : entitiesFactory.environment()) {
            engine.addEntity(entity);
        }
        dispatcher.addListener(this, Message.DIED_BY_LAVA.ordinal());
        processor = new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.R && died) {
                    router.goToLevel();
                }
                return super.keyUp(keycode);
            }
        };
        multiplexer.addProcessor(processor);
    }

    @Override
    public void hide() {
        multiplexer.removeProcessor(processor);
        engine.removeAllEntities();
        engine.removeAllSystems();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.DIED_BY_LAVA.ordinal()) {
            engine.removeEntity(player);
            engine.addEntity(entitiesFactory.gameOver());
            died = true;
            return true;
        }
        return false;
    }
}
