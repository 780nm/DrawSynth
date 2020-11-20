package ui.components;

import exceptions.GeneratorException;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.SequencerApp;
import ui.actions.FileAction;
import ui.components.dialogues.FilePickerDialogue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// Menu Bar UI element
public class MenuBar extends JMenuBar implements ActionListener {

    SequencerApp app;

    // MODIFIES: app
    // EFFECTS: generates a new menu-bar given an application
    public MenuBar(SequencerApp app) {
        super();
        this.app = app;

        add(toolbarButton("Save", "save"));
        add(toolbarButton("Load", "load"));
        add(toolbarButton("Play", "play"));
    }

    // EFFECTS: Create a new button with the given action and return it
    public JButton toolbarButton(String text, String command) {
        JButton button = new JButton(text);
        button.setSize(60,30);
        button.setEnabled(true);
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

    // MODIFIES: app
    // EFFECTS: Adds elements to the application as per input command
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

    // MODIFIES: file-system
    // EFFECTS: Saves the current state of the application to disk
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

        new FilePickerDialogue(this, save);
    }

    // MODIFIES: app
    // EFFECTS: Reloads the application from disk
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

        new FilePickerDialogue(this, load);
    }

}