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
     */
    @Override
    public void shade(Vertex in) {

        Vector pVertex = xform.projectPoint(in.worldPosition);

        in.x = (int) Math.round(pVertex.get(0));
        in.y = (int) Math.round(pVertex.get(1));
        in.depth = pVertex.get(2);

        in.alpha = 1.0;
    }
}
