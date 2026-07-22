package renderer.utils;

public class LinearInterpolator implements AttributeInterpolator {

    @Override
    public double interpolate(double a1, double a2, double a3,
                              double w1, double w2, double w3) {
        //<++
        //++ // TODO : interpolate linearly the value of
        //++ // the attribute, based on the barycentric
        //++ // coordinates of the 3 vertices composing the
        //++ // triangle the fragment is in.
        //>++

        //++ return 0;
        return w1 * a1 + w2 * a2 + w3 * a3; //!!
    }
}

