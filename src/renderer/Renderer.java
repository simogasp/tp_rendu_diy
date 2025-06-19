package renderer;

import java.io.IOException;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.algebra.Vector3;
import renderer.light.Lighting;
import renderer.rasterizer.NormalLayer;
import renderer.rasterizer.Rasterizer;
import renderer.shader.NormalMapShader;
import renderer.shader.DepthShader;
import renderer.shader.PainterShader;
import renderer.shader.Shader;
import renderer.shader.TextureShader;


/**
 * The Renderer class drives the rendering pipeline: read in a scene, projects
 * the vertices and rasterizes every faces / edges.
 * @author cdehais
 */
public final class Renderer {

    /** The scene. */
    private static Scene scene;
    /** The mesh. */
    private static Mesh mesh;
    /** The rasterizer. */
    private static Rasterizer rasterizer;
    /** The screen. */
    private static GraphicsWrapper screen;
    /** The shader. */
    private static Shader shader;
    /** The transformation. */
    private static Transformation xform;
    /** The lighting. */
    private static Lighting lighting;
    /** Whether lighting is enabled.. */
    private static boolean lightingEnabled;

    /** Wether the normals should be shown. */
    public static boolean renderNormals;
    /** The layer of the render of normals. */
    public static NormalLayer normalLayer;

    // Private constructor to prevent instantiation
    private Renderer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Initialize the renderer with the given scene file.
     * @param sceneFilename the scene file to load
     * @throws IOException if the scene file cannot be loaded
     */
    static void init(String sceneFilename) throws IOException {
        scene = new Scene(sceneFilename);
        mesh = new Mesh(scene.getMeshFileName());
        screen = new GraphicsWrapper(scene.getScreenW(), scene.getScreenH());
        screen.clearBuffer();
        normalLayer = new NormalLayer(screen);
        xform = new Transformation();
        xform.setLookAt(scene.getCameraPosition(),
                scene.getCameraLookAt(),
                scene.getCameraUp());
        xform.setProjection();
        xform.setCalibration(scene.getCameraFocal(),
                            scene.getScreenW(),
                            scene.getScreenH());
        //++ shader = new SimpleShader (screen);
        shader = new PainterShader(screen); //??
        shader = new DepthShader(screen); //??
        shader = new NormalMapShader(screen, xform);
        rasterizer = new Rasterizer(shader, normalLayer);
        // rasterizer = new PerspectiveCorrectRasterizer(shader);

        lighting = new Lighting();
        lighting.addAmbientLight(scene.getAmbientI());
        double[] lightCoord = scene.getSourceCoord();
        lighting.addPointLight(lightCoord[0],
                                lightCoord[1],
                                lightCoord[2],
                                scene.getSourceI());
    }

    /**
     * Projects the vertices of the mesh into the screen space.
     * @return an array of fragments
     */
    public static Fragment[] projectVertices() {
        final Vector[] vertices = mesh.getVertices();
        final Vector3[] normals = mesh.getNormals();
        final double[] colors = mesh.getColors();

        final Fragment[] fragments = new Fragment[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            final Vector pVertex = xform.projectPoint(vertices[i]);
            // Vector pNormal = xform.transformVector (normals[i]);
            final Vector3 pNormal = normals[i];

            final int x = (int) Math.round(pVertex.get(0));
            final int y = (int) Math.round(pVertex.get(1));
            fragments[i] = new Fragment(x, y);
            fragments[i].setDepth(pVertex.get(2));
            fragments[i].setNormal(pNormal);

            final double[] texCoords = mesh.getTextureCoordinates();
            if (texCoords != null) {
                fragments[i].setAttribute(7, texCoords[2 * i]);
                fragments[i].setAttribute(8, texCoords[2 * i + 1]);
            }

            if (!lightingEnabled) {
                fragments[i].setColor(colors[3 * i], colors[3 * i + 1], colors[3 * i + 2]);
            } else {
                final double[] color = new double[3];
                color[0] = colors[3 * i];
                color[1] = colors[3 * i + 1];
                color[2] = colors[3 * i + 2];
                final double material[] = scene.getMaterial();
                double[] litColor = lighting.applyLights(new Vector3(vertices[i]), pNormal, color,
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
    static void renderWireframe() {
        final Fragment[] fragments = projectVertices();

        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            for (int j = 0; j < 3; j++) {
                final Fragment v1 = fragments[faces[i + j]];
                final Fragment v2 = fragments[faces[i + ((j + 1) % 3)]];
                rasterizer.rasterizeEdge(v1, v2);
            }
        }

        if (renderNormals) {
            renderNormals(fragments);

        }
    }

    /**
     * Render the normals over on each vertices of the object.
     * @param fragments the fragment array of the vertices
     */
    public static void renderNormals(Fragment[] fragments) {
        final Fragment[] normals = projectNormalsDest();
        for (int i = 0; i < mesh.getNumVertices(); i++) {
            final Fragment v1 = fragments[i];
            final Fragment v2 = normals[i];
            rasterizer.rasterizeNormals(v1, v2);
        }
    }

    /**
     * Renders the solid of the mesh.
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    static void renderSolid() {
        final Fragment[] fragments = projectVertices();
        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            final Fragment v1 = fragments[faces[i]];
            final Fragment v2 = fragments[faces[i + 1]];
            final Fragment v3 = fragments[faces[i + 2]];

            rasterizer.rasterizeFace(v1, v2, v3);
        }

        if (renderNormals) {
            renderNormals(fragments);
        }
    }

    /**
     * Enables or disables lighting.
     * @param enabled true to enable lighting, false to disable it
     */
    public static void setLightingEnabled(final boolean enabled) {
        lightingEnabled = enabled;
    }

    public static void initShader() {
        Fragment[] fragments = projectVertices();
        for (Fragment fragment : fragments) {
            DepthShader.update(fragment.getDepth());
        }
    }

    /**
     * Enables or disables the normals render.
     * @param enabled true to enable normals render, false to disable it
     */
    public static void setRenderNormals(final boolean enabled) {
        renderNormals = enabled;
    }


    /**
     * Projects normals vectors into the screen space.
     * @return an array of fragments
     */
    public static Fragment[] projectNormalsDest() {
        final Vector[] vertices = mesh.getVertices();
        final Vector3[] normals = mesh.getNormals();

        final Fragment[] fragments = new Fragment[vertices.length];

        // get the smallest gap to determine the length of the normals
        double minX = vertices[0].get(0);
        double maxX = vertices[0].get(0);
        double minY = vertices[0].get(1);
        double maxY = vertices[0].get(1);
        double minZ = vertices[0].get(2);
        double maxZ = vertices[0].get(2);
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].get(0) < minX) {
                minX = vertices[i].get(0);
            } else if (vertices[i].get(0) > maxX) {
                maxX = vertices[i].get(0);
            }
            if (vertices[i].get(1) < minY) {
                minY = vertices[i].get(1);
            } else if (vertices[i].get(1) > maxY) {
                maxY = vertices[i].get(1);
            }
            if (vertices[i].get(2) < minZ) {
                minZ = vertices[i].get(2);
            } else if (vertices[i].get(2) > maxZ) {
                maxZ = vertices[i].get(2);
            }
        }
        // length is the minimum dimension of the bounding box divided by 10
        final double length = Math.min(maxX - minX, Math.min(maxY - minY, maxZ - minZ)) / 10;

        // computes to every vertices
        for (int i = 0; i < vertices.length; i++) {
            // Vector pNormal = xform.transformVector (normals[i]);

            // create the destination of the vector
            final double[] v = {
                vertices[i].get(0) + length * normals[i].get(0),
                vertices[i].get(1) + length * normals[i].get(1),
                vertices[i].get(2) + length * normals[i].get(2),
                1
            };
            final Vector normalVectorDest = xform.projectPoint(new Vector(v));

            // coordinate in screen
            final int x = (int) Math.round(normalVectorDest.get(0));
            final int y = (int) Math.round(normalVectorDest.get(1));

            fragments[i] = new Fragment(x, y);
            fragments[i].setDepth(normalVectorDest.get(2));

        }

        return fragments;
    }


    /**
     * Wait for a number of seconds.
     * @param sec the number of seconds to wait
     */
    public static void wait(final int sec) {
        try {
            final long millis = 1000;
            Thread.sleep(sec * millis);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * Main entry point of the renderer.
     * @param args the command line arguments
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    public static void main(final String[] args) {

        final int timeout = 3;

        if (args.length == 0) {
            System.out.println("usage: java Renderer <scene_file>");
            System.exit(-1);
        } else {
            try {
                init(args[0]);
            } catch (Exception e) {
                System.out.println("Problem initializing Renderer: " + e);
                e.printStackTrace();
                return;
            }
        }

        // get the nearest and the farest point for depth Shader
        initShader();

        // Uncomment to drawn normals
        setRenderNormals(true);

        // wireframe rendering
        renderWireframe();
        screen.swapBuffers();
        wait(timeout);

        // solid rendering, no lighting
        screen.clearBuffer(); //<??
        shader.reset();
        renderSolid();
        screen.swapBuffers();
        wait(timeout); //>??

        // solid rendering, with lighting
        screen.clearBuffer(); //<??
        shader.reset();
        setLightingEnabled(true);
        renderSolid();
        screen.swapBuffers();
        wait(timeout); //>??

        // solid rendering, with texture
        screen.clearBuffer(); //<??
        TextureShader texShader = new TextureShader(screen);
        texShader.setTexture("data/brick.jpg");
        shader = texShader;
        rasterizer.setShader(texShader);
        setLightingEnabled(true);
        renderSolid();
        screen.swapBuffers();
        wait(timeout); //>??

        // solid rendering, with texture combined with base color
        screen.clearBuffer(); //<??
        texShader.reset();
        texShader.setCombineWithBaseColor(true);
        shader = texShader;
        renderSolid();
        screen.swapBuffers();
        wait(timeout); //>??

        screen.destroy();
        System.exit(0);
    }
}
