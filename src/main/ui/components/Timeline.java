package ui.components;

import model.Note;
import model.Track;
import ui.SequencerApp;
import ui.actions.TimelineActionManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

// Palette UI Element, containing a list of tracks and facilitating their manipulation
public class Timeline extends Panel {

    // MODIFIES: app
    // EFFECTS: generates a new timeline panel given an application
    public Timeline(SequencerApp app) {
        super(app, new TimelineActionManager(app));

        addRow(this, "", "add", "Add", "Tracks:");
        addSep();
        addTracks();
    }

    // MODIFIES: this
    // EFFECTS: adds track information and buttons to the timeline
    private void addTracks() {
        ArrayList<Track> tracks = app.getSeq().getTracks();

        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            String uuid = track.getUuid().toString();

            Map<String,String> act = new TreeMap<>();
            act.put("Edit", "editTrack:" + uuid);
            act.put("Insert", "insert:" + uuid);

            addRow(this, String.valueOf(i), act, notesInfo(track));
            addSep();
        }
    }

    // EFFECTS: Returns a panel with the notes in a given track and their associated menu buttons
    private JPanel notesInfo(Track track) {
        JPanel noteList = new JPanel();
        noteList.setLayout(new BoxLayout(noteList, BoxLayout.X_AXIS));

        for (Map.Entry<Integer,Note> note : track.getNotes().entrySet()) {
            JPanel notePanel = new JPanel();
            notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.Y_AXIS));

            double duration = note.getValue().getDuration() / SequencerApp.FORMAT.getFrameRate();
            double position = note.getKey() / SequencerApp.FORMAT.getFrameRate();

            notePanel.add(new JLabel("Dur (s): " +  String.format("%.2g", duration)));
            notePanel.add(new JLabel("Pos (s): " +  String.format("%.2g", position)));
            notePanel.add(editButton("Remove", "remove:" + track.getUuid() + ":" + note.getKey()));
            boundHeight(notePanel);

            addHSep(noteList);
            noteList.add(Box.createHorizontalStrut(5));
            noteList.add(notePanel);
            noteList.add(Box.createHorizontalStrut(5));
        }

        boundHeight(noteList);
        return noteList;
    }

    // MODIFIES: target
    // EFFECTS: adds a horizontal separator to the target
    protected void addHSep(JComponent target) {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        Dimension size = new Dimension(
                separator.getPreferredSize().width,
                separator.getMaximumSize().height);
        separator.setMaximumSize(size);
        target.add(separator);
    }

}
