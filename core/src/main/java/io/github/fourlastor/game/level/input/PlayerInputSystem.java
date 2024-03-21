package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import io.github.fourlastor.game.level.component.PlayerComponent;
import io.github.fourlastor.game.level.component.PlayerRequestComponent;
import io.github.fourlastor.game.level.input.state.Straight;
import io.github.fourlastor.game.level.input.state.Turning;
import javax.inject.Inject;
import javax.inject.Provider;

public class PlayerInputSystem extends IteratingSystem {

    private static final Family FAMILY_REQUEST =
            Family.all(PlayerRequestComponent.class).get();
    private static final Family FAMILY = Family.all(PlayerComponent.class).get();

    private final PlayerSetup playerSetup;
    private final ComponentMapper<PlayerComponent> players;

    @Inject
    public PlayerInputSystem(PlayerSetup playerSetup, ComponentMapper<PlayerComponent> players) {
        super(FAMILY);
        this.playerSetup = playerSetup;
        this.players = players;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        players.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY_REQUEST, playerSetup);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(playerSetup);
        super.removedFromEngine(engine);
    }

    /**
     * Creates a player component whenever a request to set up a player is made.
     * Takes care of instantiating the state machine and the possible player states.
     */
    public static class PlayerSetup implements EntityListener {

        private final InputStateMachine.Factory stateMachineFactory;
        private final MessageDispatcher messageDispatcher;
        private final Provider<Straight> straightProvider;
        private final Provider<Turning> turningProvider;

        @Inject
        public PlayerSetup(
                InputStateMachine.Factory stateMachineFactory,
                MessageDispatcher messageDispatcher,
                Provider<Straight> straightProvider,
                Provider<Turning> turningProvider) {
            this.stateMachineFactory = stateMachineFactory;
            this.messageDispatcher = messageDispatcher;
            this.straightProvider = straightProvider;
            this.turningProvider = turningProvider;
        }

        @Override
        public void entityAdded(Entity entity) {
            entity.remove(PlayerRequestComponent.class);
            Straight straight = straightProvider.get();
            Turning turning = turningProvider.get();
            InputStateMachine stateMachine = stateMachineFactory.create(entity, straight);

            entity.add(new PlayerComponent(stateMachine, straight, turning));
            stateMachine.getCurrentState().enter(entity);
            // messageDispatcher.addListener(stateMachine, Message.GAME_OVER.ordinal());
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
