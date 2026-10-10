# Renderer_Java

A small 3D renderer written in plain Java (Swing), built from scratch to learn how 3D graphics work. It draws a spinning, lit shape made of filled triangles. No libraries, no GPU, just math.

## Running it

Requires Java 16 or newer (tested on Java 21).

```bash
javac src/*.java
java src.Main
```

Run both commands from the project root (the folder that contains `src`).

The program asks which shape to draw in the terminal:

```
Pick a shape: cube, pyramid, prism, octahedron, sphere
Or type quit to exit:
```

Type a shape name and it appears spinning in a window. You can keep typing new shape names to swap the shape in the same window. Type `quit` to exit.

## Project layout

| File | What it does |
|------|--------------|
| `src/Main.java` | Asks which shape to draw, then shows it in a window. Keeps asking until you type `quit`. |
| `src/Shapes.java` | Builds the shapes out of triangles: `cube`, `pyramid`, `prism`, `octahedron`, and `sphere` (generated from rings and segments). |
| `src/Renderer.java` | The renderer. Rotates, culls, lights, projects and draws the triangles every frame. |
| `src/Triangle.java` | A triangle: three `Vector3` corners. Every shape is built from these. |
| `src/Vector3.java` | A 3D vector with math helpers (`add`, `subtract`, `dot`, `cross`, `normalize`, ...). |

## How it works

Every shape is a list of triangles. About 60 times per second, the renderer does this for each triangle:

1. **Rotate**: spin each corner around the Y and X axes using `sin` and `cos`.
2. **Cull**: compute the triangle's *normal* (an arrow pointing out of its front side) with the cross product of two edges. If the normal points away from the camera, the triangle is on the back of the shape, so it's skipped.
3. **Light**: the dot product of the normal and the light direction says how directly the triangle faces the light (1 = straight at it, 0 = sideways). That number sets the brightness. A little ambient light keeps shadowed faces from turning pure black.
4. **Project**: turn each 3D corner into a 2D screen pixel. The key line is
   ```java
   scale = FOV / (z + CAMERA_DISTANCE);
   ```
   Farther points are divided by a bigger number, so they shrink toward the center. That's perspective.
5. **Draw**: sort the triangles from farthest to nearest and fill them in that order (the *painter's algorithm*), so near triangles paint over far ones.

## Making your own shapes

1. Add a method to `Shapes` that returns a `Triangle[]`, like `Shapes.cube()` does. Keep the shape centered at the origin and roughly between -1 and 1 so it fits the camera.
2. Add a `case` for it to the `switch` in `Main`, and add its name to the "Pick a shape" prompt.

One rule matters: **list each triangle's corners counter-clockwise as seen from outside the shape.** The renderer uses that order to figure out which side is the front. If a face goes missing, its corners are probably in the wrong order. Swap two of them.

For round or repetitive shapes, calculate the corners in a loop instead of typing them out. `Shapes.sphere()` is an example: it builds a grid of points like latitude and longitude lines, then fills each grid square with two triangles.

## Things to tweak

- `CAMERA_DISTANCE` / `FOV` in `Renderer.java`: zoom in and out.
- `LIGHT`: move the light around.
- `BASE_COLOR`: change the shape's color.
- `angle += 0.02`: change the spin speed.
- `Shapes.sphere(12, 24)` in `Main.java`: more rings and segments make a rounder sphere, but it's slower to draw.

## Ideas for what's next

- Load shapes from `.obj` model files.
- Give each triangle its own color.
- Replace the painter's algorithm with a depth buffer (z-buffer) so overlapping shapes draw correctly.
- Control the rotation with the mouse or keyboard.

## License

See [LICENSE](LICENSE).
