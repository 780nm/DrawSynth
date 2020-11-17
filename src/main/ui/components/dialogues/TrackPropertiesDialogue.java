package ui.components.dialogues;

import synthesis.Instrument;
import ui.SequencerApp;
import ui.actions.ModelAction;

import javax.swing.*;

public class TrackPropertiesDialogue extends PropertiesDialogue  {

    private Instrument instrument;

    public TrackPropertiesDialogue(JComponent target, SequencerApp app, ModelAction action) {
        JComboBox<Instrument> instrCombo =
                new JComboBox<>(app.getSeq().getInstruments().stream().toArray(Instrument[]::new));
        instrCombo.addActionListener(e -> instrument = (Instrument)instrCombo.getSelectedItem());
        instrCombo.setSelectedIndex(0);

        addRow(panel, "Instrument", instrCombo);

        int choice = JOptionPane.showConfirmDialog(target, panel, "Add Note", JOptionPane.OK_CANCEL_OPTION);

        if (choice == 0) {
            action.processAction(new Object[]{instrument.getUuid()});
        }

    }

}
