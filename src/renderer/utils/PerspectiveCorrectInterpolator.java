package renderer.utils;

/**
 * Provides a perspective-correct interpolation for attributes based on barycentric
 * coordinates.
 * This class implements the AttributeInterpolator interface and corrects for
 * perspective distortion when interpolating vertex attributes.
 */
public class PerspectiveCorrectInterpolator implements AttributeInterpolator {
    @Override
    public double interpolate(double a1, double a2, double a3,
                              double w1, double w2, double w3) {

        //<++
        //++ // TODO : interpolate the value of the attributes of the 3 vertices,
        //++ // but correct the error made by not correctly accounting for the
        //++ // depth.
        //>++
        //++ return w1 * a1 + w2 * a2 + w3 * a3;
        //<!!
        double aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
        double oneOverZ = w1 + w2 + w3;

        return aOverZ / oneOverZ;
        //>!!
    }
}
