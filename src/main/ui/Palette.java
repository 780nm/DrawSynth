package ui;

import model.Note;
import model.Sequencer;
import synthesis.AmplitudeModulator;
import synthesis.EnvelopeAmplitude;
import synthesis.PitchModulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Palette extends JPanel implements ActionListener {

    private SequencerApp app;

    private JPanel ampModsPanel;
    private JPanel pitchModsPanel;
    private JPanel notesPanel;

    public Palette(SequencerApp app) {
        super();

        this.app = app;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setAlignmentY(Component.TOP_ALIGNMENT);

        addRow(this, "", "add:ampMod", "Add", "Amplitude Modulators:");
        addAmpModInfo();
        addSep();
        addRow(this, "", "add:pitchMod", "Add", "Pitch Modulators:");
        addPitchModInfo();
        addSep();
        addRow(this, "", "add:note", "Add", "Notes:");
        addNotesInfo();
    }

    private void addSep() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        Dimension size = new Dimension(
                separator.getMaximumSize().width,
                separator.getPreferredSize().height);
        separator.setMaximumSize(size);
        add(separator);
    }

    private void addAmpModInfo() {
        ampModsPanel = panelHelper();

        ArrayList<AmplitudeModulator> ampMods = app.getSeq().getAmpMods();
        for (int i = 0; i < ampMods.size(); i++) {
            AmplitudeModulator mod = ampMods.get(i);
            String uuid = mod.getUuid().toString();
            String className = mod.getClass().getName();
            String info;
            switch (className) {
                case "synthesis.EnvelopeAmplitude":
                    info = "Envelope Amp: Attack - " + String.format("%.4g", ((EnvelopeAmplitude)mod).getAttack())
                            + ", Decay - " + String.format("%.4g", ((EnvelopeAmplitude)mod).getDecay())
                            + ", Balance - " + String.format("%.4g", ((EnvelopeAmplitude)mod).getBalance());
                    break;
                case "synthesis.KeyedAmplitude":
                    info = "Keyframed Amplitude";
                    break;
                default:
                    info = "Amplitude Modulator";
            }
            addRow(ampModsPanel, "   "  + i, "edit:ampMod:" + className + ":" + uuid, "Edit", info);
        }
        add(ampModsPanel);
    }

    public void addPitchModInfo() {
        pitchModsPanel = panelHelper();

        ArrayList<PitchModulator> pitchMods = app.getSeq().getPitchMods();
        for (int i = 0; i < pitchMods.size(); i++) {
            PitchModulator mod = pitchMods.get(i);
            String uuid = mod.getUuid().toString();
            String className = mod.getClass().getName();
            String info;
            switch (className) {
                case "synthesis.ConstantPitch":
                    info = "Constant Pitch Modulator";
                    break;
                case "synthesis.KeyedPitch":
                    info = "Keyframed Pitch Modulator";
                    break;
                default:
                    info = "Pitch Modulator";
            }
            addRow(pitchModsPanel, "   " + i, "edit:pitchMod:" + className + ":" + uuid, "Edit", info);
        }
        add(pitchModsPanel);
    }

    public  void addNotesInfo() {
        notesPanel = panelHelper();

        ArrayList<Note> notes = app.getSeq().getNotes();
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            String uuid = note.getUuid().toString();
            String info = "Base Pitch: " +  String.format("%.6g", note.getBasePitch()) + "hz "
                    + ", Base Amp: " + String.format("%.4g", note.getBaseAmplitude() * 100) + "% "
                    + ", Duration: "
                    + String.format("%.4g", note.getDuration() / (double)SequencerApp.FORMAT.getFrameRate()) + "s";
            addRow(notesPanel, "   " + i, "edit:note:synthesis.Note:" + uuid, "Edit", info);
        }
        add(notesPanel);
    }

    private void addRow(JComponent target, String rowTitle, String action, String buttonText, String infoText) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton edit = new JButton(buttonText);
        edit.setSize(60,30);
        edit.setEnabled(true);
        edit.setActionCommand(action);
        edit.addActionListener(this);

        row.add(new JLabel(rowTitle + "   "));
        row.add(edit);
        row.add(new JLabel("   " + infoText));

        target.add(Box.createVerticalStrut(5));
        target.add(row);
        target.add(Box.createVerticalStrut(5));
    }

    private JPanel panelHelper() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(":");
        switch (command[0]) {
            case "add":
                command = processAdd(command);
                processEdit(command);
                break;
            case "edit":
                processEdit(command);
        }

        revalidate();
        repaint();
    }

    private String[] processAdd(String[] command) {
        String[] editCommand;
        switch (command[2]) {
            case "ampMod":

            case "pitchMod":

            case "note":

            default:
                editCommand = new String[]{"", "", "", ""};
        }
        return editCommand;
    }

    private void processEdit(String[] command) {

    }

}
