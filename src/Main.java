package src;
import javax.swing.*;

/**
 * The main class for the 3D renderer.
 */
class Main {
    public static void main(String[] args) {
        // The 8 corners of a cube centered at the origin.
        Vector3[] vertices = {
                new Vector3(-1, -1, -1), // 0
                new Vector3( 1, -1, -1), // 1
                new Vector3( 1,  1, -1), // 2
                new Vector3(-1,  1, -1), // 3
                new Vector3(-1, -1,  1), // 4
                new Vector3( 1, -1,  1), // 5
                new Vector3( 1,  1,  1), // 6
                new Vector3(-1,  1,  1)  // 7
        };

        // The 12 edges, each one a pair of corner indices from the list above.
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0}, // back face
                {4, 5}, {5, 6}, {6, 7}, {7, 4}, // front face
                {0, 4}, {1, 5}, {2, 6}, {3, 7}  // connectors between faces
        };

        JFrame frame = new JFrame("Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Renderer(vertices, edges));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
