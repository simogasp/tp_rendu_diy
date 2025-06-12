package renderer;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.algebra.Vector3;

/**
 * The PerspectiveCorrectRasterizer class extends Rasterizer to perform
 * Persepctive Correct interpolation
 * of attributes.
 *
 * @author cdehais
 */
public class PerspectiveCorrectRasterizer extends Rasterizer {

    /**
     * Creates a PerspectiveCorrectRasterizer with the given shader.
     * @param shader the shader to use
     */
    public PerspectiveCorrectRasterizer(Shader shader) {
        super(shader);
    }

    /**
     * Rasterizes the triangular face made of the Fragment v1, v2 and v3.
     * @param v1 the first vertex of the face
     * @param v2 the second vertex of the face
     * @param v3 the third vertex of the face
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    @Override
    public void rasterizeFace(Fragment v1, Fragment v2, Fragment v3) {

        final Matrix cMat = makeBarycentricCoordsMatrix(v1, v2, v3);

        // iterate over the triangle's bounding box
        final int xmin = Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()));
        final int ymin = Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()));
        final int xmax = Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()));
        final int ymax = Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()));

        Fragment fragment = new Fragment(0, 0);
        final int numAttributes = fragment.getNumAttributes();

        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {

                // setup position now to allow early clipping
                fragment.setPosition(x, y);
                if (shader.isClipped(fragment)) {
                    continue;
                }

                final Vector3 v = new Vector3(1.0, (double) x, (double) y);
                final Vector bar = cMat.multiply(v);
                if ((bar.get(0) >= 0.0)
                        && (bar.get(1) >= 0.0)
                        && (bar.get(2) >= 0.0)) {
                    final double oneOverZ = bar.get(0) / v1.getDepth()
                            + bar.get(1) / v2.getDepth()
                            + bar.get(2) / v3.getDepth();
                    for (int i = 0; i < numAttributes; i++) {
                        final double aOverZ = bar.get(0) * v1.getAttribute(i) / v1.getDepth()
                                + bar.get(1) * v2.getAttribute(i) / v2.getDepth()
                                + bar.get(2) * v3.getAttribute(i) / v3.getDepth();

                        fragment.setAttribute(i, aOverZ / oneOverZ);
                    }
                    shader.shade(fragment);
                }
            }
        }
    }
}
