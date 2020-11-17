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

public class MenuBar extends JMenuBar implements ActionListener {

    SequencerApp app;

    public MenuBar(SequencerApp app) {
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

        new FilePickerDialogue(this, save);
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

        new FilePickerDialogue(this, load);
    }

}