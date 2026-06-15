package renderer.core.shader.fragmentshaders;

import java.awt.Color;

import renderer.algebra.Vector;
import renderer.core.light.Lighting;
import renderer.core.mesh.Scene;

public class PhongShader implements FragmentShader {
    
    private Scene scene;
    private Lighting lighting;

    private boolean activeLighting = false;

    /**
     * Creates a PhongShader.
     * 
     * @param scene the scene the shader is running in
     * @param lighting the Lighting of the scene
     */
    public PhongShader(Scene scene, Lighting lighting) {
        this.scene = scene;
        this.lighting = lighting;
    }

    /**
     * Creates a PhongShader.
     */
    public PhongShader() {}

    /** 
     * Sets the shader's scene.
     * 
     * @param scene the (new) scene the shader will be running in 
     */
    public void setScene(Scene scene) { this.scene = scene; }

    /** 
     * Sets the scene's lighting.
     * 
     * @param scene the (new) lighting of the scene 
     */
    public void setLighting(Lighting lighting) { this.lighting = lighting; }

    /**
     * Initializes the shader with a scene and a lighting.
     * Returns a boolean to indicate wether it is ready to function
     * or not.
     * 
     * @param scene
     * @param lighting
     * @return true if the shader is ready to function, false if not.
     */
    public boolean init(Scene scene, Lighting lighting) {
        if(scene == null || lighting == null) {
            return false;
        }
        this.scene = scene;
        this.lighting = lighting;
        return true;
    }

    /**
     * Toggles ON the lighting.
     */
    public void enableLighting() { this.activeLighting = true; }

    /**
     * Toggles OFF the lighting.
     */
    public void disableLighting() { this.activeLighting = false; }

    /**
     * Toggles ON/OFF the lighting (inverse of what it used to be).
     */
    public void toggleLighting() { this.activeLighting = !(activeLighting); }

    /**
     * Shades the fragment according to the scene's lighting.
     * 
     * @param fragment the fragment to shade
     * @return a FragmentOutput containing the color of the shaded fragment.
     */
    public FragmentOutput shade(Fragment fragment) {
        if(this.activeLighting) {
            //++ // TODO
            //++ return new FragmentOutput(fragment.getColor());
            //<!!
            Vector world_position = fragment.getWorldPosition();
            Vector normal = fragment.getNormal();
            double[] color = fragment.getAttribute(Fragment.COLOR_R, Fragment.COLOR_B);
            double[] material = scene.getMaterial();
            
            double[] lightColor = this.lighting.applyLights(world_position, normal,
                                                            color, scene.getCameraPosition(),
                                                            material[0], material[1], material[2], material[3]);

            return new FragmentOutput(new Color((float)lightColor[0],
                                                (float)lightColor[1],
                                                (float)lightColor[2]));
            //>!!
        } else {
            return new FragmentOutput(fragment.getColor());
        }
    }

}
