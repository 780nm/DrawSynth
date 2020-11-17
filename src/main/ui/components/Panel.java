package ui.components;

import model.Sequencer;
import ui.SequencerApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

public abstract class Panel extends JPanel implements ActionListener {

    protected SequencerApp app;

    public Panel(SequencerApp app) {
        super();

        this.app = app;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setAlignmentY(Component.TOP_ALIGNMENT);

    }

    protected void addRow(JComponent target, String rowTitle, String action, String buttonText, String infoText) {
        Map<String,String> act = new TreeMap<>();
        act.put(buttonText, action);
        addRow(target, rowTitle, act, new JLabel("   " + infoText));
    }

    protected void addRow(JComponent target, String rowTitle, Map<String,String> actions, JComponent content) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(new JLabel(rowTitle + "   "));

        for (Map.Entry<String, String> action : actions.entrySet()) {
            JButton edit = new JButton(action.getKey());
            edit.setSize(60, 30);
            edit.setEnabled(true);
            edit.setActionCommand(action.getValue());
            edit.addActionListener(this);

            row.add(edit);
        }

        row.add(content);

        target.add(Box.createVerticalStrut(5));
        target.add(row);
        target.add(Box.createVerticalStrut(5));
    }

    protected void addSep() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        Dimension size = new Dimension(
                separator.getMaximumSize().width,
                separator.getPreferredSize().height);
        separator.setMaximumSize(size);
        add(separator);
    }

    protected JPanel panelHelper() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

}
