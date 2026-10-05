package src;
/**
 * Triangle class
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-10-05
 *  
*/
public class Triangle {
    private Vector3 v1;
    private Vector3 v2;
    private Vector3 v3;

    /**
     * Constructs a triangle with the specified vertices.
     * @param v1 The first vertex of the triangle.
     * @param v2 The second vertex of the triangle.
     * @param v3 The third vertex of the triangle.
     */
    public Triangle(Vector3 v1, Vector3 v2, Vector3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Constructs a triangle with default vertices at the origin.
     */
    public Triangle() {
        this.v1 = new Vector3();
        this.v2 = new Vector3();
        this.v3 = new Vector3();
    }

    /**
     * Returns the first vertex of the triangle.
     * @return The first vertex of the triangle.
     */
    public Vector3 getV1() {
        return v1;
    }

    /**
     * Returns the second vertex of the triangle.
     * @return The second vertex of the triangle.
     */
    public Vector3 getV2() {
        return v2;
    }

    /**
     * Returns the third vertex of the triangle.
     * @return The third vertex of the triangle.
     */
    public Vector3 getV3() {
        return v3;
    }
}