/*
    No AI was used for this project (hence the jank code), just some good old Google and way too much Monster.
    I'm proud of this project, again it has quite some jank implementations
    and code but updates will come soon (I hope).
    - CheesyMoeez
*/
package com.github.cheesymoeez.gui;

import com.github.cheesymoeez.model.ImageToAVIFConverterModel;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

public class ImageToAVIFConverterGUI extends JFrame {

    // choose image section
    private JPanel chooseImagePanel;
    private JButton chooseImageButton;

    private JPanel chooseImageLabelPanel;
    private JLabel chooseImageLabel;

    // choose quality section
    private JPanel chooseQualityPanel;
    private JLabel chooseQualityLabel;
    private JSpinner chooseQualitySpinner;

    // better compression mode section
    private JPanel betterCompressPanel;
    private JCheckBox betterCompressCheckBox;

    // better compression & choose quality section
    private JPanel chooseQualityBetterCompressPanel;

    // shoose save directory
    private JButton chooseSaveDirectoryButton;
    private JFileChooser chooseSaveLocationFileChooser;

    // convert image section
    private JPanel convertImagePanel;
    private JButton convertImageButton;

    private JPanel convertImageLabelPanel;
    private JLabel convertImageLabel;

    private JFileChooser chooseImageFileChooser = new JFileChooser();

    private File file = new File("Please select an image");

    public ImageToAVIFConverterGUI() {

        /* I'm using FlatLaf (I had to convert this project from Ant to Maven,
        I have no idea how to use it for Ant but Maven seems better)
         */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        setTitle("SimpleImageToAVIFConverter");

        setLayout(new GridLayout(3, 1));
        setSize(300, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(3);

        // choose image section
        chooseImagePanel = new JPanel();
        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenImage open = new OpenImage();
                open.start();
            }
        });
        chooseImageLabelPanel = new JPanel();
        chooseImageLabel = new JLabel("No image selected");
        chooseImageLabelPanel.add(chooseImageLabel);
        chooseImagePanel.add(chooseImageButton);
        chooseImagePanel.add(chooseImageLabelPanel);
        add(chooseImagePanel);

        // choose quality section
        chooseQualityPanel = new JPanel();
        chooseQualityLabel = new JLabel("Quality:");
        chooseQualitySpinner = new JSpinner();
        SpinnerModel chooseQualityJSpinnerModel = new SpinnerNumberModel(100, 1, 100, 1);
        chooseQualitySpinner.setModel(chooseQualityJSpinnerModel);
        chooseQualityPanel.add(chooseQualityLabel);
        chooseQualityPanel.add(chooseQualitySpinner);

        // better compression mode section
        betterCompressPanel = new JPanel();
        betterCompressCheckBox = new JCheckBox("Better Compression");
        betterCompressPanel.add(betterCompressCheckBox);

        // better compression & choose quality section
        chooseQualityBetterCompressPanel = new JPanel(new GridLayout(1, 2));
        chooseQualityBetterCompressPanel.add(chooseQualityPanel);
        chooseQualityBetterCompressPanel.add(betterCompressPanel);
        add(chooseQualityBetterCompressPanel);

        // choose save directory
        chooseSaveDirectoryButton = new JButton("...");
        chooseSaveDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SaveImage save = new SaveImage();
                save.start();
            }
        });

        // convert image section
        convertImagePanel = new JPanel(new FlowLayout());
        convertImageButton = new JButton("Convert");
        convertImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ConvertImage convert = new ConvertImage();
                convert.start();
            }
        }
        );

        convertImageLabel = new JLabel("");
        convertImagePanel.add(chooseSaveDirectoryButton);
        convertImagePanel.add(convertImageButton);
        convertImagePanel.add(convertImageLabel);

        chooseImagePanel.setBackground(new Color(209, 209, 209));
        add(convertImagePanel);

        setVisible(true);
    }

    public class OpenImage extends Thread {

        @Override
        public void run() {
            chooseImageFileChooser.showDialog(rootPane, "Open");
            if (chooseImageFileChooser.getSelectedFile() == null) {
                chooseImageFileChooser.setSelectedFile(file);
                chooseImageLabel.setText("Please select an image.");
            } else {
                chooseImageLabel.setText(chooseImageFileChooser
                        .getSelectedFile().getName());
            }
        }
    }

    public class SaveImage extends Thread {

        // thread that executes when clicking the "..." button (choose image save location)
        @Override
        public void run() {

            String saveLocation = null;

            chooseSaveLocationFileChooser = new JFileChooser(saveLocation);
            int chooseSaveOptionChosen = chooseSaveLocationFileChooser.showSaveDialog(rootPane);

            if (chooseSaveOptionChosen == JFileChooser.CANCEL_OPTION) {

                System.out.println(chooseSaveLocationFileChooser.getCurrentDirectory());
                chooseSaveLocationFileChooser.setSelectedFile(file);
                chooseImageLabel.setText("Please select a directory.");

            } else if (!(chooseSaveLocationFileChooser.getSelectedFile().getName().toLowerCase().endsWith(".avif"))) {

                File fileNameWithExtension = new File(chooseSaveLocationFileChooser.getSelectedFile().getAbsolutePath().concat(".avif"));
                chooseSaveLocationFileChooser.setSelectedFile(fileNameWithExtension);

            }
            convertImageLabel.setText(chooseSaveLocationFileChooser
                    .getCurrentDirectory().getAbsolutePath());
        }
    }

    // thread that executes when clicking the "Convert" button
    public class ConvertImage extends Thread {

        @Override
        public void run() {
            // what on earth is happening here, I couldn't give you the answer...
            String imageQuality = ((int) chooseQualitySpinner.getValue()) + "";
            String avifencPath = "C:\\Users\\Moeez Shahzad\\Documents\\NetBeansProjects\\ImageToAVIFConverter\\avifenc.exe";

            try {
                ProcessBuilder convertImageProcessBuilder;
                if (betterCompressCheckBox.isSelected()) {
                    System.out.println("Path of chosen image: " + chooseImageFileChooser.getSelectedFile().getAbsolutePath());
                    System.out.println("Path of save directory: " + chooseSaveLocationFileChooser.getSelectedFile().getAbsolutePath());
                    convertImageProcessBuilder = new ProcessBuilder(
                            avifencPath,
                            "-q", imageQuality,
                            "-s", "10",
                            chooseImageFileChooser.getSelectedFile().getAbsolutePath(),
                            chooseSaveLocationFileChooser.getSelectedFile().getAbsolutePath()
                    );
                } else {
                    convertImageProcessBuilder = new ProcessBuilder(
                            avifencPath,
                            "-q", imageQuality,
                            "-s", "4",
                            chooseImageFileChooser.getSelectedFile().getAbsolutePath(),
                            chooseSaveLocationFileChooser.getSelectedFile().getAbsolutePath()
                    );
                }
                convertImageProcessBuilder.inheritIO();
                Process process = convertImageProcessBuilder.start();
                process.waitFor();
            } catch (Exception ex) {
            }
        }
    }
}
