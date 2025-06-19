package renderer.shader.colormap;

import java.awt.Color;

/**
 * A ColorMap provide a list of Colors to do a color gradient.
 */
public interface ColorMap {

    /**
     * Gets the colors of the palette.
     * @return the palette
     */
    public abstract Color[] getColors();

}
