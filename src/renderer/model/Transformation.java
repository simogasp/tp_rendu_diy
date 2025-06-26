package renderer.model;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;


/**
 * The Transformation class represents a transformation in 3D space.
 * author: cdehais
 */
public class Transformation {

    /**
     * The world to camera matrix.
     */
    private Matrix worldToCamera;
    /**
     * The projection matrix.
     */
    private Matrix projection;
    /**
     * The calibration matrix.
     */
    private Matrix calibration;

    /**
     * Creates a new Transformation object.
     */
    public Transformation() {
        worldToCamera = Matrix.createIdentity("W2C", 4);
        projection = new Matrix("P", 3, 4);
        calibration = Matrix.createIdentity("K", 3);
    }

    /**
     * Sets the lookAt transformation.
     * @param eye the eye position
     * @param lookAtPoint the point to look at
     * @param up the up vector
     */
    public void setLookAt(final Vector eye, final Vector lookAtPoint, final Vector up) {
        try {
            // compute rotation
            //++ // TODO
            Vector z = lookAtPoint.subtract(eye); //<!!
            z = z.normalize();

            Vector x = up.cross(z);
            x = x.normalize();
            final Vector y = z.cross(x);

            worldToCamera.set(0, 0, x.getX());
            worldToCamera.set(0, 1, x.getY());
            worldToCamera.set(0, 2, x.getZ());
            worldToCamera.set(1, 0, y.getX());
            worldToCamera.set(1, 1, y.getY());
            worldToCamera.set(1, 2, y.getZ());
            worldToCamera.set(2, 0, z.getX());
            worldToCamera.set(2, 1, z.getY());
            worldToCamera.set(2, 2, z.getZ()); //>!!

            // compute translation
            //++ // TODO
            final Vector mEye = eye.scale(-1.0); //<!!
            final Matrix m = worldToCamera.getSubMatrix(0, 0, 3, 3);

            final Vector t = m.multiply(mEye);
            System.out.println(m);
            System.out.println(mEye);

            worldToCamera.set(0, 3, t.get(0));
            worldToCamera.set(1, 3, t.get(1));
            worldToCamera.set(2, 3, t.get(2)); //>!!

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Modelview matrix:\n" + worldToCamera);
    }

    /**
     * Sets the projection matrix.
     */
    public void setProjection() {
        //++ // TODO
        projection.set(0, 0, 1.0); //<!!
        projection.set(1, 1, 1.0);
        projection.set(2, 2, 1.0); //>!!

        System.out.println("Projection matrix:\n" + projection);
    }

    /**
     * Sets the calibration matrix.
     * @param focal the focal length
     * @param width the width of the image
     * @param height the height of the image
     */
    public void setCalibration(double focal, double width, double height) {

        //++ // TODO
        calibration.set(0, 0, focal); //<!!
        calibration.set(1, 1, focal);
        calibration.set(0, 2, width / 2.0);
        calibration.set(1, 2, height / 2.0); //>!!

        System.out.println("Calibration matrix:\n" + calibration);
    }

    /**
     * Projects the given homogeneous, 4 dimensional point onto the screen.
     * The resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
     * is the depth of the point in the camera coordinate system.
     * @param p the point to project
     * @return the projected point
     * @throws SizeMismatchException if the size of the input vector is not 4
     */
    public Vector projectPoint(Vector p) throws SizeMismatchException {
        //++ // TODO
        final Vector pe = worldToCamera.multiply(p); //<!!
        final Vector ps = calibration.multiply(projection.multiply(pe));
        ps.set(0, ps.get(0) / ps.get(2));
        ps.set(1, ps.get(1) / ps.get(2)); //>!!
        return ps;
    }

    /**
     * Transform a vector from world to camera coordinates.
     * @param v the vector to transform
     * @return the transformed vector
     * @throws SizeMismatchException if the size of the input vector is not 3
     */
    public Vector transformVector(final Vector v) {
        // Doing nothing special here because there is no scaling
        final Matrix m = worldToCamera.getSubMatrix(0, 0, 3, 3);
        return m.multiply(v);
    }

}
