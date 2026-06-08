package renderer.core.pipeline;

import renderer.core.shader.Fragment;

public class FragmentShaderStage implements FragmentConsumer {

	private final FragmentShader shader;
    private final OutputMerger merger;

    public FragmentShaderStage(FragmentShader shader,
            				   OutputMerger merger) {

        this.shader = shader;
        this.merger = merger;
    }

    @Override
    public void consume(Fragment fragment) {
        FragmentOutput output = shader.shade(fragment);
        merger.merge(fragment, output);
    }
    
}
