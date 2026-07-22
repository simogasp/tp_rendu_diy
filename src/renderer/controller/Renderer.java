package renderer.controller;

import java.io.IOException;
import java.util.Optional;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.controller.ColorMapFactory.Maps;
import renderer.core.shader.fragmentshaders.PhongShader;
import renderer.core.shader.vertexshaders.GouraudShader;
import renderer.core.shader.vertexshaders.SinusVertexShader;
import renderer.core.shader.vertexshaders.SimpleVertexShader;
import renderer.core.camera.Transformation;
import renderer.core.light.Lighting;
import renderer.core.mesh.Mesh;
import renderer.core.mesh.Scene;
import renderer.core.pipeline.FragmentShaderStage;
import renderer.core.pipeline.OutputMerger;
import renderer.core.pipeline.PerspectiveCorrectRasterizer;
import renderer.core.pipeline.Rasterizer;
import renderer.core.shader.vertexshaders.Vertex;
import renderer.core.shader.vertexshaders.VertexShader;
import renderer.core.shader.fragmentshaders.TextureShader;
import renderer.core.shader.fragmentshaders.Fragment;
import renderer.core.shader.fragmentshaders.FragmentOutput;
import renderer.core.shader.fragmentshaders.FragmentShader;

/**
 * The Renderer class drives the rendering pipeline: read in a scene, projects
 * the vertices and rasterizes every faces / edges.
 *
 * @author cdehais
 */
public final class Renderer {

    /**
     * The default scene filename.
     */
    private static final String DEFAULT_FILENAME = "data/example0.scene";

    /** The divider of the normal length. */
    private static final double DIVIDER = 100;

    /** The length of the normal. */
    private static double normalLength;

    /** The scene. */
    private Scene scene;

    /** The mesh. */
    private Mesh mesh;

    /** The Vertex shader. */
    private VertexShader vertexShader;

    /** The rasterizer. */
    private Rasterizer rasterizer;

    /** The output merger. */
    private OutputMerger merger;

    /** The fragmentShaderStage. */
    private FragmentShaderStage fragmentShaderStage;

    /** The shader. */
    private FragmentShader shader;

    /** The transformation. */
    private Transformation xform;

    /** The lighting. */
    private final Lighting lighting;

    /** Whether lighting is enabled. */
    private boolean lightingEnabled;

    /** Whether the normals are drawn. */
    private boolean normalsRendered;

    /** Whether the image contains vertex. */
    private boolean vertexRendered;

    /** Whether the image contains edges. */
    private boolean wiredRendered;

    /** Whether the image contains faces. */
    private boolean solidRendered;

    /** Whether to render the wireframe in solid or non-solid mode. */
    private boolean solidWiredRendered;

    /** Whether to use perspective-correct rasterization. */
    private boolean usePerspectiveCorrect;

    /**
     * Store the last texture set.
     */
    private String texture;

    /**
     * Whether a TextureShader has to combine colors in render.
     */
    private boolean combineColorState;

    /**
     * A default shader that throws an exception when used.
     */
    private static final class DefaultShader implements FragmentShader {

        private DefaultShader() {}

        @Override
        public FragmentOutput shade(final Fragment fragment) {
            throw new IllegalArgumentException("Any Shader has been set.");
        }
    }

    /**
     * Creates a renderer, a controller with default values.
     *
     * @throws IOException if files doesn't exist
     */
    public Renderer() throws IOException {
        // set the shader Factory up
        ShaderFactory.init();

        // creates a lighting
        lighting = new Lighting();

        // set default scene : cube
        setScene(DEFAULT_FILENAME);

        // set a default shader that shouldn't been used.
        shader = new DefaultShader();
        fragmentShaderStage = new FragmentShaderStage(shader, merger);
        rasterizer = new Rasterizer(fragmentShaderStage);

        // draw nothing
        wiredRendered = false;
        solidRendered = false;
        lightingEnabled = false;
        normalsRendered = false;
        usePerspectiveCorrect = false;
    }

    /**
     * Renders the normals of the mesh.
     */
    public void renderNormal() {
        final Vector[] vertices = mesh.getVertices();
        final Vertex[] outputs = runVertexShader();

        for (int i = 0; i < vertices.length; i++) {
            final Vector vertex = vertices[i];
            final Vertex output = outputs[i];
            final Vector normal = output.getNormal();

            final Vector destVector = new Vector(
                    vertex.get(0) + normalLength * normal.get(0),
                    vertex.get(1) + normalLength * normal.get(1),
                    vertex.get(2) + normalLength * normal.get(2));

            final Vector destVectorPoint = xform.projectPoint(destVector);

            int x = (int) Math.round(destVectorPoint.get(0));
            int y = (int) Math.round(destVectorPoint.get(1));

            double[] red = new double[3];
            red[0] = 1.0;
            red[1] = 0.0;
            red[2] = 0.0;

            final Vertex destVertex = new Vertex(x, y);
            destVertex.setColor(red);
            destVertex.setNormal(normal);
            destVertex.setWorldPosition(destVector);
            destVertex.setDepth(destVectorPoint.get(2));
            destVertex.setAlpha(output.getAlpha());
            destVertex.setU(output.getU());
            destVertex.setV(output.getV());

            final Vertex originVertex = output.clone();
            originVertex.setColor(red);

            rasterizer.rasterizeEdge(originVertex, destVertex);
        }
    }

    /**
     * Enables or disables lighting.
     *
     * @param enabled true to enable lighting, false to disable it
     */
    public void setLightingEnabled(final boolean enabled) {
        lightingEnabled = enabled;
        initVertexShader();

        if (shader instanceof PhongShader) {
            if (lightingEnabled) {
                ((PhongShader) shader).enableLighting();
            } else {
                ((PhongShader) shader).disableLighting();
            }
        }
    }

    /**
     * Sets the scene with the given filename.
     *
     * @param fileName the filename of the scene
     * @throws IOException if the file doesn't exist
     */
    public void setScene(final String fileName) throws IOException {
        scene = new Scene(fileName);
        // update mesh
        mesh = new Mesh(this.scene.getMeshFileName());
        // update transformation
        xform = new Transformation();
        xform.setLookAt(scene.getCameraPosition(),
                scene.getCameraLookAt(),
                scene.getCameraUp());
        xform.setProjection();
        xform.setCalibration(scene.getCameraFocal(),
                scene.getScreenW(),
                scene.getScreenH());

        // instantiate vertex shader after the transformation is configured
        initVertexShader();

        // add lights of the scene
        lighting.reset();
        lighting.addAmbientLight(scene.getAmbientI());
        final double[] lightCoord = scene.getSourceCoord();
        lighting.addPointLight(lightCoord[0],
                lightCoord[1],
                lightCoord[2],
                scene.getSourceI());

        // determine the normal length
        initNormalLength();
    }

    /**
     * Sets the rasterizer with a Rasterizer.
     */
    public void setRasterizer() {
        this.usePerspectiveCorrect = false;
    }

    /**
     * Sets the rasterizer with a PerspectiveCorrectRasterizer.
     */
    public void setPerspectiveCorrectRasterizer() {
        this.usePerspectiveCorrect = true;
    }

    /**
     * Sets the shader to the given values.
     *
     * @param shader the new shader.
     */
    public void setShader(final FragmentShader shader) {
        this.shader = shader;
        initVertexShader();

        this.fragmentShaderStage = new FragmentShaderStage(shader, merger);

        this.rasterizer = new Rasterizer(fragmentShaderStage);
    }

    /**
     * Initialize the default vertex shader.
     */
    private void initVertexShader() {
        final VertexShader projectionShader = new SimpleVertexShader(xform);

        if (!lightingEnabled || shader instanceof PhongShader) {
            this.vertexShader = projectionShader;
            return;
        }

        final VertexShader gouraudShader = new GouraudShader(scene, lighting);
        this.vertexShader = vertex -> {
            projectionShader.shade(vertex);
            gouraudShader.shade(vertex);
        };
    }

    /**
     * Render an image from the current parameters.
     *
     * @return the rendered image.
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    public ImageWrapper render()
            throws SizeMismatchException {

        // returned image
        final ImageWrapper res = new ImageWrapper(scene);

        merger = new OutputMerger(res);

        fragmentShaderStage = new FragmentShaderStage(shader, merger);

        if (usePerspectiveCorrect) {
            rasterizer = new PerspectiveCorrectRasterizer(fragmentShaderStage);
        } else {
            rasterizer = new Rasterizer(fragmentShaderStage);
        }

        // Compute scene depth range and inform shader (useful for DepthShader)
        final Vertex[] allVertexOutputs = runVertexShader();

        // Debug output to help locate invisible render issues
        if (allVertexOutputs == null || allVertexOutputs.length == 0) {
            System.out.println("Renderer.render: no vertex outputs produced by vertex shader");
        } else {
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            for (Vertex vo : allVertexOutputs) {
                if (vo == null) continue;
                if (vo.getX() < minX) minX = vo.getX();
                if (vo.getX() > maxX) maxX = vo.getX();
                if (vo.getY() < minY) minY = vo.getY();
                if (vo.getY() > maxY) maxY = vo.getY();
            }
            System.out.println("Renderer.render: produced " + allVertexOutputs.length + " vertices; x range=[" + minX + "," + maxX + "] y range=[" + minY + "," + maxY + "]");
            for (int i = 0; i < Math.min(5, allVertexOutputs.length); i++) {
                Vertex v = allVertexOutputs[i];
                if (v != null) System.out.println("  v[" + i + "]=(" + v.getX() + "," + v.getY() + ") depth=" + v.getDepth());
            }
        }

        try {
            if (allVertexOutputs != null && allVertexOutputs.length > 0) {
                double minDepth = Double.POSITIVE_INFINITY;
                double maxDepth = Double.NEGATIVE_INFINITY;
                for (Vertex v : allVertexOutputs) {
                    final double d = v.getDepth();
                    if (d < minDepth) minDepth = d;
                    if (d > maxDepth) maxDepth = d;
                }
                shader.setDepthRange(minDepth, maxDepth);
            }
        } catch (Exception e) {
            // If anything goes wrong computing depths, continue without setting range
        }

        if (vertexRendered) {
            // render vertices if needed
            renderVertices();
        }

        if (wiredRendered) {
            if (solidWiredRendered) {
                // first rasterization pass to initialize the depthBuffer
                renderSolid(true);
            }
            // render edges if needed
            renderWireframe(solidWiredRendered);
            if (!solidWiredRendered) {
                // only render the vertices if the "Solid Wireframe" mode
                // isn't active : if it is, the "correct" vertices are rendered
                // by the renderWireframe method
                renderVertices();
            }
        }

        if (solidRendered) {
            // render faces if needed
            renderSolid(false);
        }

        // render the normals if needed
        if (normalsRendered) {
            renderNormal();
        }

        return res;
    }

    /**
     * Projects the vertices of the mesh into the screen space.
     *
     * @return an array of vertex outputs
     */
    public Vertex[] runVertexShader() {
        if (vertexShader == null) {
            initVertexShader();
        }

        Vertex[] vertices = buildInputsFromMesh();

        for (int i = 0; i < vertices.length; i++) {
            vertexShader.shade(vertices[i]);
        }

        return vertices;
    }

    /**
     * Creates an array of vertex inputs (for the
     * rasterization phase) from the mesh.
     *
     * @return an array of VertexInput
     */
    private Vertex[] buildInputsFromMesh() {

        Vector[] vertices = mesh.getVertices();
        Vector[] normals = mesh.getNormals();
        double[] colors = mesh.getColors();
        double[] texCoords = mesh.getTextureCoordinates();

        Vertex[] inputs = new Vertex[vertices.length];

        for (int i = 0; i < vertices.length; i++) {

            Vertex in = new Vertex();

            in.setWorldPosition(vertices[i]);
            in.setNormal(normals[i]);

            in.setColor(new double[] {
                colors[3 * i],
                colors[3 * i + 1],
                colors[3 * i + 2],
                1.0
            });

            if (texCoords != null) {
                in.setU(texCoords[2 * i]);
                in.setV(texCoords[2 * i + 1]);
            }

            inputs[i] = in;
        }

        return inputs;
    }

    /**
     * Gets the transformation to convert the world reference to the camera
     * reference.
     *
     * @return the transformation
     */
    public Transformation getTransformation() {
        return xform;
    }

    /**
     * Sets whether the normals should be drawn.
     *
     * @param normalsRendered the normals should be normalsRendered
     */
    public void setNormalsRendered(final boolean normalsRendered) {
        this.normalsRendered = normalsRendered;
    }

    /**
     * Sets whether the wireFrameRender should be done.
     *
     * @param wiredRendered the new value
     */
    public void setWiredRendered(final boolean wiredRendered) {
        this.wiredRendered = wiredRendered;
    }

    /**
     * Sets whether the wireframe should be rendered on top of solid.
     *
     * @param solidWiredRendered the new value
     */
    public void setSolidWiredRendered(final boolean solidWiredRendered) {
        this.solidWiredRendered = solidWiredRendered;
    }

    /**
     * Sets whether the solidRender should be done.
     *
     * @param solidRendered the new value
     */
    public void setSolidRendered(final boolean solidRendered) {
        this.solidRendered = solidRendered;
    }

    /**
     * Computes the length of the normals for the rendering.
     */
    private void initNormalLength() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vector vertex : mesh.getVertices()) {
            if (vertex.get(0) < minX) {
                minX = vertex.get(0);
            }
            if (vertex.get(0) > maxX) {
                maxX = vertex.get(0);
            }
            if (vertex.get(1) < minY) {
                minY = vertex.get(1);
            }
            if (vertex.get(1) > maxY) {
                maxY = vertex.get(1);
            }
            if (vertex.get(2) < minZ) {
                minZ = vertex.get(2);
            }
            if (vertex.get(2) > maxZ) {
                maxZ = vertex.get(2);
            }
        }

        // The length of the normal is approximately equal to 1/100 of the diagonal
        // length of the bounding box
        normalLength = (new Vector(maxX - minX, maxY - minY, maxZ - minZ)).norm() / DIVIDER;
    }

    /**
     * Renders the wireframe of the mesh.
     *
     * @param solidWireframe a boolean to toggle solid wireframe mode
     */
    private void renderWireframe(boolean solidWireframe) {
        final Vertex[] outputs = runVertexShader();
        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            //
            // BACKFACE CULLING
            //
            //if(solidWireframe) {
            //    final Vertex v1 = outputs[faces[i]];
            //    final Vertex v2 = outputs[faces[i + 1]];
            //    final Vertex v3 = outputs[faces[i + 2]];

            //    double area = Rasterizer.triangleArea(v1, v2, v3);
            //    final double eps = 1e-6;

            //    if(area >= -eps) {
            //        continue;
            //    }
            //}
            for (int j = 0; j < 3; j++) {
                final Vertex v1 = outputs[faces[i + j]];
                final Vertex v2 = outputs[faces[i + ((j + 1) % 3)]];
                rasterizer.rasterizeEdge(v1, v2);

                if (solidWireframe) {
                    rasterizer.rasterizeVertex(v1);
                    rasterizer.rasterizeVertex(v2);
                }
            }
        }
    }

    /**
     * Renders the vertices of the mesh.
     */
    private void renderVertices() {
        final Vertex[] outputs = runVertexShader();
        for (Vertex vertex : outputs) {
            rasterizer.rasterizeVertex(vertex);
        }
    }

    /**
     * Renders the solid of the mesh.
     *
     * @param onlyDepth a boolean to know if we want to interpolate all of the
     *                  fragment's attributes or only its depth
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    private void renderSolid(boolean onlyDepth)
            throws SizeMismatchException {
        final Vertex[] outputs = runVertexShader();
        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            final Vertex v1 = outputs[faces[i]];
            final Vertex v2 = outputs[faces[i + 1]];
            final Vertex v3 = outputs[faces[i + 2]];

            rasterizer.rasterizeFace(v1, v2, v3, onlyDepth);
        }
    }

    /**
     * Sets the shader to a instance of the given shader value.
     *
     * @param shaderSelected the name of a implementation of Shader
     * @return whether the operation is successful
     */
    public boolean setShader(final String shaderSelected) {
        final Optional<FragmentShader> optionalShader = ShaderFactory.create(shaderSelected);
        if (optionalShader.isPresent()) {
            final FragmentShader newShader = optionalShader.get();
            setShader(newShader);
            setTexture(texture);
            initPhong();
            setLightingEnabled(lightingEnabled);
            setCombineWithBaseColor(combineColorState);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the parameter combine with base color of the Texture shader.
     *
     * @param selected the new value
     */
    public void setCombineWithBaseColor(final boolean selected) {
        if (!(shader instanceof TextureShader)) {
            return;
        }
        ((TextureShader) shader).setCombineWithBaseColor(selected);
    }

    /**
     * Set the texture from the file given.
     *
     * @param path the path of the file
     * @return whether the operation as been correctly made.
     */
    public boolean setTexture(final String path) {
        if (path == null) {
            return true;
        }
        texture = path;
        if (!(shader instanceof TextureShader)) {
            return true;
        }
        return ((TextureShader) shader).setTexture(path);
    }


    /**
     * Initialize the Phong Shader.
     *
     * @return whether the operation as been correctly made.
     */
    public boolean initPhong() {
        if (!(shader instanceof PhongShader)) {
            return true;
        }
        return ((PhongShader) shader).init(this.scene, this.lighting);
    }

    /**
     * Sets the Vertex render on te given value.
     *
     * @param selected the new value.
     */
    public void setVertexRendered(boolean selected) {
        vertexRendered = selected;
    }

    /**
     * Sets the color map for depth shader.
     *
     * @param map the new colormap to use.
     */
    public void setColorMap(Maps map) {
        if (shader.supportsColorMap()) {
            shader.setColorMap(map);
        }
    }
}
