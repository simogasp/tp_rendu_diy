package renderer.core.pipeline;
import renderer.core.shader.Fragment;

public interface FragmentConsumer {
	void consume(Fragment fragment);
}