package renderer.core.shader;

import java.awt.Color;

import renderer.algebra.Vector;
import renderer.core.light.Lighting;
import renderer.core.mesh.Scene;

public class PhongShader implements FragmentShader {
    
    private Scene scene;
    private Lighting lighting;

    private boolean activeLighting = false;

    public PhongShader(Scene scene, Lighting lighting) {
        this.scene = scene;
        this.lighting = lighting;
    }

    public PhongShader() {}

    public void setScene(Scene scene) { this.scene = scene; }
    public void setLighting(Lighting lighting) { this.lighting = lighting; }

    public boolean init(Scene scene, Lighting lighting) {
        if(scene == null || lighting == null) {
            return false;
        }
        this.scene = scene;
        this.lighting = lighting;
        return true;
    }

    public void enableLighting() { this.activeLighting = true; }
    public void disableLighting() { this.activeLighting = false; }
    public void toggleLighting() { this.activeLighting = !(activeLighting); }

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
