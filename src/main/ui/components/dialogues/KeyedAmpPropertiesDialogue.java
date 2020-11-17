package ui.components.dialogues;

import synthesis.KeyedAmplitude;
import ui.components.GraphDrawer;

import javax.swing.*;
import java.util.Map;

public class KeyedAmpPropertiesDialogue extends PropertiesDialogue {

    public KeyedAmpPropertiesDialogue(JComponent target, KeyedAmplitude mod) {
        GraphDrawer graph = new GraphDrawer(0,1,0);

        panel.add(new JLabel("Draw the desired amplitude curve on the graph and hit OK when satisfied"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(graph);

        JOptionPane.showMessageDialog(target, panel, "Add Amplitude Modulator", JOptionPane.PLAIN_MESSAGE);

        mod.clear();
        for (Map.Entry<Integer, Double> frame : graph.getFrames().entrySet()) {
            mod.addFrame(frame.getValue());
        }
    }

}
