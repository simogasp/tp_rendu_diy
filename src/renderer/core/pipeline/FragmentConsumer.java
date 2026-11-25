package renderer.core.pipeline;
import renderer.core.shader.fragmentshaders.Fragment;

/**
 * Interface for consuming fragments in the rendering pipeline.
 * FragmentConsumer
 */
public interface FragmentConsumer {

	/**
	 * Consumes the given fragment in the rendering pipeline.
	 * @param fragment the fragment to be consumed
	 */
	void consume(Fragment fragment);
}
