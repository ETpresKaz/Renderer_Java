package src;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple 3D renderer that draws shapes made of filled triangles.
 *
 * Every frame it does these steps for each triangle of the model:
 *   1. Rotate   - spin the triangle's corners around the center of the model.
 *   2. Cull     - skip triangles that face away from the camera (you can't see them).
 *   3. Light    - make triangles that face the light brighter.
 *   4. Project  - flatten the 3D corners onto the 2D screen (perspective).
 *   5. Draw     - fill the triangles, farthest first, so near ones cover far ones.
 *
 * @author Ethan Kazenske
 * @version 1.1
 * @since 2026-10-05
 */
public class Renderer extends JPanel {
    /** How far the camera sits back from the model. Bigger = model looks smaller. */
    private static final double CAMERA_DISTANCE = 4;

    /** Field-of-view scale. Bigger = more zoomed in. */
    private static final double FOV = 400;

    /** Where the camera is. It sits in front of the model, looking toward +z. */
    private static final Vector3 CAMERA = new Vector3(0, 0, -CAMERA_DISTANCE);

    /** Direction pointing toward the light: up, to the left, and toward the camera. */
    private static final Vector3 LIGHT = new Vector3(-1, 1, -1).normalize();

    /** Minimum brightness, so faces in shadow aren't pitch black. */
    private static final double AMBIENT = 0.2;

    /** The base color of the model before lighting. */
    private static final Color BASE_COLOR = new Color(80, 200, 120);

    private Triangle[] triangles;
    private double angle = 0;

    /**
     * A triangle that is ready to draw: its flat screen shape, its shaded color,
     * and how far away it is (used to decide what order to draw in).
     */
    private record Face(Polygon shape, Color color, double depth) {}

    /**
     * Constructs a renderer for a model made of triangles.
     * @param triangles The triangles that make up the model.
     */
    public Renderer(Triangle[] triangles) {
        this.triangles = triangles;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // Ask Swing to redraw about 60 times per second, nudging the angle each time.
        Timer timer = new Timer(16, e -> {
            angle += 0.02;
            repaint();
        });
        timer.start();
    }

    /**
     * Rotates a point around the Y axis (the vertical axis), like a spinning top.
     * @param v The point to rotate.
     * @param a The angle in radians.
     * @return A new rotated point.
     */
    private Vector3 rotateY(Vector3 v, double a) {
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Vector3(
                v.getX() * cos + v.getZ() * sin,
                v.getY(),
                -v.getX() * sin + v.getZ() * cos
        );
    }

    /**
     * Rotates a point around the X axis (the horizontal axis), like a tumbling wheel.
     * @param v The point to rotate.
     * @param a The angle in radians.
     * @return A new rotated point.
     */
    private Vector3 rotateX(Vector3 v, double a) {
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Vector3(
                v.getX(),
                v.getY() * cos - v.getZ() * sin,
                v.getY() * sin + v.getZ() * cos
        );
    }

    /**
     * Applies this frame's rotation to a point.
     * @param v The point to rotate.
     * @return A new rotated point.
     */
    private Vector3 rotate(Vector3 v) {
        return rotateX(rotateY(v, angle), angle * 0.5);
    }

    /**
     * Projects a 3D point onto the 2D screen using perspective.
     * Points farther away (bigger z) get divided by a bigger number, so they shrink
     * toward the center of the screen. That shrinking is what makes it look 3D.
     * @param v The 3D point.
     * @return The pixel position on the screen.
     */
    private Point project(Vector3 v) {
        double scale = FOV / (v.getZ() + CAMERA_DISTANCE);
        int screenX = (int) (v.getX() * scale + getWidth() / 2.0);
        // Screen y grows downward, but 3D y grows upward, so flip it.
        int screenY = (int) (-v.getY() * scale + getHeight() / 2.0);
        return new Point(screenX, screenY);
    }

    /**
     * Darkens or brightens a color by a factor between 0 (black) and 1 (full color).
     * @param c The color to shade.
     * @param brightness How bright to make it, from 0 to 1.
     * @return The shaded color.
     */
    private Color shade(Color c, double brightness) {
        return new Color(
                (int) (c.getRed() * brightness),
                (int) (c.getGreen() * brightness),
                (int) (c.getBlue() * brightness)
        );
    }

    /**
     * Called by Swing whenever the panel needs to be redrawn.
     * @param g The graphics context to draw with.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clears the old frame
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Face> faces = new ArrayList<>();
        for (Triangle t : triangles) {
            // Step 1: rotate the three corners.
            Vector3 a = rotate(t.getV1());
            Vector3 b = rotate(t.getV2());
            Vector3 c = rotate(t.getV3());

            // The normal is an arrow sticking straight out of the triangle's front side.
            // The cross product of two edges gives exactly that.
            Vector3 normal = b.subtract(a).cross(c.subtract(a)).normalize();

            // Step 2: if the normal points away from the camera, we're looking at the
            // back of the triangle, which is hidden inside the shape. Skip it.
            Vector3 toTriangle = a.subtract(CAMERA);
            if (normal.dot(toTriangle) >= 0) {
                continue;
            }

            // Step 3: the dot product is 1 when the face points right at the light,
            // 0 when it's sideways, and negative when it faces away.
            double brightness = Math.max(0, normal.dot(LIGHT));
            brightness = AMBIENT + (1 - AMBIENT) * brightness;

            // Step 4: project the corners to screen pixels.
            Point pa = project(a);
            Point pb = project(b);
            Point pc = project(c);
            Polygon shape = new Polygon(
                    new int[] {pa.x, pb.x, pc.x},
                    new int[] {pa.y, pb.y, pc.y},
                    3
            );

            double depth = (a.getZ() + b.getZ() + c.getZ()) / 3;
            faces.add(new Face(shape, shade(BASE_COLOR, brightness), depth));
        }

        // Step 5: draw the farthest triangles first (the "painter's algorithm"),
        // so closer triangles get painted on top of them.
        faces.sort((f1, f2) -> Double.compare(f2.depth(), f1.depth()));
        for (Face f : faces) {
            g2.setColor(f.color());
            g2.fillPolygon(f.shape());
            // Outline in the same color to hide thin gaps between neighboring triangles.
            g2.drawPolygon(f.shape());
        }
    }
}
