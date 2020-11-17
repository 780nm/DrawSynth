package ui.components.dialogues;

import model.Note;
import ui.SequencerApp;

import javax.swing.*;
import java.util.UUID;

public class InsertNoteDialogue extends PropertiesDialogue {

    private SequencerApp app;

    private JSpinner position;
    private Note note;

    public InsertNoteDialogue(SequencerApp app) {
        this.app = app;
        position = new JSpinner(new SpinnerNumberModel(0., 0., Double.MAX_VALUE, .1));

        JComboBox<Note> noteCombo = new JComboBox<>(app.getSeq().getNotes().stream().toArray(Note[]::new));
        noteCombo.addActionListener(e -> note = (Note)noteCombo.getSelectedItem());
        noteCombo.setSelectedIndex(0);

        addRow(panel, "Position (s)", position);
        addRow(panel, "Note", noteCombo);
    }

    public boolean tryInsert(JComponent target, UUID trackID) {
        int choice = JOptionPane.showConfirmDialog(target, panel, "Add Note", JOptionPane.OK_CANCEL_OPTION);

        if (choice == 0) {
            return app.getSeq().insertNote((int)((double)position.getValue() * SequencerApp.FORMAT.getFrameRate()),
                    note.getUuid(), trackID);
        }

        return false;
    }

}
