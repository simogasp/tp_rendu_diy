package renderer.utils;

public interface AttributeInterpolator {
    
    /**
     * This method's role is to factorize the code for the
     * interpolation of attribute values between 3 points
     * 
     * @param a1 the value of the attribute for the first vertex
     * @param a2 the value of the attribute for the second vertex
     * @param a3 the value of the attribute for the third vertex
     * @param w1 the barycentric coordinate of the first vertex
     * @param w2 the barycentric coordinate of the second vertex
     * @param w3 the barycentric coordinate of the third vertex
     * @return the interpolated value of the attribute 
     */
    double interpolate(double a1, double a2, double a3,
                       double w1, double w2, double w3);

}
