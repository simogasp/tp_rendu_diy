package renderer.utils;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;
import renderer.core.pipeline.VertexOutput;
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
    public static void interpolate2(VertexOutput v1, VertexOutput v2, Fragment f, double middle_double_value) {
        final int x1 = v1.x;
        final int y1 = v1.y;
        final int x2 = v2.x;
        final int y2 = v2.y;
        final int x = f.getX();
        final int y = f.getY(); // was an x ?? maybe it was on purpose ??

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

        // Interpolate the depth
        double interpolated = (1.0 - alpha) * v1.depth + alpha * v2.depth;
        f.setAttribute(Fragment.DEPTH, interpolated);

        // Interpolate the normal
        Vector n1 = v1.normal;
        Vector n2 = v2.normal;
        
        for(int i = 0 ; i < 3 ; i++) {
            interpolated = (1.0 - alpha) * n1.get(i) + alpha * n2.get(i);
            f.setAttribute(Fragment.NORMAL_X + i, interpolated);
        }

        // Interpolate the world position
        Vector wp1 = v1.worldPosition;
        Vector wp2 = v2.worldPosition;
        
        for(int i = 0 ; i < 3 ; i++) {
            interpolated = (1.0 - alpha) * wp1.get(i) + alpha * wp2.get(i);
            f.setAttribute(Fragment.WORLD_X + i, interpolated);
        }

        // Interpolate the color
        double[] c1 = v1.color;
        double[] c2 = v2.color;

        for(int i = 0 ; i < 3 ; i++) {
            interpolated = MathUtils.clamp((1.0 - alpha) * c1[i] + alpha * c2[i], 0, 1);
            f.setAttribute(Fragment.COLOR_R + i, interpolated);
        }

        // Interpolate the alpha value
        double a1 = v1.alpha;
        double a2 = v2.alpha;
        interpolated = MathUtils.clamp((1.0 - alpha) * a1 + alpha * a2, 0, 1);
        f.setAttribute(Fragment.COLOR_ALPHA, interpolated);

        // Interpolate the UV coordinates
        interpolated = (1.0 - alpha) * v1.u + alpha * v2.u;
        f.setAttribute(Fragment.TEXTURE_U, interpolated);
        interpolated = (1.0 - alpha) * v1.v + alpha * v2.v;
        f.setAttribute(Fragment.TEXTURE_V, interpolated);
    }


    /**
     * Linear interpolation of a Fragment f on the triangle defined by 
     * Vertices v1, v2 and v3.
     *
     * @param v1 the first vertex of the triangle
     * @param v2 the second vertex of the triangle
     * @param v2 the third vertex of the triangle
     * @param f  the fragment to interpolate
     */
    public static void interpolate3(VertexOutput v1, VertexOutput v2, VertexOutput v3, Fragment f) {

        

    }
}
