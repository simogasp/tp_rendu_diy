package renderer.core.pipeline;

import renderer.algebra.Vector;

public class VertexOutput {
    
    public int x, y;
    public double depth;

    public Vector normal;
    public Vector worldPosition;

    public double[] color = new double[4];
    public double u, v;
}
