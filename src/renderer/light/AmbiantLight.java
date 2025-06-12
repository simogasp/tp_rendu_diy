package renderer.light;

import renderer.algebra.Vector3;

/**
 * This Class represents a ambiant light.
 */
public class AmbiantLight extends Light {


    
    public AmbiantLight(double intensity) {
        super(intensity);
    }

    public double getContribution(Vector3 position, Vector3 normal, double[] color, Vector3 cameraPosition, double ka,
            double kd, double ks, double s) {
        return intensity;
    }
}
