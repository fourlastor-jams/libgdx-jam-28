package io.github.fourlastor.game.intro;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Segment {

    private final Point p1;
    private final Point p2;

    private final Color color;

    private final Polygon shape = new Polygon(new float[] {0, 0, 0, 0, 0, 0, 0, 0});

    public Segment(float z1, float z2, Color color) {
        p1 = new Point(z1);
        p2 = new Point(z2);
        this.color = color;
    }

    public void project(
            float cameraX,
            float cameraY,
            float cameraZ,
            float cameraDepth,
            float width,
            float height,
            float roadWidth) {
        p1.project(cameraX, cameraY, cameraZ, cameraDepth, width, height, roadWidth);
        p2.project(cameraX, cameraY, cameraZ, cameraDepth, width, height, roadWidth);
        shape.setVertex(0, p1.screen.x - p1.projectedWidth, p1.screen.y);
        shape.setVertex(1, p1.screen.x + p1.projectedWidth, p1.screen.y);
        shape.setVertex(2, p2.screen.x + p2.projectedWidth, p2.screen.y);
        shape.setVertex(3, p2.screen.x - p2.projectedWidth, p2.screen.y);
    }

    public void render(ShapeDrawer drawer) {
        drawer.setColor(color);
        drawer.filledPolygon(shape);
    }

    public static class Point {

        public final Vector3 world = new Vector3();
        public final Vector3 camera = new Vector3();
        public final Vector2 screen = new Vector2();
        public float projectedWidth = 0f;

        public Point(float z) {
            world.z = z;
        }

        public void project(
                float cameraX,
                float cameraY,
                float cameraZ,
                float cameraDepth,
                float width,
                float height,
                float roadWidth) {
            camera.set(world).sub(cameraX, cameraY, cameraZ);
            float scale = cameraDepth / camera.z;
            screen.set(
                    Math.round(width / 2f + scale * camera.x * width / 2f),
                    Math.round(height / 2f + scale * camera.y * height / 2f));
            projectedWidth = Math.round(scale * roadWidth * width / 2f);
        }
    }
}
