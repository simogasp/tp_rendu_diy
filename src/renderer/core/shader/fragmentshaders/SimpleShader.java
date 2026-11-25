package renderer.core.shader.fragmentshaders;

import java.awt.Color;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into account.
 * @author cdehais
 */
public class SimpleShader implements FragmentShader {

    /**
     * Shades the fragment by simply returning its raw color (albedo).
     * (no other operations)
     *
     * @param fragment the fragment to shade
     * @return a FragmentOutput containing the raw color of the fragment
     */
    @Override
    public FragmentOutput shade(Fragment fragment) {
        Color black = new Color(0f, 0f, 0f);

        if (fragment.getColor().equals(black)) {
            return new FragmentOutput(new Color(1.0f, 0.0f, 0.0f));
        }

        return new FragmentOutput(fragment.getColor());
    }
}
