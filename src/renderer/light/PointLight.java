package renderer.light;

import renderer.algebra.Vector3;

public class PointLight extends Light {

    /**
     * The first coordinate.
     */
    private double x;

    /**
     * The second coordinate.
     */
    private double y;

    /**
     * The third coordinate.
     */
    private double z;

    /**
     * Adds a new point light source of intensity @id at position (x, y, z)
     * to the environment.
     * 
     * @param x  the x coordinate of the light source
     * @param y  the y coordinate of the light source
     * @param z  the z coordinate of the light source
     * @param id the intensity of the light source
     */
    public PointLight(double x, double y, double z, double id) {
        super(id);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getContribution(Vector3 position, Vector3 normal, double[] color, Vector3 cameraPosition, double ka,
            double kd, double ks, double s) {
        double I = 0;

        // vector from point to camera center
        Vector3 e = new Vector3(cameraPosition);
        e.subtract(position);
        e.normalize();

        // vector from point to light
        Vector3 l = getPositionAsVector3();
        l.subtract(position);
        l.normalize();

        // half-vector between e and l
        Vector3 h = new Vector3(e);
        h.add(l);
        h.normalize();

        // diffuse contribution
        double I_diffuse = kd * intensity * normal.dot(l); //++ // TODO
        //++ // double I_diffuse = ...;

        // specular contribution
        double I_specular = ks * intensity * Math.pow(normal.dot(h), s); //++ // TODO
        //++ // double I_specular = ...;
        I += I_diffuse + I_specular; //++ // I += I_diffuse + I_specular;

        return I;
    }

    private Vector3 getPositionAsVector3() {
        return new Vector3(x, y, z);
    }

}
