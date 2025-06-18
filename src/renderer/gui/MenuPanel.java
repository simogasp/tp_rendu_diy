package renderer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import renderer.DepthShader;
import renderer.Renderer;
import renderer.shader.NormalMapShader;
import renderer.shader.PainterShader;
import renderer.shader.SimpleShader;
import renderer.shader.TextureShader;


public class MenuPanel extends JPanel {

    private static final boolean IS_DOUBLE_BUFFERED = false;
    private static final boolean SELECTED = true;
    private static final String CUBE_ENDPOINT = "data/example0.scene";
    private static final String RABBIT_ENDPOINT = "data/example1.scene";
    private static final String SUZANNE_ENDPOINT = "data/example2.scene";



    public MenuPanel(final RenderPanel renderPanel) {
        super(IS_DOUBLE_BUFFERED);

        // add a grid bag layout
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;


        // radio insets
        final Insets insets = new JButton().getInsets();
        // create a tab
        insets.left = 30;

        // checkbox insets
        final Insets insetsCheckBox = new JCheckBox().getInsets();
        // create a tab
        insetsCheckBox.left = 30;

        // textfield insets
        final Insets insetsTextField = new JTextField().getInsets();
        // create a tab
        insetsTextField.left = 30;

        int itemNumber = 0;

        constraints.gridx = 0;
        constraints.gridy = itemNumber++;
        add(new JLabel("MENU"), constraints);

        constraints.gridy = itemNumber++;
        add(new JLabel("Filename"), constraints);

        // Radio mesh
        final ButtonGroup meshGroup = new ButtonGroup();

        final JRadioButton cube = new JRadioButton("Cube");
        cube.setMargin(insets);
        constraints.gridy = itemNumber++;
        constraints.ipadx = 20;
        cube.addItemListener(e -> {
            if (!cube.isSelected()) {
                return;
            }
            try {
                Renderer.init(CUBE_ENDPOINT);
            } catch (IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
        });
        add(cube, constraints);
        meshGroup.add(cube);

        final JRadioButton rabbit = new JRadioButton("Rabbit");
        rabbit.setMargin(insets);
        constraints.gridy = itemNumber++;
        rabbit.addActionListener(e -> {
            if (!rabbit.isSelected()) {
                return;
            }
            try {
                Renderer.init(RABBIT_ENDPOINT);
            } catch (IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
        });
        add(rabbit, constraints);
        meshGroup.add(rabbit);
        
        final JRadioButton suzanne = new JRadioButton("Suzanne");
        suzanne.setMargin(insets);
        constraints.gridy = itemNumber++;
        suzanne.addActionListener(e -> {
            if (!suzanne.isSelected()) {
                return;
            }
            try {
                Renderer.init(SUZANNE_ENDPOINT);
            } catch (IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
        });
        add(suzanne, constraints);
        meshGroup.add(suzanne);

        
        // another filename
        constraints.gridy = itemNumber++;
        add(new JLabel("With another file"), constraints);
        final JTextField filenameTextField = new JTextField();
        constraints.gridy = itemNumber++;
        filenameTextField.addActionListener(e -> {
            try {
                Renderer.init("data/" + filenameTextField.getText());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(filenameTextField, e1.getMessage(), e1.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
            }
        });
        add(filenameTextField, constraints);

        constraints.gridy = itemNumber++;
        add(new JLabel("Choix du shader"), constraints);
        
        // Radio
        final ButtonGroup shaderGroup = new ButtonGroup();
        
        final JRadioButton simpleShader = new JRadioButton("Simple");
        simpleShader.setMargin(insets);
        constraints.gridy = itemNumber++;
        constraints.ipadx = 20;
        simpleShader.addItemListener(e -> {
            if(!simpleShader.isSelected()){
                return;
            }
            Renderer.setShader(new SimpleShader(renderPanel));
            System.out.println("The shader is now SimpleShader");
        });
        add(simpleShader, constraints);
        shaderGroup.add(simpleShader);

        final JRadioButton painterShader = new JRadioButton("Painter");
        painterShader.setMargin(insets);
        constraints.gridy = itemNumber++;
        painterShader.addActionListener(e -> {
            if (!painterShader.isSelected()) {
                return;
            }
            Renderer.setShader(new PainterShader(renderPanel));
            System.out.println("The shader is now PainterShader");

        });
        add(painterShader, constraints);
        shaderGroup.add(painterShader);
        
        final JRadioButton textureShader = new JRadioButton("Texture");
        textureShader.setMargin(insets);
        constraints.gridy = itemNumber++;
        textureShader.addActionListener(e -> {
            if (!textureShader.isSelected()) {
                return;
            }
            Renderer.setShader(new TextureShader(renderPanel));
            System.out.println("The shader is now TextureShader");

        });
        add(textureShader, constraints);
        shaderGroup.add(textureShader);
        
        final JRadioButton depthShader = new JRadioButton("Depth");
        depthShader.setMargin(insets);
        constraints.gridy = itemNumber++;
        depthShader.addActionListener(e -> {
            if (!depthShader.isSelected()) {
                return;
            }
            Renderer.setShader(new DepthShader(renderPanel));
            System.out.println("The shader is now DepthShader");

        });
        add(depthShader, constraints);
        shaderGroup.add(depthShader);
        
        final JRadioButton normalShader = new JRadioButton("Normal Map");
        normalShader.setMargin(insets);
        constraints.gridy = itemNumber++;
        normalShader.addActionListener(e -> {
            Renderer.setShader(new NormalMapShader(renderPanel, Renderer.getXform()));
            System.out.println("The shader is now NormalShader");
        });
        add(normalShader, constraints);
        shaderGroup.add(normalShader);


        constraints.gridy = itemNumber++;
        add(new JLabel("Render"), constraints);

        // check box to draw the normals
        final JCheckBox drawWireframeCheckBox = new JCheckBox("Draw wireframe");
        drawWireframeCheckBox.addItemListener(e -> {
            Renderer.renderWireframe();
            renderPanel.swapBuffers();
        });

        // remove the border on the component
        drawWireframeCheckBox.setMargin(insetsCheckBox);
        constraints.gridy = itemNumber++;
        add(drawWireframeCheckBox, constraints);

        // check box to enable the lighting
        final JCheckBox drawSolidCheckBox = new JCheckBox("Draw solid");
        drawSolidCheckBox.addItemListener(e -> {
            Renderer.renderSolid();
            renderPanel.swapBuffers();
        });
        // remove the border on the component
        drawSolidCheckBox.setMargin(insetsCheckBox);
        constraints.gridy = itemNumber++;
        add(drawSolidCheckBox, constraints);


        constraints.gridy = itemNumber++;
        add(new JLabel("Option"), constraints);

        // check box to draw the normals
        final JCheckBox drawNormalCheckBox = new JCheckBox("Draw normals");
        drawNormalCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                final boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
                System.out.println(isChecked? "Should draw normal" : "should not draw normals");
            }
        });

        // remove the border on the component
        drawNormalCheckBox.setMargin(insetsCheckBox);
        constraints.gridy = itemNumber++;
        add(drawNormalCheckBox, constraints);

        // check box to enable the lighting
        final JCheckBox lightingCheckBox = new JCheckBox("lighting");
        lightingCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Renderer.setLightingEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        // remove the border on the component
        lightingCheckBox.setMargin(insetsCheckBox);
        constraints.gridy = itemNumber++;
        add(lightingCheckBox, constraints);
        
        
        constraints.gridy = itemNumber++;
        final JButton but = new JButton("Repaint");
        add(but, constraints);
        but.addActionListener(e -> {
            renderPanel.repaint();
        });

        // start configuration
        System.out.println("START");
        Renderer.setShader(new SimpleShader(renderPanel));
        simpleShader.setSelected(SELECTED);
        cube.setSelected(SELECTED);
        drawWireframeCheckBox.setSelected(SELECTED);
        System.out.println("END");
    }
}
