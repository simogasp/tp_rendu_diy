package renderer.light;

import renderer.algebra.Vector;
import renderer.algebra.Vector3;

/**
 * This Class represents a ambiant light.
 */
public class AmbientLight extends Light {


    
    public AmbientLight(double intensity) {
        super(intensity);
    }

    public double getContribution(Vector3 position, Vector normal, double[] color, Vector3 cameraPosition, double ka,
            double kd, double ks, double s) {
        return intensity;
    }
}
