package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Straight;
import io.github.fourlastor.game.level.input.state.Turning;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class PlayerComponent implements Component {
    public final InputStateMachine stateMachine;
    public final Straight straight;
    public final Turning turning;

    public float speed = 0;

    public PlayerComponent(InputStateMachine stateMachine, Straight straight, Turning turning) {
        this.stateMachine = stateMachine;
        this.straight = straight;
        this.turning = turning;
    }
}
