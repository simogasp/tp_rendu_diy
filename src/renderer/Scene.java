package renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import renderer.algebra.Vector3;

/**
 * Class that describes a simple 3D Scene:
 * * the Mesh objects composing the scene
 * * the World-to-Camera transformation
 * * the Lights and their parameters
 * This description is meant to be read form a scene description file (.scene
 * extension).
 *
 * @author cdehais based on smondet, gmorin
 */

public class Scene {

    private static String nextLine(BufferedReader in) throws IOException {
        String r = in.readLine();

        while (r.matches("(\\s*#.*)|(\\s*$)")) {
            r = in.readLine();
        }
        return r;
    }

    String meshFilename;
    Vector3 cameraPosition = new Vector3("cam_pos");
    Vector3 cameraLookAt = new Vector3("cam_lookat");
    Vector3 cameraUp = new Vector3("cam_up");
    double cameraFocal;
    int screenW;
    int screenH;
    double ambientI;
    double sourceI;
    double[] sourceCoord = new double[4];
    double[] material = new double[4];

    /**
     * Creates a new Scene object by reading in a scene description file.
     *
     * @param filename path to the scene description file.
     * @throws IOException if the file is not a valid scene description file.
     */
    public Scene(String filename) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(filename));

        meshFilename = nextLine(in);

        String r = nextLine(in);
        String[] sar = r.split("\\s+");
        cameraPosition.set(Double.parseDouble(sar[0]),
                Double.parseDouble(sar[1]),
                Double.parseDouble(sar[2]));

        r = nextLine(in);
        sar = r.split("\\s+");
        cameraLookAt.set(Double.parseDouble(sar[0]),
                Double.parseDouble(sar[1]),
                Double.parseDouble(sar[2]));

        r = nextLine(in);
        sar = r.split("\\s+");
        cameraUp.set(Double.parseDouble(sar[0]),
                Double.parseDouble(sar[1]),
                Double.parseDouble(sar[2]));

        r = nextLine(in);
        cameraFocal = Double.parseDouble(r);

        r = nextLine(in);
        sar = r.split("\\s+");
        screenW = Integer.parseInt(sar[0]);
        screenH = Integer.parseInt(sar[1]);

        r = nextLine(in);
        ambientI = Double.parseDouble(r);

        r = nextLine(in);
        sar = r.split("\\s+");
        for (int i = 0; i < sourceCoord.length; i++) {
            sourceCoord[i] = Double.parseDouble(sar[i]);
        }
        sourceI = Double.parseDouble(sar[3]);

        r = nextLine(in);
        sar = r.split("\\s+");
        for (int i = 0; i < material.length; i++) {
            material[i] = Double.parseDouble(sar[i]);
        }
    }

    /**
     * Gets the name of the mesh file.
     * @return the name of the mesh file.
     */
    public String getMeshFileName() {
        return meshFilename;
    }

    /**
     * Gets the camera position.
     * @return the camera position.
     */
    public Vector3 getCameraPosition() {
        return cameraPosition;
    }

    /**
     * Gets the camera look at point.
     * @return the camera look at point.
     */
    public Vector3 getCameraLookAt() {
        return cameraLookAt;
    }

    /**
     * Gets the camera up vector.
     * @return the camera up vector.
     */
    public Vector3 getCameraUp() {
        return cameraUp;
    }

    /**
     * Gets the camera focal length.
     * @return the camera focal length.
     */
    public double getCameraFocal() {
        return cameraFocal;
    }

    /**
     * Gets the screen width.
     * @return the screen width.
     */
    public int getScreenW() {
        return screenW;
    }

    /**
     * Gets the screen height.
     * @return the screen height.
     */
    public int getScreenH() {
        return screenH;
    }

    /**
     * Gets the ambient intensity.
     * @return the ambient intensity.
     */
    public double getAmbientI() {
        return ambientI;
    }

    /**
     * Gets the source intensity.
     * @return the source intensity.
     */
    public double getSourceI() {
        return sourceI;
    }

    /**
     * Gets the source coordinates.
     * @return the source coordinates.
     */
    public double[] getSourceCoord() {
        return sourceCoord;
    }

    /**
     * Gets the material.
     * @return the material.
     */
    public double[] getMaterial() {
        return material;
    }
}
