package renderer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;


public class GUIApp extends JFrame {

    private static final int DEFAULT_PIXEL_SIZE = 1;


    public static void main(String[] args) {
        JFrame a = new GUIApp(1000, 1000);
    }

    /**
     * The panel for the render.
     */
    private final RenderPanel renderPanel;

    /**
     * The panel for the menu to choose shader, draw normal, wire, vertices, solid, textured.
     */
    private final MenuPanel menuPanel;

    /**
     * Create a GUI for a render of size (width * pixelSize + menuSize, height * pixelSize).
     * @param width the width of the screen of the camera
     * @param height the height of pixel on the screen of the camera
     * @param pixelSize the number of pixel on the GUI by pixel of the camera
     */
    public GUIApp(final int width, final int height, final int pixelSize) {
        super("Simple Inverse Rasterization Renderer (TSI)");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a grid bag layout
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;

        renderPanel = new RenderPanel(width, height, pixelSize);

        menuPanel = new MenuPanel(renderPanel);

        add(renderPanel, constraints);
        add(menuPanel, constraints);

        // commit the adds and show the app
        pack();
        setVisible(true);
    }



    /**
     * Create a GUIApp for a render of size (width + menuSize, height).
     * @param width the width of the screen of the camera
     * @param height the height of pixel on the screen of the camera
     */
    public GUIApp(final int width, final int height) {
        this(width, height, DEFAULT_PIXEL_SIZE);
    }
}
