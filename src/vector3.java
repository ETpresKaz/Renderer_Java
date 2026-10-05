package src;
/**
 * A 3D vector with x, y, and z components.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-5-10
 * vector3
 */
public class vector3 {
    private double x;
    private double y;
    private double z;

    /**
     * Constructs a new vector3 with the specified x, y, and z components.
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     * @param z The z component of the vector.
     */
    public vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a new vector3 with all components set to zero.
     */
    public vector3() {
        this(0, 0, 0);
    }

    /**
     * Returns the x component of the vector.
     * @return The x component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y component of the vector.
     * @return The y component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the z component of the vector.
     * @return The z component of the vector.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the x component of the vector.
     * @param x The new x component of the vector.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y component of the vector.
     * @param y The new y component of the vector.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the z component of the vector.
     * @param z The new z component of the vector.
     */
    public void setZ(double z) {
        this.z = z;
    }
    
    /**
     * Returns a string representation of the vector.
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
    /**
     * Checks if this vector is equal to another object.
     * @param o The object to compare with.
     * @return true if the other object is a vector3 with the same components, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof vector3) {
            vector3 vector3 = (vector3) o;
            return x == vector3.x && y == vector3.y && z == vector3.z;
        }
        return false;
    }

    /**
     * Adds this vector to another vector.
     * @param other The other vector to add.
     * @return A new vector3 that is the sum of this vector and the other vector.
     */
    public vector3 add(vector3 other) {
        return new vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts another vector from this vector.
     * @param other The other vector to subtract.
     * @return A new vector3 that is the difference of this vector and the other vector.
     */
    public vector3 subtract(vector3 other) {
        return new vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Multiplies this vector by a scalar.
     * @param scalar The scalar to multiply by.
     * @return A new vector3 that is the product of this vector and the scalar.
     */
    public vector3 multiply(double scalar) {
        return new vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Divides this vector by a scalar.
     * @param scalar The scalar to divide by.
     * @return A new vector3 that is the quotient of this vector and the scalar.
     * @throws IllegalArgumentException if the scalar is zero.
     */
    public vector3 divide(double scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new vector3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    /**
     * Calculates the dot product of this vector and another vector.
     * @param other The other vector to calculate the dot product with.
     * @return The dot product of this vector and the other vector.
     */
    public double dot(vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Calculates the cross product of this vector and another vector.
     * @param other The other vector to calculate the cross product with.
     * @return A new vector3 that is the cross product of this vector and the other vector.
     */
    public vector3 cross(vector3 other) {
        return new vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    /**
     * Calculates the magnitude (length) of this vector.
     * @return The magnitude of this vector.
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this vector (scales it to have a magnitude of 1).
     * @return A new vector3 that is the normalized version of this vector.
     * @throws IllegalStateException if the vector is a zero vector (magnitude is 0).
     */
    public vector3 normalize() {
        double mag = magnitude();
        if (mag == 0) {
            throw new IllegalStateException("Cannot normalize a zero vector");
        }
        return divide(mag);
    }
}
