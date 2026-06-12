package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;

public class VertexOutput {
    
    public int x, y;
    public double depth;

    public Vector normal;
    public Vector worldPosition;

    public double[] color = new double[3];
    public double alpha;
    public double u, v;

    public VertexOutput() {}

    public VertexOutput(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public VertexOutput(int x, int y, double depth) {
        this(x, y);
        this.depth = depth;
    }

    public VertexOutput(int x, int y, double depth, Vector normal, Vector worldPos) {
        this(x, y, depth);
        this.normal = new Vector(normal);
        this.worldPosition = new Vector(worldPos);
    }

    public VertexOutput(int x, int y, double depth, 
                        Vector normal, Vector worldPos,
                        double[] color, double alpha) {
        this(x, y, depth, normal, worldPos);
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
        this.alpha = alpha;
    }

    public VertexOutput(int x, int y, double depth, 
                        Vector normal, Vector worldPos,
                        double[] color, double alpha,
                        double u, double v) {
        this(x, y, depth, normal, worldPos, color, alpha);
        this.u = u;
        this.v = v;
    }

    public VertexOutput clone() {
        VertexOutput clone = new VertexOutput(x, y, depth, normal, worldPosition, 
                                              color, alpha, u, v);
        return clone;
    }
}
