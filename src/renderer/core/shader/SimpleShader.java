package renderer.core.shader;

import renderer.core.pipeline.FragmentOutput;
import renderer.core.pipeline.FragmentShader;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into account.
 * @author cdehais
 */
public class SimpleShader implements FragmentShader {

    @Override
    public FragmentOutput shade(Fragment fragment) {

        return new FragmentOutput(fragment.getColor());
    }
}
