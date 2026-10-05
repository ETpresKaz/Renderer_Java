package src;
import javax.swing.*;
import java.awt.*;

/**
 * A simple wireframe 3D renderer.
 *
 * Every frame it does three steps for each point of the model:
 *   1. Rotate   - spin the point around the center of the model.
 *   2. Project  - flatten the 3D point onto the 2D screen (perspective).
 *   3. Draw     - connect projected points with lines (the "edges").
 *
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-10-05
 */
public class Renderer extends JPanel {
    /** How far the camera sits back from the model. Bigger = model looks smaller. */
    private static final double CAMERA_DISTANCE = 4;

    /** Field-of-view scale. Bigger = more zoomed in. */
    private static final double FOV = 400;

    private Vector3[] vertices;
    private int[][] edges;
    private double angle = 0;

    /**
     * Constructs a renderer for a wireframe model.
     * @param vertices The corner points of the model.
     * @param edges Pairs of indices into vertices; each pair is drawn as a line.
     */
    public Renderer(Vector3[] vertices, int[][] edges) {
        this.vertices = vertices;
        this.edges = edges;
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
     * Called by Swing whenever the panel needs to be redrawn.
     * @param g The graphics context to draw with.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clears the old frame
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);

        // Steps 1 and 2: rotate and project every vertex once.
        Point[] projected = new Point[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            Vector3 rotated = rotateX(rotateY(vertices[i], angle), angle * 0.5);
            projected[i] = project(rotated);
        }

        // Step 3: draw a line for every edge.
        for (int[] edge : edges) {
            Point a = projected[edge[0]];
            Point b = projected[edge[1]];
            g2.drawLine(a.x, a.y, b.x, b.y);
        }
    }
}
