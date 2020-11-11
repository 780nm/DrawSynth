package ui;

import exceptions.GeneratorException;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuBar extends JMenuBar implements ActionListener {

    private static final String FILE_EXT = "json";
    private static final String FILE_ROOT = "./data/";

    SequencerApp app;

    MenuBar(SequencerApp app) {
        super();
        this.app = app;

        add(new ToolbarButton("Save", "save", this));
        add(new ToolbarButton("Load", "load", this));
        add(new ToolbarButton("Play", "play", this));
    }

    private static class ToolbarButton extends JButton {
        public ToolbarButton(String text, String command, ActionListener listener) {
            super(text);
            setSize(60,30);
            setEnabled(true);
            setActionCommand(command);
            addActionListener(listener);
        }
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "save":
                processSave();
                return;
            case "load":
                processLoad();
                return;
            case "play":
                SwingUtilities.updateComponentTreeUI(this);
                app.play();
        }
    }

    private void processSave() {
        FileAction save = new FileAction() {
            @Override
            public String getActionName() {
                return "Save";
            }

            @Override
            public String getActionTitle() {
                return "Save Sequencer";
            }

            @Override
            public boolean process(String path) {
                try {
                    JsonWriter writer = new JsonWriter(path);
                    writer.write(app.getSeq());
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        };

        processFileAction(save);
    }

    private void processLoad() {
        FileAction load = new FileAction() {
            @Override
            public String getActionName() {
                return "Load";
            }

            @Override
            public String getActionTitle() {
                return "Load Sequencer";
            }

            @Override
            public boolean process(String path) {
                try {
                    JsonReader reader = new JsonReader(path);
                    app.setSeq(reader.getSequencer());
                    return true;
                } catch (GeneratorException | IOException | JSONException exception) {
                    return false;
                }
            }
        };

        processFileAction(load);
    }

    private void processFileAction(FileAction action) {
        JFileChooser fileChooser = new JFileChooser(FILE_ROOT);
        fileChooser.setDialogTitle(action.getActionTitle());
        fileChooser.setApproveButtonToolTipText(action.getActionName());

        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Sequencer Files", FILE_EXT));
        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setMinimumSize(new Dimension(800, 600));

        while (true) {
            if (fileChooser.showDialog(this, action.getActionName()) == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().getName().toLowerCase().endsWith("." + FILE_EXT)) {
                    if (action.process(fileChooser.getSelectedFile().getPath())) {
                        return;
                    } else {
                        JOptionPane.showMessageDialog(fileChooser, "Unable to process file.");
                    }
                } else {
                    JOptionPane.showMessageDialog(fileChooser, "Invalid Filename. Please try again");
                }
            } else {
                return;
            }
        }
    }

}