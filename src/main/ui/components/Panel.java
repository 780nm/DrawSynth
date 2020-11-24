package ui.components;

import ui.SequencerApp;
import ui.actions.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

// Abstract UI component representing an application panel, with helper functions
public abstract class Panel extends JPanel {

    protected SequencerApp app;
    protected ActionManager actionManager;

    // EFFECTS: creates a new panel with the given action manager and associated application
    public Panel(SequencerApp app, ActionManager actionManager) {
        super();

        this.app = app;
        this.actionManager = actionManager;
        actionManager.setTarget(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setAlignmentY(Component.TOP_ALIGNMENT);

    }

    // MODIFIES: this
    // EFFECTS: Adds a row to the panel with given title, button and info text
    protected void addRow(JComponent target, String rowTitle, String action, String buttonText, String infoText) {
        Map<String,String> act = new TreeMap<>();
        act.put(buttonText, action);
        addRow(target, rowTitle, act, new JLabel(infoText));
    }

    // MODIFIES: this
    // EFFECTS: Adds a row to the panel with given title, buttons and content
    protected void addRow(JComponent target, String rowTitle, Map<String,String> actions, JComponent content) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(Box.createHorizontalStrut(10));

        if (rowTitle != null) {
            row.add(new JLabel(rowTitle));
            row.add(Box.createHorizontalStrut(10));
        }

        for (Map.Entry<String, String> action : actions.entrySet()) {
            row.add(editButton(action.getKey(), action.getValue()));
        }

        row.add(Box.createHorizontalStrut(10));
        row.add(content);

        boundHeight(row);

        target.add(Box.createVerticalStrut(5));
        target.add(row);
        target.add(Box.createVerticalStrut(5));
    }

    // EFFECTS: Constructs a button with the given title and action and returns it
    protected JButton editButton(String title, String action) {
        JButton button = new JButton(title);
        button.setSize(60, 30);
        button.setEnabled(true);
        button.setActionCommand(action);
        button.addActionListener(actionManager);
        return button;
    }

    // MODIFIES: this
    // EFFECTS: Adds a horizontal separator to the panel
    protected void addSep() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        boundHeight(separator);
        add(separator);
    }

    // MODIFIES: component
    // EFFECTS: Bounds the vertical height of a component so it doesn't expand to fill space
    protected void boundHeight(JComponent component) {
        component.setMaximumSize(new Dimension(
                component.getMaximumSize().width,
                component.getPreferredSize().height
        ));
    }

    // EFFECTS: Returns an empty JPanel with column formatting
    protected JPanel panelHelper() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

}
