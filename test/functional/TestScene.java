import org.junit.Test;

import renderer.algebra.Vector;
import renderer.model.Scene;


/**
 * Test class from the Scene class.
 * 
 * @author: cdehais
 */
public class TestScene {

    @Test
    public void test() throws Exception {

        Scene scene = new Scene("data/example1.scene");
        String mesh_filename = scene.getMeshFileName();
        Vector cameraPosition = scene.getCameraPosition();
        Vector cameraLookAt = scene.getCameraLookAt();
        Vector cameraUp = scene.getCameraUp();
        // double cameraXLimit = scene.getCameraXLimit ();
        double cameraFocal = scene.getCameraFocal();
        int screenW = scene.getScreenW();
        int screenH = scene.getScreenH();
        double ambientI = scene.getAmbientI();
        double sourceI = scene.getSourceI();
        double[] sourceCoord = scene.getSourceCoord();
        double[] material = scene.getMaterial();

        System.out.println("# Test output");
        System.out.println(mesh_filename);

        System.out.println(cameraPosition);
        System.out.println(cameraLookAt);
        System.out.println(cameraUp);

        // System.out.println ("Camera X Limit = " + cameraXLimit);
        System.out.println("Focal = " + cameraFocal);

        System.out.println("Screen: " + screenW + " x " + screenH);

        System.out.println("Light ambient: " + ambientI);
        System.out.println("Light ambient: " + sourceI);

        System.out.println(sourceCoord[0] + " " +
                sourceCoord[1] + " " +
                sourceCoord[2] + " " +
                sourceCoord[3]);

        System.out.println(material[0] + " " +
                material[1] + " " +
                material[2] + " " +
                material[3]);

    }

}
