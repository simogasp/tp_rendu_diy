package renderer.core.shader.vertexshaders;

import renderer.core.light.Lighting;
import renderer.core.mesh.Scene;

public class GouraudShader implements VertexShader {

    /** The Scene object containing the mesh and light(s). */
    private final Scene scene;

    /** The Lighting object containing the applyLights() method. */
    private final Lighting lighting;

    /**
     * Creates a SimpleVertexShader.
     * 
     * @param xform the Transformation object to use
     */
    public GouraudShader(Scene scene, Lighting lighting) {
        this.scene = scene;
        this.lighting = lighting;
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
        double[] material = scene.getMaterial();
        
        double[] lightColor = this.lighting.applyLights(in.worldPosition, in.normal,
                                                        in.color, scene.getCameraPosition(),
                                                        material[0], material[1], material[2], material[3]);

        in.color = lightColor;
        in.alpha = 1.0;
    }

}
