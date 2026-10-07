package src;
import javax.swing.*;
import java.util.Scanner;

/**
 * The main class for the 3D renderer.
 * Asks the user which shape to draw, shows it, and keeps asking until they quit.
 * @author Ethan Kazenske
 * @version 1.4
 * @since 2026-10-05
 */
class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // One window that gets reused. Each new shape replaces the old one.
        JFrame frame = new JFrame("Renderer");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        boolean running = true;
        while (running) {
            String choice;
            Triangle[] shape;

            // Keep asking until the user types a shape we know, or "quit".
            do {
                System.out.println();
                System.out.println("Pick a shape: cube, pyramid, prism, octahedron, sphere");
                System.out.print("Or type quit to exit: ");
                choice = input.nextLine().trim().toLowerCase();

                shape = switch (choice) {
                    case "cube"       -> Shapes.cube();
                    case "pyramid"    -> Shapes.pyramid();
                    case "prism"      -> Shapes.prism();
                    case "octahedron" -> Shapes.octahedron();
                    case "sphere"     -> Shapes.sphere(12, 24);
                    default           -> null;
                };

                if (shape == null && !choice.equals("quit")) {
                    System.out.println("\"" + choice + "\" isn't a shape. Try again.");
                }
            } while (shape == null && !choice.equals("quit"));

            if (choice.equals("quit")) {
                running = false;
            } else {
                // Swap the new shape into the window and show it.
                frame.getContentPane().removeAll();
                frame.add(new Renderer(shape));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                System.out.println("Showing a " + choice + ".");
            }
        }

        System.out.println("Goodbye <3");
        input.close();
        frame.dispose();
        System.exit(0);
    }
}
