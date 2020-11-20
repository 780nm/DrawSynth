package ui.components.dialogues;

import synthesis.KeyedElement;
import ui.components.GraphDrawer;

import javax.swing.*;
import java.util.Map;

public class KeyedElementPropertiesDialogue extends PropertiesDialogue {

    public KeyedElementPropertiesDialogue(JComponent target, KeyedElement element,
                                          String message, String title, GraphDrawer graph) {

        panel.add(new JLabel(message));
        panel.add(Box.createVerticalStrut(5));
        panel.add(graph);

        JOptionPane.showMessageDialog(target, panel, title, JOptionPane.PLAIN_MESSAGE);

        element.clear();
        for (Map.Entry<Integer, Double> frame : graph.getFrames().entrySet()) {
            element.addFrame(frame.getValue());
        }
    }

}
