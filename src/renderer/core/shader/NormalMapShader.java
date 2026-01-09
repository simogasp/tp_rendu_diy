package renderer.core.shader;

import renderer.algebra.Vector;
import renderer.controller.ImageWrapper;
import renderer.controller.Renderer;
import renderer.core.camera.Transformation;

public class NormalMapShader extends Shader {

    /**
     * Value tolerance to compare two double.
     */
    private static final double EPSILON = 0.001;

    /**
     * The depth buffer.
     */
    private DepthBuffer depthBuffer;
    /**
     * The transformation to get an object from the world reference to the camera
     * reference.
     */
    private Transformation xform;

    @Override
    public void reset() {
        this.depthBuffer.clear();
    }

    @Override
    public void init(final Renderer renderer, final ImageWrapper screen) {
        super.init(renderer, screen);
        this.depthBuffer = new DepthBuffer(screen.getWidth(), screen.getHeight());
        this.xform = renderer.getTransformation();
    }

    @Override
    public void shade(Fragment fragment) {
        // don't shade a normal of a non-visible point
        if (!this.depthBuffer.testFragment(fragment)) {
            return;
        }

        final Vector nn = fragment.getNormal();
        Vector n = xform.transformVector(nn);

        // some vector has NaN value so we skip it
        if (Double.isNaN(n.getX()) || Double.isNaN(n.getY()) || Double.isNaN(n.getZ())) {
            return;
        }

        // we normalize the vector to compute the color of the pixel.
        if (Math.abs(n.norm() - 1) > EPSILON) {
            n = n.normalize();
        }

        // transform a 3D direction in a color.
        final double r = n.getX() / 2 + 0.5;
        final double g = n.getY() / 2 + 0.5;
        // need to invert to map negatives z values to "high" blue colors
        final double b = -n.getZ() / 2 + 0.5;

        fragment.setColor(r, g, b);

        screen.setRGB(fragment.getX(), fragment.getY(), fragment.getColor().getRGB());
        this.depthBuffer.writeFragment(fragment);
    }

}
