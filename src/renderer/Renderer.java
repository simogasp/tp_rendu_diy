package renderer;

import java.awt.Color;
import java.io.IOException;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.gui.RenderPanel;
import renderer.light.Lighting;
import renderer.rasterizer.PerspectiveCorrectRasterizer;
import renderer.rasterizer.Rasterizer;
import renderer.shader.Shader;

/**
 * The Renderer class drives the rendering pipeline: read in a scene, projects
 * the vertices and rasterizes every faces / edges.
 *
 * @author cdehais
 */
public final class Renderer {

    /** The scene. */
    private Scene scene;
    /** The mesh. */
    private Mesh mesh;
    /** The rasterizer. */
    private Rasterizer rasterizer;
    /** The screen. */
    private RenderPanel screen;
    /** The shader. */
    private Shader shader;
    /** The transformation. */
    private Transformation xform;
    /** The lighting. */
    private Lighting lighting;
    /** Whether lighting is enabled.. */
    private boolean lightingEnabled;
    /** The length of the normal to render. */
    private double normalLength;

    // Private constructor to prevent instantiation
    public Renderer() {
    }

    /**
     * Initialize the renderer with the given scene file.
     *
     * @param sceneFilename the scene file to load
     * @throws IOException if the scene file cannot be loaded
     */
    public void init(final String sceneFilename) throws IOException {
        scene = new Scene(sceneFilename);
        mesh = new Mesh(scene.getMeshFileName());

        xform = new Transformation();
        xform.setLookAt(scene.getCameraPosition(),
                scene.getCameraLookAt(),
                scene.getCameraUp());
        xform.setProjection();
        xform.setCalibration(scene.getCameraFocal(),
                scene.getScreenW(),
                scene.getScreenH());

        screen.updateDims(scene.getScreenW(), scene.getScreenH());

        lighting = new Lighting();
        lighting.addAmbientLight(scene.getAmbientI());
        double[] lightCoord = scene.getSourceCoord();
        lighting.addPointLight(lightCoord[0],
                lightCoord[1],
                lightCoord[2],
                scene.getSourceI());

        // determine the normal length
        initNormalLength();

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

        // The length of the normal is approximately equal to 1/100 of the minimal
        // length of the bounding 
        normalLength = Math.min(Math.min(maxX - minX, maxY - minY), maxZ - minZ) / 100;
    }
    
    /**
     * Projects the vertices of the mesh into the screen space.
     *
     * @return an array of fragments
     */
    private Fragment[] projectVertices() {
        Vector[] vertices = mesh.getVertices();
        Vector[] normals = mesh.getNormals();
        double[] colors = mesh.getColors();
        
        Fragment[] fragments = new Fragment[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            Vector pVertex = xform.projectPoint(vertices[i]);
            // Vector pNormal = xform.transformVector (normals[i]);
            Vector pNormal = normals[i];

            int x = (int) Math.round(pVertex.get(0));
            int y = (int) Math.round(pVertex.get(1));
            fragments[i] = new Fragment(x, y);
            fragments[i].setDepth(pVertex.get(2));
            fragments[i].setNormal(pNormal);

            double[] texCoords = mesh.getTextureCoordinates();
            if (texCoords != null) {
                fragments[i].setAttribute(7, texCoords[2 * i]);
                fragments[i].setAttribute(8, texCoords[2 * i + 1]);
            }

            if (!lightingEnabled) {
                fragments[i].setColor(colors[3 * i],
                        colors[3 * i + 1],
                        colors[3 * i + 2]);
            } else {
                double[] color = new double[3];
                color[0] = colors[3 * i];
                color[1] = colors[3 * i + 1];
                color[2] = colors[3 * i + 2];
                double[] material = scene.getMaterial();
                final Vector v3d = new Vector(vertices[i].getSubVector(0, 3));
                double[] litColor = lighting.applyLights(v3d, pNormal, color,
                        scene.getCameraPosition(),
                        material[0], material[1], material[2], material[3]);
                fragments[i].setColor(litColor[0], litColor[1], litColor[2]);
            }
        }

        return fragments;
    }

    /**
     * Renders the wireframe of the mesh.
     */
    public void renderWireframe() {
        Fragment[] fragment = projectVertices();
        int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            for (int j = 0; j < 3; j++) {
                Fragment v1 = fragment[faces[i + j]];
                Fragment v2 = fragment[faces[i + ((j + 1) % 3)]];
                rasterizer.rasterizeEdge(v1, v2);
            }
        }
    }

    /**
     * Renders the normals of the mesh.
     */
    public void renderNormal() {
        final Vector[] vertices = mesh.getVertices();
        final Fragment[] fragments = projectVertices();

        for (int i = 0; i < vertices.length; i++) {
            final Vector vertex = vertices[i];
            final Fragment fragment = fragments[i];
            final Vector normal = fragment.getNormal();

            final Vector destVector = new Vector(
                    vertex.get(0) + normalLength * normal.get(0),
                    vertex.get(1) + normalLength * normal.get(1),
                    vertex.get(2) + normalLength * normal.get(2));

            final Vector destVectorPoint = xform.projectPoint(destVector.homogeneous());

            int x = (int) Math.round(destVectorPoint.get(0));
            int y = (int) Math.round(destVectorPoint.get(1));

            final Fragment destFragment = new Fragment(x, y);
            destFragment.setColor(Color.RED);
            destFragment.setNormal(normal);
            destFragment.setDepth(destVectorPoint.get(2));

            final Fragment originFragment = fragment.clone();
            originFragment.setColor(Color.RED);

            rasterizer.rasterizeEdge(originFragment, destFragment);

        }
    }

    /**
     * Renders the solid of the mesh.
     *
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    public void renderSolid() {
        Fragment[] fragments = projectVertices();
        int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            Fragment v1 = fragments[faces[i]];
            Fragment v2 = fragments[faces[i + 1]];
            Fragment v3 = fragments[faces[i + 2]];

            rasterizer.rasterizeFace(v1, v2, v3);
        }
    }

    /**
     * Enables or disables lighting.
     *
     * @param enabled true to enable lighting, false to disable it
     */
    public void setLightingEnabled(boolean enabled) {
        lightingEnabled = enabled;
    }

    /**
     * Wait for a number of seconds.
     *
     * @param sec the number of seconds to wait
     */
    public void wait(int sec) {
        try {
            final long millis = 1000;
            Thread.sleep(sec * millis);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * Change the render panel by the given one.
     * 
     * @param renderPanel the new screen
     */
    public void setScreen(RenderPanel renderPanel) {
        screen = renderPanel;
    }

    /**
     * Changes the shader by the given one.
     * 
     * @param nShader the new shader
     */
    public void setShader(Shader nShader) {
        shader = nShader;
        updateRasterizer();
    }

    /**
     * Gets the transformation.
     * 
     * @return the transformation
     */
    public Transformation getXform() {
        return xform;
    }

    /**
     * Updates the rasterizer to a new one if it doesn't exist or change the shader.
     */
    private void updateRasterizer() {
        if (rasterizer != null) {
            rasterizer.setShader(shader);
        } else {
            rasterizer = new Rasterizer(shader);
        }
    }

    /**
     * Resets the shader for a new render.
     */
    public void resetShader() {
        if (shader != null) {
            shader.reset();
        }
    }

    /**
     * Sets the rasterizer to Rasterizer.
     */
    public void setRasterizer() {
        rasterizer = new Rasterizer(shader);
    }

    /**
     * Sets the rasterizer to PerspectiveCorectRasterizer.
     */
    public void setPerpectiveRasterizer() {
        rasterizer = new PerspectiveCorrectRasterizer(shader);
    }

    /**
     * Initializes the shader.
     */
    public void initShader() {
        if (shader != null) {
            shader.init(screen.getScreenWidth(),
                    screen.getScreenHeight(),
                    projectVertices());
        }
    }
}
