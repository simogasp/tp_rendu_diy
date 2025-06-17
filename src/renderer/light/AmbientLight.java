package renderer.light;

import renderer.algebra.Vector;

/**
 * This Class represents a ambiant light.
 */
public class AmbientLight extends Light {


    
    public AmbientLight(double intensity) {
        super(intensity);
    }

    public double getContribution(Vector position, Vector normal, double[] color, Vector cameraPosition, double ka,
            double kd, double ks, double s) {
        return intensity;
    }
}
