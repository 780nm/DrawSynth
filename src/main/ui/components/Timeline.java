package ui.components;

import model.Track;
import ui.SequencerApp;
import ui.actions.ModelAction;
import ui.components.dialogues.InsertNoteDialogue;
import ui.components.dialogues.TrackPropertiesDialogue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static persistence.PersistenceUtil.getElementWithID;

public class Timeline extends Panel {

    public Timeline(SequencerApp app) {
        super(app);

        addRow(this, "", "add", "Add", "Tracks:");
        addSep();
        addTracks();
    }

    private void addTracks() {
        ArrayList<Track> tracks = app.getSeq().getTracks();

        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            String uuid = track.getUuid().toString();

            Map<String,String> act = new TreeMap<>();
            act.put("Edit", "editTrack:" + uuid);
            act.put("Insert", "insert:" + uuid);

            addRow(this, String.valueOf(i), act, new JLabel("track"));
            addSep();
        }
    }

//    private void addNotes (Track track) {
//        for (Note notel track.getNotes()) {
//
//        }
//    }

    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(":");
        switch (command[0]) {
            case "add":
                processAddTrack();
                break;
            case "editTrack":
                processEditTrack(command);
                break;
            case "insert":
                processInsertNote(command);
        }
    }

    private void processAddTrack() {
        ModelAction addTrackAction = new ModelAction() {
            @Override
            public String getActionName() {
                return "Add Track";
            }

            @Override
            public void processAction(Object[] params) {
                app.getSeq().addTrack((UUID)params[0]);
                app.reinitializeContent();
            }
        };

        new TrackPropertiesDialogue(this, app, addTrackAction);
    }

    private void processEditTrack(String[] command) {
        ModelAction addTrackAction = new ModelAction() {
            @Override
            public String getActionName() {
                return "Add Track";
            }

            @Override
            public void processAction(Object[] params) {
                Track track = getElementWithID(app.getSeq().getTracks(), UUID.fromString(command[1]));
                track.setInstrument(getElementWithID(app.getSeq().getInstruments(), (UUID)params[0]));
            }
        };

        new TrackPropertiesDialogue(this, app, addTrackAction);
    }

    private void processInsertNote(String[] command) {
        InsertNoteDialogue dialogue = new InsertNoteDialogue(app);

        while (dialogue.tryInsert(this, UUID.fromString(command[1]))) {
            JOptionPane.showMessageDialog(this, "Error: note intersects with existing note");
        }
    }

}
