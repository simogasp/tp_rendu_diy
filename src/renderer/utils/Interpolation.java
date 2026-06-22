package renderer.utils;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;
import renderer.core.shader.vertexshaders.Vertex;
import renderer.core.shader.fragmentshaders.Fragment;

public class Interpolation {
    
    /**
     * Linear interpolation of a Fragment f on the edge defined by Fragment's v1 and
     * v2.
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @param f  the fragment to interpolate
     * @param middle_double_value the value to use if the two vertices (v1 and v2) are
     *                            in the same position (to avoid division by 0)
     */
    public static void interpolate2(Vertex v1, Vertex v2, Fragment f, double middle_double_value) {
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
                                    double w1, double w2, double w3, AttributeInterpolator interp) {


        //<++ 
        //++  // TODO: calculate the fragment's (f) attributes' values by interpolating the values of the
        //++  // attributes of the 3 vertices composing the triangle it is in.
        //++
        //++  // You may need to consult the methods of these classes in particular :
        //++  //     - Fragment
        //++  //     - VertexOutput
        //++
        //++  // You also need to implement the LinearInterpolator class for the rasterization to work !
        //>++
        
        //<!!
        // Interpolate the depth of the fragment
        f.setAttribute(Fragment.DEPTH, interp.interpolate(v1.depth, v2.depth, v3.depth, w1, w2, w3));
        
        // Interpolate the normals of the fragment
        for (int i = 0; i < 3; i++) {
            double value = interp.interpolate(
                v1.normal.get(i), 
                v2.normal.get(i), 
                v3.normal.get(i), 
                w1, w2, w3);
            f.setAttribute(Fragment.NORMAL_X + i, value);
        }

        // Interpolate the color of the fragment
        for (int i = 0; i < 3; i++) {
            double value = interp.interpolate(
                v1.color[i], 
                v2.color[i], 
                v3.color[i],  
                w1, w2, w3);
            f.setAttribute(Fragment.COLOR_R + i, MathUtils.clamp(value, 0, 1));
        }

        // Interpolate the alpha value of the fragment
        double value = interp.interpolate(
            v1.alpha, 
            v2.alpha,
            v3.alpha,
            w1, w2, w3);
        f.setAttribute(Fragment.COLOR_ALPHA, MathUtils.clamp(value, 0, 1));
    
        // Interpolate the world position of the fragment
        for (int i = 0; i < 3; i++) {
            value = interp.interpolate(
                v1.worldPosition.get(i),
                v2.worldPosition.get(i),
                v3.worldPosition.get(i),
                w1, w2, w3);
            f.setAttribute(Fragment.WORLD_X + i, value);
        }

        // Interpolate the UV coordinates of the fragment
        f.setAttribute(Fragment.TEXTURE_U, interp.interpolate(v1.u, v2.u, v3.u, w1, w2, w3));
        f.setAttribute(Fragment.TEXTURE_V, interp.interpolate(v1.v, v2.v, v3.v, w1, w2, w3));
        //>!!
    }
}
