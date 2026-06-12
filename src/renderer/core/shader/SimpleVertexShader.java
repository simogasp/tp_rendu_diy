package renderer.core.shader;

import renderer.algebra.Vector;
import renderer.core.camera.Transformation;
import renderer.core.pipeline.VertexInput;
import renderer.core.pipeline.VertexOutput;
import renderer.core.pipeline.VertexShader;

public class SimpleVertexShader implements VertexShader {

    private final Transformation xform;

    public SimpleVertexShader(Transformation xform) {
        this.xform = xform;
    }

    @Override
    public VertexOutput shade(VertexInput in) {

        VertexOutput out = new VertexOutput();

        Vector pVertex = xform.projectPoint(in.position);

        out.x = (int) Math.round(pVertex.get(0));
        out.y = (int) Math.round(pVertex.get(1));
        out.depth = pVertex.get(2);

        out.worldPosition = in.position;
        out.normal = in.normal;

        out.color = in.color;
        out.alpha = 1.0;

        out.u = in.u;
        out.v = in.v;

        return out;
    }
}
