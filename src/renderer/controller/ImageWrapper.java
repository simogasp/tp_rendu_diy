package renderer.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;

import renderer.model.shader.Fragment;
import renderer.model.mesh.Scene;
import renderer.model.rasterizer.Rasterizer;
import renderer.model.shader.Shader;
import renderer.model.shader.TextureShader;

public class ImageWrapper extends BufferedImage {

    /**
     * The scene used in this image.
     */
    private final Scene scene;
    /**
     * The shader used in this image.
     */
    private final Shader shader;
    /**
     * The rasterizer used in this image.
     */
    private final Rasterizer rasterizer;
    /**
     * Wether the lighting is enabled.
     */
    private final boolean lightingEnabled;
    /**
     * Wether the normals drawn is enabled.
     */
    private final boolean normalsEnabled;
    /**
     * Wether the render wired is enabled.
     */
    private final boolean wired;
    /**
     * Wether the render solid is enabled.
     */
    private final boolean solid;

    /**
     * Creates a Image wrapper with the image configuration.
     *
     * @param scene           the scene of the image
     * @param shader          the shader of the image
     * @param rasterizer      the raster of the image
     * @param lightingEnabled wether the lighting is enabled on the image
     * @param normalsEnabled  wether the normals are drawn of the image
     * @param wired           wether the render wired is enabled
     * @param solid           wether the render solid is enabled
     */
    public ImageWrapper(Scene scene, Shader shader, Rasterizer rasterizer,
            boolean lightingEnabled, boolean normalsEnabled,
            boolean wired, boolean solid) {
        super(scene.getScreenW(), scene.getScreenH(), BufferedImage.TYPE_3BYTE_BGR);
        this.scene = scene;
        this.shader = shader;
        this.rasterizer = rasterizer;
        this.lightingEnabled = lightingEnabled;
        this.normalsEnabled = normalsEnabled;
        this.wired = wired;
        this.solid = solid;
    }

    /**
     * Places a pixel of rgb color in the (x, y) pixel.
     *
     * @param x   the abscissa of the pixel
     * @param y   the ordinate of the pixel
     * @param rgb the color of the pixel
     */
    public void setPixel(final int x, final int y, final Color rgb) {
        if (isClipped(new Fragment(x, y))) {
            return;
        }
        this.setRGB(x, y, rgb.getRGB());
    }

    /**
     * Wether an other Image has the same parameter.
     *
     * @param otherImageWrapper the other image.
     * @return wether the other Image has the parameters.
     */
    public boolean isSameParams(final ImageWrapper otherImageWrapper) {
        boolean ok = true;
        if (shader instanceof TextureShader
                && otherImageWrapper.shader instanceof TextureShader) {
            final TextureShader textShader = (TextureShader) shader;
            final TextureShader other = (TextureShader) otherImageWrapper.shader;
            ok = textShader.getCombineWithBaseColor() == other.getCombineWithBaseColor();
        }
        return ok
                && scene == otherImageWrapper.scene
                && shader == otherImageWrapper.shader
                && rasterizer == otherImageWrapper.rasterizer
                && lightingEnabled == otherImageWrapper.lightingEnabled
                && normalsEnabled == otherImageWrapper.normalsEnabled
                && wired == otherImageWrapper.wired
                && solid == otherImageWrapper.solid;
    }

    /**
     * Test whether the fragment falls onto the screen.
     *
     * @param fragment the fragment to test
     * @return true if the fragment is clipped, false otherwise
     */
    public boolean isClipped(Fragment fragment) {
        return ((fragment.getX() < 0) || (fragment.getX() >= super.getWidth())
                || (fragment.getY() < 0) || (fragment.getY() >= super.getHeight()));
    }
}
