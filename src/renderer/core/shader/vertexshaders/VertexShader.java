package renderer.core.shader.vertexshaders;

/**
 * Represents a vertex shader that performs geometry calculations on vertex data.
 * Implementations of this interface modify the attributes of a vertex in-place.
 */
public interface VertexShader {

    /**
     * Perform geometry calculations on the vertex data given as input
     * (VertexInput) and modifies its attributes in-place.
     *
     * @param vertex the vertex data input
     */
    void shade(Vertex vertex);

}
