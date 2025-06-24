package renderer.shader;

import java.awt.Color;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;
import renderer.Texture;
import renderer.algebra.MathUtils;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into account.
 *
 * @author cdehais
 */
public class TextureShader extends Shader {

    /**
     * The start index of the texture attribute.
     */
    private static final int START_TEXTURE_ATTRIBUTE = 7;

    /**
     * The number of attribute about the texture.
     */
    private static final int NUMBER_TEXTURE_ATTRIBUTE = 2;

    /** The depth buffer. */
    private DepthBuffer depth;
    /** The texture to apply. */
    private Texture texture;
    /**
     * If we have to combine the texture color and
     * the original color of the fragment.
     */
    private boolean combineWithBaseColor;

    /**
     * Creates a PainterShader with the given screen.
     *
     * @param screen the screen to draw on
     */
    public TextureShader(GraphicsWrapper screen) {
        super(screen);
        depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
        texture = null;
    }

    /**
     * Set the texture to use for shading.
     *
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
     *
     * @param combineWithBaseColor true if the texture should be combined
     *                             with the base color
     */
    public void setCombineWithBaseColor(boolean combineWithBaseColor) {
        this.combineWithBaseColor = combineWithBaseColor;
    }

    /**
     * Shade the fragment, taking the depth of the fragment into account.
     *
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        if (!depth.testFragment(fragment)) {
            return;
        }
        // The Fragment may not have texture coordinates
        try {
            // ++ // TODO
            final double[] uv = fragment.getAttribute(START_TEXTURE_ATTRIBUTE,
                    NUMBER_TEXTURE_ATTRIBUTE);
            Color texColor;
            if (texture == null) {
                texColor = Color.BLACK;
            } else {
                texColor = texture.sample(uv[0], uv[1]);
            }
            Color finalColor;
            if (combineWithBaseColor) {
                Color baseColor = fragment.getColor();
                finalColor = new Color(Math.min(MathUtils.MAX8INT,
                        baseColor.getRed() + texColor.getRed()),
                        Math.min(MathUtils.MAX8INT,
                            baseColor.getGreen() + texColor.getGreen()),
                        Math.min(MathUtils.MAX8INT,
                            baseColor.getBlue() + texColor.getBlue()));
            } else {
                finalColor = texColor;
            }
            screen.setPixel(fragment.getX(), fragment.getY(), finalColor); //>!!
        } catch (ArrayIndexOutOfBoundsException e) {
            screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
        }
        depth.writeFragment(fragment);
    }

    /**
     * Reset the shader.
     */
    public void reset() {
        depth.clear();
    }
}
