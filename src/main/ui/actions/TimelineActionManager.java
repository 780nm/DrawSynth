package ui.actions;

import model.Track;
import ui.SequencerApp;
import ui.components.dialogues.InsertNoteDialogue;
import ui.components.dialogues.TrackPropertiesDialogue;

import java.awt.event.ActionEvent;
import java.util.UUID;

import static persistence.PersistenceUtil.getElementWithID;

public class TimelineActionManager extends ActionManager {

    public TimelineActionManager(SequencerApp app) {
        super(app);
    }

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

    private void processInsertNote(String[] command) {
        new InsertNoteDialogue(target, app, UUID.fromString(command[1]));
    }

    private void processRemoveNote(String[] command) {
        Track track = getElementWithID(app.getSeq().getTracks(), UUID.fromString(command[1]));
        track.removeNote(Integer.valueOf(command[2]));
    }

}
