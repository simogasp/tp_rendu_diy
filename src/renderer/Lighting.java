package renderer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector3;

/**
 * The Lighting class describes a scene lighting environment.
 * @author gmorin, smondet
 */
public class Lighting {

    // Constants for the type of light sources.

    /**
     * None.
    */
    static final int NONE = 0;
    /**
     * Ambient light.
    */
    static final int AMBIENT = 1;
    /**
     * Point light.
     */
    static final int POINT = 2;

    /** List of light sources. */
    private List<Light> lights;

    /**
     * Internal Class describing a light source.
     */
    private class Light {

        /** type of light. */
        public int type = NONE;
        /** parameters. */
        public double[] params;

        /**
         * Creates a new light source of given type and parameters.
         * @param lightType the type of the light source
         * @param parameters the parameters of the light source
         */
        Light(int lightType, double[] parameters) {
            this.type = lightType;
            this.params = parameters;
        }
    }

    /**
     * Creates a new Lighting environment.
     */
    public Lighting() {
        lights = new LinkedList<>();
    }

    /**
     * Adds a new ambient light source of intensity @ia to the environment.
     * @param ia the intensity of the ambient light
     */
    public void addAmbientLight(double ia) {
        double[] v = new double[1];
        v[0] = ia;
        lights.add(new Light(AMBIENT, v));
    }

    /**
     * Adds a new point light source of intensity @id at position (x, y, z)
     * to the environment.
     * @param x the x coordinate of the light source
     * @param y the y coordinate of the light source
     * @param z the z coordinate of the light source
     * @param id the intensity of the light source
     */
    public void addPointLight(double x, double y, double z, double id) {
        double[] v = new double[5];
        v[0] = x;
        v[1] = y;
        v[2] = z;
        v[3] = id;
        lights.add(new Light(POINT, v));
    }

    /**
     * Computes the illuminated color of a 3D points of given position, normal and
     * color, and given the camera position and material parameters.
     * @param position the position of the point
     * @param normal the normal at the point
     * @param color the color of the point
     * @param cameraPosition the position of the camera
     * @param ka the ambient reflection coefficient
     * @param kd the diffuse reflection coefficient
     * @param ks the specular reflection coefficient
     * @param s the shininess coefficient
     * @return the illuminated color of the point
     */
    public double[] applyLights(Vector3 position, Vector3 normal, double[] color,
            Vector3 cameraPosition,
            double ka, double kd, double ks, double s) {
        double[] litColor = new double[3];

        // total light intensity
        double I = 0.0;

        Iterator<Light> it = lights.iterator();
        while (it.hasNext()) {
            Light light = it.next();
            switch (light.type) {
                case AMBIENT:
                    // ambient light contribution
                    I += ka * light.params[0]; //++ // TODO
                    break;

                case POINT:
                    try {
                        // vector from point to camera center
                        Vector3 e = new Vector3(cameraPosition);
                        e.subtract(position);
                        e.normalize();

                        // vector from point to light
                        Vector3 l = new Vector3(light.params[0],
                                                light.params[1],
                                                light.params[2]);
                        l.subtract(position);
                        l.normalize();

                        // half-vector between e and l
                        Vector3 h = new Vector3(e);
                        h.add(l);
                        h.normalize();

                        // diffuse contribution
                        double I_diffuse = kd * light.params[3] * normal.dot(l); //++ // TODO
                        //++ // double I_diffuse = ...;

                        // specular contribution
                        double I_specular = ks * light.params[3] * Math.pow(normal.dot(h), s); //++ // TODO
                        //++ // double I_specular = ...;
                        I += I_diffuse + I_specular; //++ // I += I_diffuse + I_specular;

                    } catch (SizeMismatchException ex) {
                        /* should not reach */
                    }

                    break;
                default:
                    // ignore unknown lights
                    break;
            }
        }

        litColor[0] = I * color[0];
        litColor[1] = I * color[1];
        litColor[2] = I * color[2];

        return litColor;
    }
}
