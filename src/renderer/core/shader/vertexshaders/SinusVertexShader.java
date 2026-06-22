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
        double x_amplitude = 0.02;
        double y_amplitude = 0.03;
        double z_amplitude = 0.07;

        // The frequency of distortion along each axis
        double x_frequency = 15.0;
        double y_frequency = 20.0;
        double z_frequency = 5.00;

        Vector sinPos = new Vector(
            x + x_amplitude * Math.sin((y + z) * x_frequency), 
            y + y_amplitude * Math.sin((x + z) * y_frequency), 
            z + z_amplitude * Math.sin((x + y) * z_frequency));
        Vector pVertex = xform.projectPoint(sinPos);

        in.x = (int) Math.round(pVertex.get(0));
        in.y = (int) Math.round(pVertex.get(1));
        in.depth = pVertex.get(2);

        in.alpha = 1.0;
    }
}
