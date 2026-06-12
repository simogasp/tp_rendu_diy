package renderer.core.pipeline;
import renderer.core.shader.fragmentshaders.Fragment;

public interface FragmentConsumer {
	void consume(Fragment fragment);
}