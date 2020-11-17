package ui.components.dialogues;

import synthesis.AmplitudeModulator;
import synthesis.PitchModulator;
import ui.SequencerApp;
import ui.actions.ModelAction;

import javax.swing.*;
import java.util.ArrayList;

public class NotePropertiesDialogue extends PropertiesDialogue {

    private AmplitudeModulator ampMod;
    private PitchModulator pitchMod;
    private JSlider amp;
    private JSpinner pitch;
    private JSpinner duration;

    public NotePropertiesDialogue(JComponent target, SequencerApp app, ModelAction action) {
        ArrayList<AmplitudeModulator> ampMods = app.getSeq().getAmpMods();
        ArrayList<PitchModulator> pitchMods = app.getSeq().getPitchMods();

        if (ampMods.size() + pitchMods.size() < 2) {
            JOptionPane.showMessageDialog(target,
                    "Go make some Amplitude and Pitch modulators first and come back later");
            return;
        }

        addComboBoxes(panel, ampMods, pitchMods);
        addNumericalInputs(panel);

        int choice = JOptionPane.showConfirmDialog(target, panel, "Add Note", JOptionPane.OK_CANCEL_OPTION);

        if (choice == 0) {
            Object[] params = new Object[]{
                    amp.getValue() / (double)1000,
                    ampMod.getUuid(),
                    pitch.getValue(),
                    pitchMod.getUuid(),
                    (int)((double)duration.getValue() * SequencerApp.FORMAT.getFrameRate())};
            action.processAction(params);
        }

    }

    private void addComboBoxes(JPanel panel, ArrayList<AmplitudeModulator> ampMods,
                               ArrayList<PitchModulator> pitchMods) {

        JComboBox<AmplitudeModulator> ampModCombo =
                new JComboBox<>(ampMods.stream().toArray(AmplitudeModulator[]::new));
        JComboBox<PitchModulator> pitchModCombo =
                new JComboBox<>(pitchMods.stream().toArray(PitchModulator[]::new));

        ampModCombo.addActionListener(e -> ampMod = (AmplitudeModulator)ampModCombo.getSelectedItem());
        pitchModCombo.addActionListener(e -> pitchMod = (PitchModulator)pitchModCombo.getSelectedItem());

        ampModCombo.setSelectedIndex(0);
        pitchModCombo.setSelectedIndex(0);

        addRow(panel, "Amplitude Modulator", ampModCombo);
        addRow(panel, "Pitch Modulator", pitchModCombo);
    }

    private void addNumericalInputs(JPanel panel) {
        amp = new JSlider(0, 1000);
        pitch = new JSpinner(new SpinnerNumberModel(0., 0., Double.MAX_VALUE, 10));
        duration = new JSpinner(new SpinnerNumberModel(0., 0., Double.MAX_VALUE, .1));

        addRow(panel, "Amplitude (%)", amp);
        addRow(panel, "Pitch (hz)", pitch);
        addRow(panel, "Duration (s)", duration);
    }

}
