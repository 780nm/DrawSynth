package ui.components.dialogues;

import synthesis.KeyedPitch;
import ui.components.GraphDrawer;

import javax.swing.*;
import java.util.Map;

public class KeyedPitchPropertiesDialogue extends PropertiesDialogue {

    public KeyedPitchPropertiesDialogue(JComponent target, KeyedPitch mod) {
        GraphDrawer graph = new GraphDrawer(0.5,1.5,1);

        panel.add(new JLabel("Draw the desired pitch curve on the graph and hit OK when satisfied. "
                + "Values are in multiples of the base pitch."));

        panel.add(Box.createVerticalStrut(5));
        panel.add(graph);

        JOptionPane.showMessageDialog(target, panel, "Add Amplitude Modulator", JOptionPane.PLAIN_MESSAGE);

        mod.clear();
        for (Map.Entry<Integer, Double> frame : graph.getFrames().entrySet()) {
            mod.addFrame(frame.getValue());
        }
    }


}
