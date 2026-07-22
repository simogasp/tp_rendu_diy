package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;

public class Vertex {

    /** The 2D (screen-space) x coordinate of the (transformed) vertex. */
    private int x;
    /** The 2D (screen-space) y coordinate of the (transformed) vertex. */
    private int y;
    /** The depth of the (transformed) vertex. */
    private double depth;

    /** The normal of the (transformed) vertex. */
    private Vector normal;
    /** The world position of the (transformed) vertex. */
    private Vector worldPosition;

    /** The color (albedo) of the (transformed) vertex. */
    private double[] color = new double[3];
    /** The alpha value of the (transformed) vertex. */
    private double alpha;

    /** The UV 'u' coordinate of the (transformed) vertex. */
    private double u;
    /** The UV 'v' coordinate of the (transformed) vertex. */
    private double v;

    /**
     * Creates a Vertex.
     */
    public Vertex() {}

    /**
     * Creates a Vertex.
     *
     * @param x the x coordinate of the vertex (in screen-space)
     * @param y the y coordinate of the vertex (in screen-space)
     */
    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a Vertex.
     *
     * @param x the x coordinate of the vertex (in screen-space)
     * @param y the y coordinate of the vertex (in screen-space)
     * @param depth the depth of the vertex
     */
    public Vertex(int x, int y, double depth) {
        this(x, y);
        this.depth = depth;
    }

    /**
     * Creates a Vertex.
     *
     * @param x the x coordinate of the vertex (in screen-space)
     * @param y the y coordinate of the vertex (in screen-space)
     * @param depth the depth of the vertex
     * @param normal the normal of the vertex
     * @param worldPos the world position vector of the vertex
     */
    public Vertex(int x, int y, double depth, Vector normal, Vector worldPos) {
        this(x, y, depth);
        this.normal = new Vector(normal);
        this.worldPosition = new Vector(worldPos);
    }

    /**
     * Creates a Vertex.
     *
     * @param x the x coordinate of the vertex (in screen-space)
     * @param y the y coordinate of the vertex (in screen-space)
     * @param depth the depth of the vertex
     * @param normal the normal of the vertex
     * @param worldPos the world position vector of the vertex
     * @param color the color (albedo) of the vertex
     * @param alpha the alpha value (transparency value) of the vertex
     */
    public Vertex(int x, int y, double depth,
                        Vector normal, Vector worldPos,
                        double[] color, double alpha) {
        this(x, y, depth, normal, worldPos);
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
        this.alpha = alpha;
    }

    /**
     * Creates a Vertex.
     *
     * @param x the x coordinate of the vertex (in screen-space)
     * @param y the y coordinate of the vertex (in screen-space)
     * @param depth the depth of the vertex
     * @param normal the normal of the vertex
     * @param worldPos the world position vector of the vertex
     * @param color the color (albedo) of the vertex
     * @param alpha the alpha value (transparency value) of the vertex
     * @param u the U coordinate (for texture mapping) of the vertex
     * @param v the V coordinate (for texture mapping) of the vertex
     */
    public Vertex(int x, int y, double depth,
                        Vector normal, Vector worldPos,
                        double[] color, double alpha,
                        double u, double v) {
        this(x, y, depth, normal, worldPos, color, alpha);
        this.u = u;
        this.v = v;
    }

    /**
     * Return the x-coordinate of the vertex (on the screen).
     * @return the x screen-space coordinate of the vertex.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set the x-coordinate of the vertex (on the screen).
     * @param x the new x value of the vertex
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Return the x-coordinate of the vertex (on the screen).
     * @return the x screen-space coordinate of the vertex.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the y-coordinate of the vertex (on the screen).
     * @param y the new y value of the vertex
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return the depth of the vertex in the scene.
     * @return the depth of the vertex in 3D-space
     */
    public double getDepth() {
        return this.depth;
    }

    /**
     * Set the depth of the vertex in the scene.
     * @param depth the new depth value of the vertex
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * Return the normal of the vertex.
     * @return the normal of the vertex
     */
    public Vector getNormal() {
        return this.normal;
    }

    /**
     * Set the normal of the vertex.
     * @param normal the new normal of the vertex
     */
    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    /**
     * Return the world position vector of the vertex.
     * @return the worldPos vector of the vertex
     */
    public Vector getWorldPosition() {
        return this.worldPosition;
    }

    /**
     * Set the world position vector of the vertex.
     * @param worldPosition the new world position vector of the vertex
     */
    public void setWorldPosition(Vector worldPosition) {
        this.worldPosition = worldPosition;
    }

    /**
     * Return the color (albedo) of the vertex.
     * @return the color array of the vertex
     */
    public double[] getColor() {
        return this.color;
    }

    /**
     * Set the color (albedo) of the vertex.
     * @param color the new color array of the vertex
     */
    public void setColor(double[] color) {
        this.color = color;
    }

    /**
     * Return the alpha value of the vertex's albedo.
     * @return the alpha value of the vertex
     */
    public double getAlpha() {
        return this.alpha;
    }

    /**
     * Set the alpha value of the vertex's albedo.
     * @param alpha the new alpha value of the vertex
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Return the 'U' coordinate of the vertex.
     * @return the 'u' coordinate of the vertex
     */
    public double getU() {
        return this.u;
    }

    /**
     * Set the 'U' coordinate of the vertex.
     * @param u the new 'U' coordinate of the vertex
     */
    public void setU(double u) {
        this.u = u;
    }

    /**
     * Return the 'V' coordinate of the vertex.
     * @return the 'v' coordinate of the vertex
     */
    public double getV() {
        return this.v;
    }

    /**
     * Set the 'V' coordinate of the vertex.
     * @param v the new 'V' coordinate of the vertex
     */
    public void setV(double v) {
        this.v = v;
    }

    /**
     * Creates a clone of the vertex.
     *
     * @return a clone of the vertex
     */
    public Vertex clone() {
        Vertex clone = new Vertex(x, y, depth, normal, worldPosition,
                                  color, alpha, u, v);
        return clone;
    }
}
