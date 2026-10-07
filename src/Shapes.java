package src;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of shapes built out of triangles.
 *
 * Every method returns a Triangle[] that can be handed straight to the Renderer.
 * All shapes are centered at the origin and fit roughly between -1 and 1.
 *
 * Corner order matters: list each triangle's corners counter-clockwise when
 * looking at it from outside the shape. That way the renderer knows which
 * side is the front. If a face goes missing, swap two of its corners.
 *
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-10-05
 */
public class Shapes {

    /**
     * Builds a cube out of 12 triangles (2 per face).
     * @return The triangles of the cube.
     */
    public static Triangle[] cube() {
        // The 8 corners of the cube.
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
    public static Triangle[] pyramid() {
        // The 4 corners of the base, plus the tip.
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

    /**
     * Builds a triangular prism (like a Toblerone bar) out of 8 triangles:
     * a triangle on each end, and 3 rectangular sides made of 2 triangles each.
     * @return The triangles of the prism.
     */
    public static Triangle[] prism() {
        // The triangle at the back end (z = -1)...
        Vector3 a = new Vector3(-1, -1, -1);
        Vector3 b = new Vector3( 1, -1, -1);
        Vector3 c = new Vector3( 0,  1, -1);
        // ...and the same triangle at the front end (z = +1).
        Vector3 d = new Vector3(-1, -1,  1);
        Vector3 e = new Vector3( 1, -1,  1);
        Vector3 f = new Vector3( 0,  1,  1);

        return new Triangle[] {
                new Triangle(a, c, b),                        // back end
                new Triangle(d, e, f),                        // front end
                new Triangle(a, b, e), new Triangle(a, e, d), // bottom
                new Triangle(b, c, f), new Triangle(b, f, e), // right slope
                new Triangle(a, d, f), new Triangle(a, f, c)  // left slope
        };
    }

    /**
     * Builds an octahedron (two pyramids glued base to base, like a diamond)
     * out of 8 triangles.
     * @return The triangles of the octahedron.
     */
    public static Triangle[] octahedron() {
        Vector3 top    = new Vector3( 0,  1,  0);
        Vector3 bottom = new Vector3( 0, -1,  0);
        // The 4 points around the middle.
        Vector3 east   = new Vector3( 1,  0,  0);
        Vector3 south  = new Vector3( 0,  0,  1);
        Vector3 west   = new Vector3(-1,  0,  0);
        Vector3 north  = new Vector3( 0,  0, -1);

        return new Triangle[] {
                // Top half
                new Triangle(east, top, south),
                new Triangle(south, top, west),
                new Triangle(west, top, north),
                new Triangle(north, top, east),
                // Bottom half
                new Triangle(east, south, bottom),
                new Triangle(south, west, bottom),
                new Triangle(west, north, bottom),
                new Triangle(north, east, bottom)
        };
    }

    /**
     * Builds a sphere out of many small triangles, like the lines on a globe.
     *
     * Instead of typing every corner by hand, this calculates them:
     * it slices the sphere into horizontal rings (like latitude lines) and
     * vertical segments (like longitude lines), then fills each little
     * rectangle of the grid with 2 triangles.
     *
     * More rings and segments make it rounder, but slower to draw.
     *
     * @param rings How many horizontal slices (try 12).
     * @param segments How many vertical slices (try 24).
     * @return The triangles of the sphere.
     */
    public static Triangle[] sphere(int rings, int segments) {
        // Step 1: calculate every grid point on the sphere.
        Vector3[][] points = new Vector3[rings + 1][segments + 1];
        for (int i = 0; i <= rings; i++) {
            // theta goes from 0 (top of the sphere) to PI (bottom).
            double theta = Math.PI * i / rings;
            double y = Math.cos(theta);
            double ringRadius = Math.sin(theta); // rings are small near the poles

            for (int j = 0; j <= segments; j++) {
                // phi goes all the way around, from 0 to 2*PI.
                double phi = 2 * Math.PI * j / segments;
                double x = ringRadius * Math.cos(phi);
                double z = ringRadius * Math.sin(phi);
                points[i][j] = new Vector3(x, y, z);
            }
        }

        // Step 2: connect the grid points into triangles.
        List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                // The 4 corners of one little rectangle in the grid.
                Vector3 topLeft     = points[i][j];
                Vector3 topRight    = points[i][j + 1];
                Vector3 bottomRight = points[i + 1][j + 1];
                Vector3 bottomLeft  = points[i + 1][j];

                // At the very top and bottom, the rectangle squishes into a triangle
                // (two of its corners are the same point), so only one triangle fits.
                if (i != 0) {
                    triangles.add(new Triangle(topLeft, topRight, bottomRight));
                }
                if (i != rings - 1) {
                    triangles.add(new Triangle(topLeft, bottomRight, bottomLeft));
                }
            }
        }

        return triangles.toArray(new Triangle[0]);
    }
}
