package renderer.core.shader.vertexshaders;

import renderer.algebra.Vector;
import renderer.core.camera.Transformation;

public class SinusVertexShader implements VertexShader {

    /** The Transformation object containg all the 3D transformation methods. */
    private final Transformation xform;

    /**
     * Creates a SimpleVertexShader.
     *
     * @param xform the Transformation object to use
     */
    public SinusVertexShader(Transformation xform) {
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

        double x =  in.worldPosition.get(0);
        double y =  in.worldPosition.get(1);
        double z =  in.worldPosition.get(2);

        // The amplitudes of distortion along each axis
        double xAmplitude = 0.02;
        double yAmplitude = 0.03;
        double zAmplitude = 0.07;

        // The frequency of distortion along each axis
        double xFrequency = 15.0;
        double yFrequency = 20.0;
        double zFrequency = 5.00;

        Vector sinPos = new Vector(
            x + xAmplitude * Math.sin((y + z) * xFrequency),
            y + yAmplitude * Math.sin((x + z) * yFrequency),
            z + zAmplitude * Math.sin((x + y) * zFrequency));
        Vector pVertex = xform.projectPoint(sinPos);

        in.x = (int) Math.round(pVertex.get(0));
        in.y = (int) Math.round(pVertex.get(1));
        in.depth = pVertex.get(2);

        in.alpha = 1.0;
    }
}
