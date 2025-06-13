package renderer;

import renderer.algebra.Vector3;

public class NormalMapShader extends Shader {

    /**
     * The depth buffer.
     */
    private DepthBuffer depthBuffer;

    
    /**
     * Creates a NormalMapShader with the given screen.
     * @param screen the screen to draw on
     */
    protected NormalMapShader(GraphicsWrapper screen) {
        super(screen);
        this.depthBuffer = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }



    @Override
    public void reset() {
        this.depthBuffer.clear();
    }



    @Override
    public void shade(Fragment fragment) {
        if (this.depthBuffer.testFragment(fragment)) {
            // compute the color of the pixel

            Vector3 n = fragment.getNormal();
            if (!(n.norm() == 1))
                n.normalize();

            // some vector has NaN value
            if (Double.isNaN(n.getX())) {
                n.set(0, 0);
            }
            if (Double.isNaN(n.getY())) {
                n.set(1, 0);
            }
            if (Double.isNaN(n.getZ())) {
                n.set(2, 0);
            }

            // transform a 3D direction in a color.
            double r = n.getX() / 2 + 0.5;
            double g = n.getY() / 2 + 0.5;
            double b = n.getZ() / 2 + 0.5;

            fragment.setColor(r, g, b);

            screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
            this.depthBuffer.writeFragment(fragment);
        }
    }

    
    
}
