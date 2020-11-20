package ui.components.dialogues;

import synthesis.KeyedElement;
import ui.components.GraphDrawer;

import javax.swing.*;
import java.util.Map;

// UI for drawing keyed element curves
public class KeyedElementPropertiesDialogue extends PropertiesDialogue {

    // MODIFIES: element
    // EFFECTS: display dialog, save drawn curve to element if it has at least 2 points, retry otherwise
    public KeyedElementPropertiesDialogue(JComponent target, KeyedElement element,
                                          String message, String title, GraphDrawer graph) {

        panel.add(new JLabel(message));
        panel.add(Box.createVerticalStrut(5));
        panel.add(graph);

        while (true) {
            JOptionPane.showMessageDialog(target, panel, title, JOptionPane.PLAIN_MESSAGE);
            if  (graph.getFrames().size() < 2) {
                JOptionPane.showMessageDialog(target, "Insufficient Frames!", "Error:", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        element.clear();
        for (Map.Entry<Integer, Double> frame : graph.getFrames().entrySet()) {
            element.addFrame(frame.getValue());
        }
    }

}
