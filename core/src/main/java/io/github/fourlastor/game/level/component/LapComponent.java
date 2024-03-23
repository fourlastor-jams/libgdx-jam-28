package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LapComponent implements Component {
    public final Label label;

    public LapComponent(Label label) {
        this.label = label;
    }
}
