package ui.components.dialogues;

import javax.swing.*;
import java.awt.*;

// UI for inputting element properties
public abstract class PropertiesDialogue {

    protected JPanel panel;

    // EFFECTS: Sets up the dialogue panel
    protected PropertiesDialogue() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // EFFECTS: Adds a row to the target panel, with given title and contents
    protected void addRow(JComponent target, String rowTitle, JComponent content) {
        JPanel row = new JPanel();

        JLabel label = new JLabel(rowTitle);
        label.setPreferredSize(new Dimension(150, 30));
        row.add(label);
        content.setPreferredSize(new Dimension(400, 30));
        row.add(content);

        target.add(Box.createVerticalStrut(5));
        target.add(row);
        target.add(Box.createVerticalStrut(5));
    }

}
