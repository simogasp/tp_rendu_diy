package renderer.utils;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;
import renderer.core.shader.vertexshaders.Vertex;
import renderer.core.shader.fragmentshaders.Fragment;

/**
 * Provides utility methods for interpolating attributes of fragments between vertices.
 */
public final class Interpolation {

    private Interpolation() {
        // Prevent instantiation
    }

    /**
     * Linear interpolation of a Fragment f on the edge defined by Fragment's v1 and
     * v2.
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @param f  the fragment to interpolate
     * @param middleDoubleValue the value to use if the two vertices (v1 and v2) are
     *                          in the same position (to avoid division by 0)
     */
    public static void interpolate2(Vertex v1, Vertex v2,
                                    Fragment f, double middleDoubleValue) {
        final int x1 = v1.getX();
        final int y1 = v1.getY();
        final int x2 = v2.getX();
        final int y2 = v2.getY();
        final int x = f.getX();
        final int y = f.getY(); // was an x ?? maybe it was on purpose ??

        // corner case in which the two vertices are the same
        double alpha = middleDoubleValue;
        // if we have more pixel on the horizontal axis
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            if (x2 != x1) {
                // compute alpha only if there are pixels to interpolate
                alpha = (double) (x - x1) / (double) (x2 - x1);
            }
        } else {
            if (y2 != y1) {
                alpha = (double) (y - y1) / (double) (y2 - y1);
            }
        }

        // Interpolate the depth
        final double depthInterpolated =
                                (1.0 - alpha) * v1.getDepth() + alpha * v2.getDepth();
        f.setDepth(depthInterpolated);

        // Interpolate the normal
        final Vector n1 = v1.getNormal();
        final Vector n2 = v2.getNormal();
        final Vector nv = new Vector(n1.size());

        for (int i = 0; i < n1.size(); ++i) {
            final double interpolated = (1.0 - alpha) * n1.get(i) + alpha * n2.get(i);
            nv.set(i, interpolated);
        }
        f.setNormal(nv);

        // Interpolate the world position
        final Vector wp1 = v1.getWorldPosition();
        final Vector wp2 = v2.getWorldPosition();
        final Vector wpv = new Vector(wp1.size());

        for (int i = 0; i < wp1.size(); i++) {
            final double interpolated = (1.0 - alpha) * wp1.get(i) + alpha * wp2.get(i);
            wpv.set(i, interpolated);
        }
        f.setWorldPosition(wpv);

        // Interpolate the color
        final double[] c1 = v1.getColor();
        final double[] c2 = v2.getColor();
        final double[] cv = new double[c1.length];

        for (int i = 0; i < c1.length; i++) {
            final double interpolated = MathUtils.clamp(
                (1.0 - alpha) * c1[i] + alpha * c2[i], 0, 1);
            cv[i] = interpolated;
        }
        f.setColor(cv);

        // Interpolate the alpha value
        final double a1 = v1.getAlpha();
        final double a2 = v2.getAlpha();
        final double alphaInterpolated = MathUtils.clamp(
            (1.0 - alpha) * a1 + alpha * a2, 0, 1);
        f.setAlpha(alphaInterpolated);

        // Interpolate the UV coordinates
        final double uInterpolated = (1.0 - alpha) * v1.getU() + alpha * v2.getU();
        final double vInterpolated = (1.0 - alpha) * v1.getV() + alpha * v2.getV();
        f.setTextureCoordinates(uInterpolated, vInterpolated);
    }


    /**
     * Linear interpolation of a Fragment f on the triangle defined by
     * Vertices v1, v2 and v3.
     *
     * This method uses an interpolator function (as AttributeInterpolator interp,
     * use interp.interpolate(...)) to calculate the interpolation of the attributes.
     * This allows different rasterizers to use different interpolation methods
     *
     * @param v1 the first vertex of the triangle
     * @param v2 the second vertex of the triangle
     * @param v3 the third vertex of the triangle
     * @param f  the fragment to interpolate
     * @param w1 the barycentric coordinate of the first vertex
     * @param w2 the barycentric coordinate of the second vertex
     * @param w3 the barycentric coordinate of the third vertex
     * @param interp the interpolation function
     */
    public static void interpolate3(Vertex v1, Vertex v2, Vertex v3, Fragment f,
                                    double w1, double w2, double w3,
                                    AttributeInterpolator interp) {


        //<++
        //++  // TODO : calculate the fragment's (f) attributes' values by
        //++  // interpolating the values of the attributes of the 3 vertices
        //++  // composing the triangle it's in.
        //++
        //++  // You may need to consult the methods of these classes in particular :
        //++  //     - Fragment
        //++  //     - VertexOutput
        //++
        //++  // You also need to implement the LinearInterpolator class
        //++  // for the rasterization to work !
        //>++

        //<!!
        // Interpolate the depth of the fragment
        f.setDepth(interp.interpolate(
            v1.getDepth(), v2.getDepth(), v3.getDepth(),
            w1, w2, w3));

        // Interpolate the normals of the fragment
        Vector n1 = v1.getNormal();
        Vector n2 = v2.getNormal();
        Vector n3 = v3.getNormal();
        Vector nInterpolated = new Vector(n1.size());
        for (int i = 0; i < n1.size(); i++) {
            double value = interp.interpolate(
                n1.get(i),
                n2.get(i),
                n3.get(i),
                w1, w2, w3);
            nInterpolated.set(i, value);
        }
        f.setNormal(nInterpolated);

        // Interpolate the color of the fragment
        double[] c1 = v1.getColor();
        double[] c2 = v2.getColor();
        double[] c3 = v3.getColor();
        double[] cInterpolated = new double[c1.length];
        for (int i = 0; i < c1.length; i++) {
            final double value = interp.interpolate(
                c1[i],
                c2[i],
                c3[i],
                w1, w2, w3);
            cInterpolated[i] = MathUtils.clamp(value, 0, 1);
        }
        f.setColor(cInterpolated);

        // Interpolate the alpha value of the fragment
        double value = interp.interpolate(
            v1.getAlpha(),
            v2.getAlpha(),
            v3.getAlpha(),
            w1, w2, w3);
        f.setAlpha(MathUtils.clamp(value, 0, 1));

        // Interpolate the world position of the fragment
        Vector wp1 = v1.getWorldPosition();
        Vector wp2 = v2.getWorldPosition();
        Vector wp3 = v3.getWorldPosition();
        Vector wpInterpolated = new Vector(wp1.size());
        for (int i = 0; i < wp1.size(); i++) {
            value = interp.interpolate(
                wp1.get(i),
                wp2.get(i),
                wp3.get(i),
                w1, w2, w3);
            wpInterpolated.set(i, value);
        }
        f.setWorldPosition(wpInterpolated);

        // Interpolate the UV coordinates of the fragment
        double uInterpolated = interp.interpolate(v1.getU(), v2.getU(), v3.getU(),
                                                  w1, w2, w3);
        double vInterpolated = interp.interpolate(v1.getV(), v2.getV(), v3.getV(),
                                                  w1, w2, w3);
        f.setTextureCoordinates(uInterpolated, vInterpolated);
        //>!!
    }
}
