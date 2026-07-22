package renderer.core.shader.fragmentshaders;

import java.awt.Color;

public final class FragmentOutput {

    /** The final color of the fragment to output on screen. */
    private final Color color;

    /**
     * Creates a FragmentOutput, with a color.
     *
     * @param color the color of the fragment output
     */
    public FragmentOutput(Color color) {
        this.color = color;
    }

    /**
     * Get the fragment output's color.
     *
     * @return the color of the fragment output
     */
    public Color color() {
        return color;
    }
}

