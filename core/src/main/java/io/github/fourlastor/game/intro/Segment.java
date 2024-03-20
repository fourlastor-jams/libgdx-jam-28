package io.github.fourlastor.game.intro;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Segment {

    private final Point p1;
    private final Point p2;

    private final Color color;

    public final float curve;

    private final Polygon shape = new Polygon(new float[] {0, 0, 0, 0, 0, 0, 0, 0});

    public Segment(float z1, float z2, Color color, float curve) {
        p1 = new Point(z1);
        p2 = new Point(z2);
        this.color = color;
        this.curve = curve;
    }

    public void project(
            float cameraX,
            float cameraY,
            float cameraZ,
            float curve,
            float dCurve,
            float cameraDepth,
            float width,
            float height,
            float roadWidth,
            float x,
            float y) {
        p1.project(cameraX - curve, cameraY, cameraZ, cameraDepth, width, height, roadWidth);
        p2.project(cameraX - curve - dCurve, cameraY, cameraZ, cameraDepth, width, height, roadWidth);
        shape.setVertex(0, x + p1.screen.x - p1.projectedWidth, y + p1.screen.y);
        shape.setVertex(1, x + p1.screen.x + p1.projectedWidth, y + p1.screen.y);
        shape.setVertex(2, x + p2.screen.x + p2.projectedWidth, y + p2.screen.y);
        shape.setVertex(3, x + p2.screen.x - p2.projectedWidth, y + p2.screen.y);
    }

    public void render(ShapeDrawer drawer) {
        drawer.setColor(color);
        drawer.filledPolygon(shape);
    }

    private final Vector2 shapeVertex = new Vector2();

    public boolean inside(Rectangle screenRectangle) {
        for (int i = 0; i < 4; i++) {
            Vector2 vertex = shape.getVertex(i, shapeVertex);
            if (screenRectangle.contains(screenRectangle.x + screenRectangle.width / 2, vertex.y)) {
                return true;
            }
        }
        return false;
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
