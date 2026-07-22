package renderer.core.pipeline;

import renderer.core.shader.fragmentshaders.Fragment;
import renderer.core.shader.fragmentshaders.FragmentOutput;
import renderer.core.shader.fragmentshaders.FragmentShader;

public class FragmentShaderStage implements FragmentConsumer {

    /** The fragment shader to be used in this stage. */
	private final FragmentShader shader;

    /** The outputmerger that will end ths stage. */
    private final OutputMerger merger;

    /**
     * Create a FragmentShaderStage.
     *
     * @param shader the fragment shader to use
     * @param merger the output merger to use
     */
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
