package renderer;

import java.awt.Color;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into acount.
 * @author cdehais
 */
public class TextureShader extends Shader {

    /**
     * The depth buffer.
     */
    private DepthBuffer depth;

    /**
     * The texture to apply on the object.
     */
    private Texture texture;

    /**
     * Wether the color has to be a combination of the fragment's color
     * and the texture's color.
     */
    private boolean combineWithBaseColor;

    /**
     * Creates a PainterShader with the given screen.
     * @param screen the screen to draw on
     */
    public TextureShader(GraphicsWrapper screen) {
        super(screen);
        depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
        texture = null;
    }

    /**
     * Set the texture to use for shading.
     * @param path the path to the texture image
     */
    public void setTexture(String path) {
        try {
            texture = new Texture(path);
        } catch (Exception e) {
            System.out.println("Could not load texture " + path);
            e.printStackTrace();
            texture = null;
        }
    }

    /**
     * Set whether the texture should be combined with the base color.
     * @param combineWithBaseColor true if the texture should be combined
     * with the base color
     */
    public void setCombineWithBaseColor(boolean combineWithBaseColor) {
        this.combineWithBaseColor = combineWithBaseColor;
    }

    /**
     * Shade the fragment, taking the depth of the fragment into account.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        if (depth.testFragment(fragment)) {
            // The Fragment may not have texture coordinates
            try {
                //++ // TODO
                double[] uv = fragment.getAttribute(7, 2); //<!!
                Color texColor;
                if (texture == null) {
                    texColor = Color.BLACK;
                } else {
                    texColor = texture.sample(uv[0], uv[1]);
                }
                Color finalColor;
                if (combineWithBaseColor) {
                    Color baseColor = fragment.getColor();
                    finalColor = new Color(
                            Math.min(255, baseColor.getRed() + texColor.getRed()),
                            Math.min(255, baseColor.getGreen() + texColor.getGreen()),
                            Math.min(255, baseColor.getBlue() + texColor.getBlue()));
                } else {
                    finalColor = texColor;
                }
                screen.setPixel(fragment.getX(), fragment.getY(), finalColor); //>!!
            } catch (ArrayIndexOutOfBoundsException e) {
                screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
            }
            depth.writeFragment(fragment);
        }
    }

    /**
     * Reset the shader.
     */
    public void reset() {
        depth.clear();
    }
}
