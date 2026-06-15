package renderer.core.shader.fragmentshaders;

import renderer.controller.ColorMapFactory.Maps;

public interface FragmentShader {

	/**
	 * Shades the fragment given as parameter.
	 * 
	 * @param fragment the framgment to shade
	 * @return a FragmentOutput with the 'shaded' values
	 */
	FragmentOutput shade(Fragment fragment);

	/**
	 * Whether this fragment shader supports a colormap.
	 * Default: not supported.
	 * 
	 * @return true if the shader is compatible with a colormap, 
	 * 		   or false if it isn't.
	 */
	default boolean supportsColorMap() {
		return false;
	}

	/**
	 * Set the colormap to use. Default implementation is a no-op.
	 * Implementations that support colormaps should override this.
	 *
	 * @param map the colormap enum value
	 */
	default void setColorMap(Maps map) {
		// no-op
	}

	/**
	 * Provide a depth range [near, far] to the shader. Default no-op.
	 * Some shaders (e.g. DepthShader) need the scene depth range to map
	 * depths to colors.
	 * 
	 * @param near the lower bound of the depth range
	 * @param far  the upper bound of the depth range 
	 */
	default void setDepthRange(double near, double far) {
		// no-op
	}
}
