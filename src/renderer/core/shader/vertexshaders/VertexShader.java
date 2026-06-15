package renderer.core.shader.vertexshaders;

public interface VertexShader {
    
    /**
     * Perform geometry calculations on the vertex data given as input
     * (VertexInput) and return them in a VertexOuput object.
     * 
     * @param vertex the vertex data input
     * @return a vertexOuput containg all the transformed data
     */
    VertexOutput shade(VertexInput vertex);

}
