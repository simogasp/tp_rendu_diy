package renderer.core.shader.fragmentshaders;

import java.awt.Color;

public final class FragmentOutput {

    private final Color color;

    public FragmentOutput(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }
}

