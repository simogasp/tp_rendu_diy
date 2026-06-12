package renderer.utils;

public class LinearInterpolator implements AttributeInterpolator {
    
    @Override
    public double interpolate(double a1, double a2, double a3,
                              double w1, double w2, double w3) {
        return w1 * a1 + w2 * a2 + w3 * a3;
    }
}

