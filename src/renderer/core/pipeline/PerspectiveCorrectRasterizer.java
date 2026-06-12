package renderer.core.pipeline;

import renderer.algebra.MathUtils;
import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.core.shader.Fragment;

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
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    @Override
    public void rasterizeFace(VertexOutput v1, VertexOutput v2, VertexOutput v3, boolean onlyDepth)
            throws SizeMismatchException {

        // early exit if the triangle is too small
        final double minArea = 1e-6;
        if (Math.abs(triangleArea(v1, v2, v3)) < minArea) {
            return;
        }
        final Matrix cMat = makeBarycentricCoordsMatrix(v1, v2, v3);

        // iterate over the triangle's bounding box
        final int xmin = Math.min(v1.x, Math.min(v2.x, v3.x));
        final int ymin = Math.min(v1.y, Math.min(v2.y, v3.y));
        final int xmax = Math.max(v1.x, Math.max(v2.x, v3.x));
        final int ymax = Math.max(v1.y, Math.max(v2.y, v3.y));

        final Fragment fragment = new Fragment(0, 0);
        final double eps = (new Vector(ymax - ymin, xmax - xmin)).norm() / 1e6;

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
                final double w1 = bar.get(0) / v1.depth;
                final double w2 = bar.get(1) / v2.depth;
                final double w3 = bar.get(2) / v3.depth;

                // weighting factor for perspective correct interpolation
                final double oneOverZ = w1 + w2 + w3;
                
                if(!onlyDepth) {
                    // Interpolate the depth
                    double a1 = v1.depth;
                    double a2 = v2.depth;
                    double a3 = v3.depth;

                    double aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                    double interpolated = aOverZ / oneOverZ;
                    fragment.setAttribute(Fragment.DEPTH, interpolated);

                    // Interpolate the normal
                    Vector n1 = v1.normal;
                    Vector n2 = v2.normal;
                    Vector n3 = v3.normal;
                    
                    for(int i = 0 ; i < 3 ; i++) {
                        a1 = n1.get(i);
                        a2 = n2.get(i);
                        a3 = n3.get(i);
                        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                        interpolated = aOverZ / oneOverZ;
                        fragment.setAttribute(Fragment.NORMAL_X + i, interpolated);
                    }

                    // Interpolate the world position
                    Vector wp1 = v1.normal;
                    Vector wp2 = v2.normal;
                    Vector wp3 = v3.normal;
                    
                    for(int i = 0 ; i < 3 ; i++) {
                        a1 = wp1.get(i);
                        a2 = wp2.get(i);
                        a3 = wp3.get(i);
                        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                        interpolated = aOverZ / oneOverZ;
                        fragment.setAttribute(Fragment.WORLD_X + i, interpolated);
                    }

                    // Interpolate the color
                    double[] c1 = v1.color;
                    double[] c2 = v2.color;
                    double[] c3 = v3.color;

                    for(int i = 0 ; i < 3 ; i++) {
                        a1 = c1[i];
                        a2 = c2[i];
                        a3 = c3[i];
                        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                        interpolated = MathUtils.clamp(aOverZ / oneOverZ, 0, 1);
                        fragment.setAttribute(Fragment.COLOR_R + i, interpolated);
                    }

                    // Interpolate the alpha value
                    a1 = v1.alpha;
                    a2 = v2.alpha;
                    a3 = v3.alpha;

                    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                    interpolated = MathUtils.clamp(aOverZ / oneOverZ, 0, 1);
                    fragment.setAttribute(Fragment.COLOR_ALPHA, interpolated);

                    // Interpolate the UV coordinates
                    a1 = v1.u;
                    a2 = v2.u;
                    a3 = v3.u;

                    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                    interpolated = aOverZ / oneOverZ;
                    fragment.setAttribute(Fragment.TEXTURE_U, interpolated);

                    a1 = v1.v;
                    a2 = v2.v;
                    a3 = v3.v;

                    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
                    interpolated = aOverZ / oneOverZ;
                    fragment.setAttribute(Fragment.TEXTURE_V, interpolated);
                } else {
                    final double bias = 1.01;

                    Vector vecAtt = new Vector(v1.depth, v2.depth, v3.depth);
                    double interpolated = bar.dot(vecAtt);
                    fragment.setAttribute(Fragment.DEPTH, interpolated * bias);
                }
                consumer.consume(fragment.clone());
            }
        }
    }
}
