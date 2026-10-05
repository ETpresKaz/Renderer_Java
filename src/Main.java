package src;
import javax.swing.*;

/**
 * The main class for the 3D renderer.
 * @author Ethan Kazenske
 * @version 1.3
 * @since 2026-10-05
 */
class Main {
    public static void main(String[] args) {
        // Pick which shape to draw. Try swapping in makePyramid().
        Triangle[] shape = makeCube();

        JFrame frame = new JFrame("Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Renderer(shape));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Builds a cube out of 12 triangles (2 per face).
     *
     * Corner order matters: list each triangle's corners counter-clockwise when
     * looking at it from outside the shape. That way the renderer knows which
     * side is the front.
     * @return The triangles of the cube.
     */
    static Triangle[] makeCube() {
        // The 8 corners of a cube centered at the origin.
        Vector3 p0 = new Vector3(-1, -1, -1);
        Vector3 p1 = new Vector3( 1, -1, -1);
        Vector3 p2 = new Vector3( 1,  1, -1);
        Vector3 p3 = new Vector3(-1,  1, -1);
        Vector3 p4 = new Vector3(-1, -1,  1);
        Vector3 p5 = new Vector3( 1, -1,  1);
        Vector3 p6 = new Vector3( 1,  1,  1);
        Vector3 p7 = new Vector3(-1,  1,  1);

        return new Triangle[] {
                new Triangle(p0, p3, p2), new Triangle(p0, p2, p1), // back   (z = -1)
                new Triangle(p4, p5, p6), new Triangle(p4, p6, p7), // front  (z = +1)
                new Triangle(p0, p4, p7), new Triangle(p0, p7, p3), // left   (x = -1)
                new Triangle(p1, p2, p6), new Triangle(p1, p6, p5), // right  (x = +1)
                new Triangle(p3, p7, p6), new Triangle(p3, p6, p2), // top    (y = +1)
                new Triangle(p0, p1, p5), new Triangle(p0, p5, p4)  // bottom (y = -1)
        };
    }

    /**
     * Builds a square-based pyramid out of 6 triangles (2 for the base, 4 sides).
     * @return The triangles of the pyramid.
     */
    static Triangle[] makePyramid() {
        Vector3 a = new Vector3(-1, -1, -1);
        Vector3 b = new Vector3( 1, -1, -1);
        Vector3 c = new Vector3( 1, -1,  1);
        Vector3 d = new Vector3(-1, -1,  1);
        Vector3 top = new Vector3(0, 1, 0);

        return new Triangle[] {
                new Triangle(a, b, c), new Triangle(a, c, d), // base
                new Triangle(a, top, b),                      // side facing -z
                new Triangle(b, top, c),                      // side facing +x
                new Triangle(c, top, d),                      // side facing +z
                new Triangle(d, top, a)                       // side facing -x
        };
    }
}
