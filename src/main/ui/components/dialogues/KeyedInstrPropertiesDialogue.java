package ui.components.dialogues;

import synthesis.KeyedInstrument;
import ui.components.GraphDrawer;

import javax.swing.*;
import java.util.Map;

public class KeyedInstrPropertiesDialogue extends PropertiesDialogue {

    public KeyedInstrPropertiesDialogue(JComponent target, KeyedInstrument instr) {
        GraphDrawer graph = new GraphDrawer(-1,1,0);

        panel.add(new JLabel("Draw the desired amplitude curve on the graph and hit OK when satisfied"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(graph);

        JOptionPane.showMessageDialog(target, panel, "Add Instrument", JOptionPane.PLAIN_MESSAGE);

        instr.clear();
        for (Map.Entry<Integer, Double> frame : graph.getFrames().entrySet()) {
            instr.addFrame(frame.getValue());
        }
    }

}
