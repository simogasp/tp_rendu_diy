package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;
import renderer.core.camera.Transformation;

public class SimpleVertexShader implements VertexShader {

    /** The Transformation object containg all the 3D transformation methods. */
    private final Transformation xform;

    /**
     * Creates a SimpleVertexShader.
     * 
     * @param xform the Transformation object to use
     */
    public SimpleVertexShader(Transformation xform) {
        this.xform = xform;
    }

    /**
     * The simplest form of Vertex Shader : only project the
     * vertex from 3D space (scene/mesh) to 2D space (the screen).
     * All the other data (color/albedo, normal, world position, depth, 
     * alpha value, UV coordinates...) is left unchanged.
     * 
     * @param in the vertex in 3D space
     * @return a VertexOutput containing the transformed vertex's data 
     *         (in 2D space)
     */
    @Override
    public VertexOutput shade(VertexInput in) {

        VertexOutput out = new VertexOutput();

        Vector pVertex = xform.projectPoint(in.position);

        out.x = (int) Math.round(pVertex.get(0));
        out.y = (int) Math.round(pVertex.get(1));
        out.depth = pVertex.get(2);

        out.worldPosition = in.position;
        out.normal = in.normal;

        out.color = in.color;
        out.alpha = 1.0;

        out.u = in.u;
        out.v = in.v;

        return out;
    }
}
