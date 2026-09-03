package renderer.core.shader.vertexshaders;

public interface VertexShader {
    
    /**
     * Perform geometry calculations on the vertex data given as input
     * (VertexInput) and modifies its attributes in-place.
     * 
     * @param vertex the vertex data input
     */
    void shade(Vertex vertex);

}
