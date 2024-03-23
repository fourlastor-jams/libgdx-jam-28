package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import io.github.fourlastor.game.level.Setup;
import io.github.fourlastor.game.level.component.GroundComponent;
import io.github.fourlastor.game.level.component.LapComponent;
import io.github.fourlastor.game.level.road.Road;
import io.github.fourlastor.game.level.road.RoadCam;
import javax.inject.Inject;
import text.formic.Stringf;

public class LapSystem extends EntitySystem {

    private static final Family FAMILY_LAP = Family.all(LapComponent.class).get();
    private static final Family FAMILY_GROUND =
            Family.all(GroundComponent.class).get();
    private final ComponentMapper<LapComponent> laps;
    private final ComponentMapper<GroundComponent> grounds;

    private final RoadCam roadCam;
    private final Road road;

    private int lap;
    private float lapTime;

    private LapComponent lapComponent;
    private GroundComponent groundComponent;
    private EntityListener lapListener;
    private EntityListener groundListener;

    @Inject
    public LapSystem(
            ComponentMapper<LapComponent> laps, ComponentMapper<GroundComponent> grounds, RoadCam roadCam, Road road) {
        this.laps = laps;
        this.grounds = grounds;
        this.roadCam = roadCam;
        this.road = road;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        lapListener = new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                lapComponent = laps.get(entity);
            }

            @Override
            public void entityRemoved(Entity entity) {}
        };
        engine.addEntityListener(FAMILY_LAP, lapListener);
        groundListener = new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                groundComponent = grounds.get(entity);
            }

            @Override
            public void entityRemoved(Entity entity) {}
        };
        engine.addEntityListener(FAMILY_GROUND, groundListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(lapListener);
        engine.removeEntityListener(groundListener);
        lapComponent = null;
        groundComponent = null;
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        lapTime += deltaTime;
        float nextLapZ = road.totalLength * (lap + 1);
        if (roadCam.position.z > nextLapZ) {
            lap += 1;
            if (lapComponent != null) {
                lapComponent.label.setText(Stringf.format("Lap %d (%.2fs)", lap, lapTime));
            }
            if (groundComponent != null) {
                road.floorIsLava = !road.floorIsLava;
                groundComponent.image.setImage(road.floorIsLava ? groundComponent.lava : groundComponent.ground);
                groundComponent.image.setScale(Setup.SPRITE_SCALE);
            }
            lapTime = 0;
        }
    }
}
