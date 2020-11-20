package ui.actions;

import model.Track;
import ui.SequencerApp;
import ui.components.dialogues.InsertNoteDialogue;
import ui.components.dialogues.TrackPropertiesDialogue;

import java.awt.event.ActionEvent;
import java.util.UUID;

import static persistence.PersistenceUtil.getElementWithID;

// Processes actions related to the Timeline UI element
public class TimelineActionManager extends ActionManager {

    // EFFECTS: creates a new action manager with the associated application model
    public TimelineActionManager(SequencerApp app) {
        super(app);
    }

    // MODIFIES: app
    // EFFECTS: Catches fired action events and modifies the application model accordingly
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
                break;
            case "remove":
                processRemoveNote(command);
        }
        app.reinitializeContent();
    }

    // REQUIRES: command[] is not malformed
    // MODIFIES: app
    // EFFECTS:  Adds a track to the sequencer as per input configuration
    private void processAddTrack() {
        ModelAction addTrackAction = new ModelAction() {
            @Override
            public String getActionName() {
                return "Add Track";
            }

            @Override
            public void processAction(Object[] params) {
                app.getSeq().addTrack((UUID)params[0]);
            }
        };

        new TrackPropertiesDialogue(target, app, addTrackAction);
    }

    // REQUIRES: command[] is not malformed
    // MODIFIES: app
    // EFFECTS:  Edits track properties as per input configuration
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

        new TrackPropertiesDialogue(target, app, addTrackAction);
    }

    // REQUIRES: command[] is not malformed
    // MODIFIES: app
    // EFFECTS:  Inserts given note into given track as per input configuration
    private void processInsertNote(String[] command) {
        new InsertNoteDialogue(target, app, UUID.fromString(command[1]));
    }

    // REQUIRES: command[] is not malformed
    // MODIFIES: app
    // EFFECTS:  Removes given note from given track as per input configuration
    private void processRemoveNote(String[] command) {
        Track track = getElementWithID(app.getSeq().getTracks(), UUID.fromString(command[1]));
        track.removeNote(Integer.valueOf(command[2]));
    }

}
