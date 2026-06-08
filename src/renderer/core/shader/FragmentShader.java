package renderer.core.shader;

import renderer.controller.ColorMapFactory.Maps;

public interface FragmentShader {
	FragmentOutput shade(Fragment fragment);

	/**
	 * Whether this fragment shader supports a colormap.
	 * Default: not supported.
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
	 */
	default void setDepthRange(double near, double far) {
		// no-op
	}
}
