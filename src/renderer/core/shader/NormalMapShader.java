package renderer.core.shader;

import java.awt.Color;

import renderer.algebra.Vector;
import renderer.core.pipeline.FragmentOutput;
import renderer.core.pipeline.FragmentShader;

public class NormalMapShader implements FragmentShader {

    @Override
    public FragmentOutput shade(Fragment fragment) {

        Vector n = fragment.getNormal().normalize();
        
        if (Double.isNaN(n.getX()) || Double.isNaN(n.getY()) || Double.isNaN(n.getZ())) {
            return new FragmentOutput(Color.BLACK);
        }

        double r = 0.5 * (n.getX() + 1.0);
        double g = 0.5 * (n.getY() + 1.0);
        double b = 0.5 * (n.getZ() + 1.0);

        return new FragmentOutput(
            new Color(
                (float) r,
                (float) g,
                (float) b
            )
        );
    }
}
