package ui.components;

import ui.SequencerApp;
import ui.actions.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

public abstract class Panel extends JPanel {

    protected SequencerApp app;
    protected ActionListener actionListener;

    public Panel(SequencerApp app, ActionManager actionListener) {
        super();

        this.app = app;
        this.actionListener = actionListener;
        actionListener.setTarget(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setAlignmentY(Component.TOP_ALIGNMENT);

    }

    protected void addRow(JComponent target, String rowTitle, String action, String buttonText, String infoText) {
        Map<String,String> act = new TreeMap<>();
        act.put(buttonText, action);
        addRow(target, rowTitle, act, new JLabel(infoText));
    }

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

    protected JButton editButton(String title, String action) {
        JButton button = new JButton(title);
        button.setSize(60, 30);
        button.setEnabled(true);
        button.setActionCommand(action);
        button.addActionListener(actionListener);
        return button;
    }

    protected void addSep() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        boundHeight(separator);
        add(separator);
    }

    protected void boundHeight(JComponent component) {
        component.setMaximumSize(new Dimension(
                component.getMaximumSize().width,
                component.getPreferredSize().height
        ));
    }

    protected JPanel panelHelper() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

}
