package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;

public class Vertex {
    
    /** The 2D (screen-space) coordinates of the (transformed) vertex */
    public int x, y;
    /** The depth of the (transformed) vertex */
    public double depth;

    /** The normal of the (transformed) vertex */
    public Vector normal;
    /** The world position of the (transformed) vertex */
    public Vector worldPosition;

    /** The color (albedo) of the (transformed) vertex */
    public double[] color = new double[3];
    /** The alpha value of the (transformed) vertex */
    public double alpha;
    /** The UV coordinates of the (transformed) vertex */
    public double u, v;

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
