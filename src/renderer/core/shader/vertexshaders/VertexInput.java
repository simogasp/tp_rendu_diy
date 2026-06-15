package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;

public class VertexInput {
    
    /** The position of the vertex (in 3D space) */
    public Vector position;

    /** The normal of the vertex */
    public Vector normal;

    /** The color (albedo) of teh vertex */
    public double[] color;

    /** The UV coordinates of the vertex */
    public double u, v;
}
