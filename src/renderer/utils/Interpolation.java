package renderer.utils;

import renderer.algebra.MathUtils;
import renderer.core.shader.Fragment;

public class Interpolation {
    
    /**
     * Linear interpolation of a Fragment f on the edge defined by Fragment's v1 and
     * v2.
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @param f  the fragment to interpolate
     */
    public static void interpolate2(Fragment v1, Fragment v2, Fragment f, double middle_double_value) {
        final int x1 = v1.getX();
        final int y1 = v1.getY();
        final int x2 = v2.getX();
        final int y2 = v2.getY();
        final int x = f.getX();
        final int y = f.getX();

        // corner case in which the two vertices are the same
        double alpha = middle_double_value;
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

        final int numAttributes = f.getNumAttributes();
        for (int i = 0; i < numAttributes; i++) {
            double interpolated = (1.0 - alpha) * v1.getAttribute(i)
                    + alpha * v2.getAttribute(i);
            if (i >= Fragment.COLOR_R && i <= Fragment.COLOR_B) {
                // clamp the color between 0 and 1;
                interpolated = MathUtils.clamp(interpolated, 0., 1.);
            }
            f.setAttribute(i, interpolated);
        }
    }

}
