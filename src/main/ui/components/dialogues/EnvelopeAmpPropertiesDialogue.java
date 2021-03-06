package ui.components.dialogues;

import synthesis.EnvelopeAmplitude;

import javax.swing.*;

// UI for inputting envelope amplitude modulator properties
public class EnvelopeAmpPropertiesDialogue extends PropertiesDialogue {

    private JSlider attack;
    private JSlider decay;
    private JSlider balance;

    // MODIFIES: amp
    // EFFECTS: Create a new dialogue and mutate the given modulator on completion
    public EnvelopeAmpPropertiesDialogue(JComponent target, EnvelopeAmplitude amp) {
        addNumericalInputs(panel);

        int choice = JOptionPane.showConfirmDialog(target, panel,
                "Add Amplitude Modulator", JOptionPane.OK_CANCEL_OPTION);

        if (choice == 0) {
            amp.setAttack(attack.getValue() / 100.);
            amp.setDecay(1 - (decay.getValue() / 100.));
            amp.setBalance(balance.getValue() / 100.);
        }

    }

    // MODIFIES: this
    // EFFECTS: adds numerical input elements to panel
    private void addNumericalInputs(JPanel panel) {
        attack = new JSlider(0, 1000);
        decay = new JSlider(0, 1000);
        balance = new JSlider(-1000, 1000);

        setSliderModels();
        decay.setValue(1000);

        addRow(panel, "Attack (% Duration)", attack);
        addRow(panel, "Attack (% Duration)", decay);
        addRow(panel, "Balance (% Left/Right)", balance);
    }

    // MODIFIES: this
    // EFFECTS: adds slider elements to panel
    private void setSliderModels() {

        // Taken from https://stackoverflow.com/questions/31835586/set-jslider-limits
        attack.setModel(
                new DefaultBoundedRangeModel() {
                    @Override
                    public void setValue(int n) {
                        if (n > decay.getValue()) {
                            n = decay.getValue();
                        }
                        super.setValue(n);
                    }
                }
        );

        decay.setModel(
                new DefaultBoundedRangeModel() {
                    @Override
                    public void setValue(int n) {
                        if (n < attack.getValue()) {
                            n = attack.getValue();
                        }
                        super.setValue(n);
                    }
                }
        );
    }

}
