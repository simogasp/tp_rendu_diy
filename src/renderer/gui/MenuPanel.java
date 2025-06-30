package renderer.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Optional;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import renderer.controller.Renderer;
import renderer.controller.ShaderFactory;

public class MenuPanel extends JPanel {

    /**
     * This panel isn't double buffered.
     */
    private static final boolean IS_DOUBLE_BUFFERED = false;
    /**
     * Selected state.
     */
    private static final boolean SELECTED = true;
    /**
     * The path to the cube scene.
     */
    private static final String CUBE_ENDPOINT = "data/example0.scene";
    /**
     * The path to the rabbit.
     */
    private static final String RABBIT_ENDPOINT = "data/example1.scene";
    /**
     * The path to suzanne.
     */
    private static final String SUZANNE_ENDPOINT = "data/example2.scene";
    /**
     * The path to the textured wall.
     */
    private static final String TEXTURE_ENDPOINT = "data/example_textured.scene";
    /**
     * The shift of the button.
     */
    private static final int TAB_SIZE_PIXEL = 30;

    /**
     * The render panel of the app.
     */
    private final RenderPanel renderPanel;
    /**
     * the constraints to build the pane.
     */
    private final GridBagConstraints constraints;

    /**
     * The insets to display nice radio button.
     */
    private final Insets insetsRadio;
    /**
     * The insets to display nice checkbox button.
     */
    private final Insets insetsCheckBox;

    /**
     * The Group of mesh selection.
     */
    private final ButtonGroup meshGroup;
    /**
     * The cube mesh selection button.
     */
    private final JRadioButton cube;
    /**
     * The rabbit mesh selection button.
     */
    private final JRadioButton rabbit;
    /**
     * The suzanne mesh selection button.
     */
    private final JRadioButton suzanne;
    /**
     * The wall texture selection button.
     */
    private final JRadioButton texture;
    /**
     * The file name input text field.
     */
    private final JTextField filenameTextField;

    /**
     * The shader combo box.
     */
    private final JComboBox<String> shaderComboBox;

    /**
     * The draw wire frame check box.
     */
    private final JCheckBox drawWireframeCheckBox;
    /**
     * The draw solid check box.
     */
    private final JCheckBox drawSolidCheckBox;
    /**
     * The rasterizer group button.
     */
    private final ButtonGroup rasterizerGroup;
    /**
     * The simple rasterizer button.
     */
    private final JRadioButton simpleRasterizer;
    /**
     * The perspective rasterizer button.
     */
    private final JRadioButton persperctiveRasterizer;

    /**
     * The draw normal option check box.
     */
    private final JCheckBox drawNormalCheckBox;
    /**
     * The lighting option check box.
     */
    private final JCheckBox lightingCheckBox;

    // ===================================================================================
    // controller part
    /**
     * The renderer used to make a render.
     */
    private final Renderer render;

    /**
     * Creates a MenuPanel from a RenderPanel.
     *
     * @param renderPanel the render panel
     */
    public MenuPanel(final RenderPanel renderPanel) {
        // set up the panel
        super(IS_DOUBLE_BUFFERED);

        // set up the renderer
        Renderer tmp;
        try {
            tmp = new Renderer();
        } catch (IOException e) {
            // should not be reach
            e.printStackTrace();
            tmp = null;
            ShaderFactory.init();
        }
        render = tmp;

        // set up the renderPanel
        this.renderPanel = renderPanel;

        // add a grid bag layout
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // set up the button insets

        // radio insets
        insetsRadio = new JButton().getInsets();
        // create a tab
        insetsRadio.left = TAB_SIZE_PIXEL;

        // checkbox insets
        insetsCheckBox = new JCheckBox().getInsets();
        // create a tab
        insetsCheckBox.left = TAB_SIZE_PIXEL;

        // fill the panel
        // add a title
        constraints.gridx = 0;
        constraints.gridy++;
        add(new JLabel("MENU"), constraints);

        // mesh radio
        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Filename"), constraints);
        meshGroup = new ButtonGroup();
        cube = new JRadioButton("Cube");
        rabbit = new JRadioButton("Rabbit");
        suzanne = new JRadioButton("Suzanne");
        texture = new JRadioButton("Texture");

        filenameTextField = new JTextField();
        // set up the buttons
        meshRadioConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Shader"), constraints);

        // shader ComboBox
        shaderComboBox = new JComboBox<>(ShaderFactory.getShaderSetAsStringArray());
        shaderComboBoxConfiguration();
        
        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Render"), constraints);

        // check box to draw the normals
        drawWireframeCheckBox = new JCheckBox("Draw wireframe");

        // check box to enable the lighting
        drawSolidCheckBox = new JCheckBox("Draw solid");

        // set up the buttons
        renderConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Rasterizer"), constraints);

        rasterizerGroup = new ButtonGroup();
        simpleRasterizer = new JRadioButton("Rasterizer");
        persperctiveRasterizer = new JRadioButton("Perspective Rasterizer");
        // set up the buttons
        rasterizerConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Option"), constraints);

        // check box to draw the normals
        drawNormalCheckBox = new JCheckBox("Draw normals");

        // check box to enable the lighting
        lightingCheckBox = new JCheckBox("lighting");

        // set up the buttons
        optionConfiguration();

        // add a update button (useless normally)
        constraints.gridy++;
        final JButton but = new JButton("Render");
        add(but, constraints);
        but.addActionListener(e -> {
            updateRender();
        });

        // start configuration
        setConfiguration();
    }

    private void shaderComboBoxConfiguration() {
        constraints.gridy++;
        shaderComboBox.addActionListener(e -> {
            final String shaderSelected = (String) shaderComboBox.getSelectedItem();
            if (!render.setShader(shaderSelected)) {
                System.out.println("ERROR");
                JOptionPane.showMessageDialog(filenameTextField,
                        "Error : creating the shader using the Factory.",
                        "Shader creation failed",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            updateRender();
        });
        add(shaderComboBox, constraints);
    }

    /**
     * Set up the mesh button group.
     */
    private void meshRadioConfiguration() {
        // Radio mesh

        // cube radio
        cube.setMargin(insetsRadio);
        constraints.gridy++;
        cube.addItemListener(e -> {
            if (!cube.isSelected()) {
                return;
            }
            try {
                render.setScene(CUBE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(cube, constraints);
        meshGroup.add(cube);

        // rabbit radio
        rabbit.setMargin(insetsRadio);
        constraints.gridy++;
        rabbit.addItemListener(e -> {
            if (!rabbit.isSelected()) {
                return;
            }
            try {
                render.setScene(RABBIT_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(rabbit, constraints);
        meshGroup.add(rabbit);

        // suzanne radio
        suzanne.setMargin(insetsRadio);
        constraints.gridy++;
        suzanne.addItemListener(e -> {
            if (!suzanne.isSelected()) {
                return;
            }
            try {
                render.setScene(SUZANNE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(suzanne, constraints);
        meshGroup.add(suzanne);

        // texture radio
        texture.setMargin(insetsRadio);
        constraints.gridy++;
        texture.addItemListener(e -> {
            if (!texture.isSelected()) {
                return;
            }
            try {
                render.setScene(TEXTURE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(texture, constraints);
        meshGroup.add(texture);

        // another filename
        constraints.gridy++;
        add(new JLabel("With another scene file"), constraints);
        constraints.gridy++;
        filenameTextField.addActionListener(e -> {
            try {
                render.setScene("data/" + filenameTextField.getText());
            } catch (final IOException e1) {
                JOptionPane.showMessageDialog(filenameTextField,
                        e1.getMessage(),
                        e1.getClass().getSimpleName(),
                        JOptionPane.ERROR_MESSAGE);
            }
            updateRender();
        });
        add(filenameTextField, constraints);
    }


    /**
     * Set up the render button group.
     */
    private void renderConfiguration() {
        // remove the border on the component
        drawWireframeCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(drawWireframeCheckBox, constraints);

        // remove the border on the component
        drawSolidCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(drawSolidCheckBox, constraints);

        // add the interdependant Item listener
        drawWireframeCheckBox.addItemListener(e -> {
            render.setWiredRendered(drawWireframeCheckBox.isSelected());
            updateRender();
        });

        drawSolidCheckBox.addItemListener(e -> {
            render.setSolidRendered(drawSolidCheckBox.isSelected());
            updateRender();
        });

    }

    /**
     * Set up the rasterizer button group.
     */
    private void rasterizerConfiguration() {

        // rasterizer
        simpleRasterizer.setMargin(insetsRadio);
        constraints.gridy++;
        simpleRasterizer.addItemListener(e -> {
            if (!simpleRasterizer.isSelected()) {
                return;
            }
            render.setRasterizer();
            updateRender();
        });
        add(simpleRasterizer, constraints);
        rasterizerGroup.add(simpleRasterizer);

        // rasterizer
        persperctiveRasterizer.setMargin(insetsRadio);
        constraints.gridy++;
        persperctiveRasterizer.addItemListener(e -> {
            if (!persperctiveRasterizer.isSelected()) {
                return;
            }
            render.setPerpectiveCorrectRasterizer();
            updateRender();
        });
        add(persperctiveRasterizer, constraints);
        rasterizerGroup.add(persperctiveRasterizer);

    }

    /**
     * Set up the option button group.
     */
    private void optionConfiguration() {
        drawNormalCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                render.setNormalsRendered(drawNormalCheckBox.isSelected());
                updateRender();
            }
        });

        // remove the border on the component
        drawNormalCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(drawNormalCheckBox, constraints);

        lightingCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                render.setLightingEnabled(lightingCheckBox.isSelected());
                updateRender();
            }
        });
        // remove the border on the component
        lightingCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(lightingCheckBox, constraints);

    }

    /**
     * Set up the selected button and make a first render.
     */
    private void setConfiguration() {
        // set the start configuration
        simpleRasterizer.setSelected(SELECTED);
        cube.setSelected(SELECTED);
        shaderComboBox.setSelectedIndex(0);
        drawWireframeCheckBox.setSelected(SELECTED);
    }

    /**
     * Update the render.
     */
    private void updateRender() {
        renderPanel.setImage(render.render());    }
}
