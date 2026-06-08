package renderer.core.shader;

import java.awt.Color;

import renderer.algebra.MathUtils;
import renderer.core.mesh.Texture;
import renderer.core.pipeline.FragmentOutput;
import renderer.core.pipeline.FragmentShader;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into account.
 *
 * @author cdehais
 */
public class TextureShader implements FragmentShader {

    /**
     * The start index of the texture attribute.
     */
    private static final int START_TEXTURE_ATTRIBUTE = 7;

    /**
     * The number of attribute about the texture.
     */
    private static final int NUMBER_TEXTURE_ATTRIBUTE = 2;

    /** The texture to apply. */
    private Texture texture;
    /**
     * If we have to combine the texture color and
     * the original color of the fragment.
     */
    private boolean combineWithBaseColor;

    /**
     * Creates a PainterShader.
     */
    public TextureShader() {
        super();
        texture = null;
    }

    /**
     * Set the texture to use for shading.
     *
     * @param path the path to the texture image
     * @return whether the operation is a success
     */
    public boolean setTexture(String path) {
        try {
            texture = new Texture(path);
            return true;
        } catch (Exception e) {
            System.out.println("Could not load texture " + path);
            e.printStackTrace();
            texture = null;
            return false;
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
    @Override
    public FragmentOutput shade(Fragment fragment) {
        // The Fragment may not have texture coordinates
        try {
            //++ // TODO : change the return statement to return the right color
            //++ return new FragmentOutput(Color.BLACK);
            final double[] uv = fragment.getAttribute(START_TEXTURE_ATTRIBUTE, //<!!
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
            return new FragmentOutput(finalColor); //>!!
        } catch (ArrayIndexOutOfBoundsException e) {
            return new FragmentOutput(fragment.getColor());
        }
    }

    /**
     * Gets whether the color has to be combined with the base color.
     * @return whether the color has to be combined with the base color
     */
    public boolean getCombineWithBaseColor() {
        return combineWithBaseColor;
    }
}
