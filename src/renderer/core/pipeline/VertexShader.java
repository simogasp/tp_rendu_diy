package renderer.core.pipeline;

import renderer.core.pipeline.VertexInput;
import renderer.core.pipeline.VertexOutput;

public interface VertexShader {
    
    VertexOutput shade(VertexInput vertex);

}
