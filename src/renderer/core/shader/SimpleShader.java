package renderer.core.shader;

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
