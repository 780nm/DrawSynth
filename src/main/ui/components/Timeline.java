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

public class Timeline extends Panel {

    public Timeline(SequencerApp app) {
        super(app, new TimelineActionManager(app));

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

            addRow(this, String.valueOf(i), act, notesInfo(track));
            addSep();
        }
    }

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

    protected void addHSep(JComponent target) {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        Dimension size = new Dimension(
                separator.getPreferredSize().width,
                separator.getMaximumSize().height);
        separator.setMaximumSize(size);
        target.add(separator);
    }

}
