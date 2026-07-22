package renderer.core.pipeline;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.core.shader.fragmentshaders.Fragment;
import renderer.core.shader.vertexshaders.Vertex;
import renderer.utils.AttributeInterpolator;
import renderer.utils.Interpolation;
import renderer.utils.PerspectiveCorrectInterpolator;

/**
 * The PerspectiveCorrectRasterizer class extends Rasterizer to perform
 * Perspective Correct interpolation
 * of attributes.
 *
 * @author cdehais
 */
public class PerspectiveCorrectRasterizer extends Rasterizer {

    /**
     * Creates a PerspectiveCorrectRasterizer with the given fragment consumer.
     *
     * @param consumer the fragment consumer to use
     */
    public PerspectiveCorrectRasterizer(FragmentConsumer consumer) {
        super(consumer);
    }

    /**
     * Rasterizes the triangular face made of the Fragment v1, v2 and v3.
     *
     * @param v1 the first vertex of the face
     * @param v2 the second vertex of the face
     * @param v3 the third vertex of the face
     * @param onlyDepth a boolean to indicate if the rasterizer should calculate
     *                  all the fragment's attributes or only its depth
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    @Override
    public void rasterizeFace(Vertex v1, Vertex v2, Vertex v3, boolean onlyDepth)
            throws SizeMismatchException {

        // early exit if the triangle is too small
        final double minArea = 1e-6;
        if (Math.abs(triangleArea(v1, v2, v3)) < minArea) {
            return;
        }
        final Matrix cMat = makeBarycentricCoordsMatrix(v1, v2, v3);

        // iterate over the triangle's bounding box
        final int xmin = Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()));
        final int ymin = Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()));
        final int xmax = Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()));
        final int ymax = Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()));

        final Fragment fragment = new Fragment(0, 0);
        final double eps = (new Vector(ymax - ymin, xmax - xmin)).norm() / 1e6;
        AttributeInterpolator interp = new PerspectiveCorrectInterpolator();

        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {

                fragment.setPosition(x, y);

                final Vector v = new Vector(1.0, (double) x, (double) y);
                final Vector bar = cMat.multiply(v);
                // skip the fragment if outside the triangle
                if (bar.get(0) < -eps || bar.get(1) < -eps || bar.get(2) < -eps) {
                    continue;
                }

                // perspective correction factor
                final double w1 = bar.get(0) / v1.getDepth();
                final double w2 = bar.get(1) / v2.getDepth();
                final double w3 = bar.get(2) / v3.getDepth();

                //!! // This would be where the oneOverZ would go

                if (!onlyDepth) {
                    Interpolation.interpolate3(v1, v2, v3, fragment, w1, w2, w3, interp);
                    //!! // The backup code would be here
                } else {
                    final double bias = 1.01;

                    Vector vecAtt = new Vector(v1.getDepth(), v2.getDepth(), v3.getDepth());
                    double interpolated = bar.dot(vecAtt);
                    fragment.setAttribute(Fragment.DEPTH, interpolated * bias);
                }
                consumer.consume(fragment.clone());
            }
        }
    }
}
