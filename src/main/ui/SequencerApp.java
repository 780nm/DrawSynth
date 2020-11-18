package ui;

import exceptions.ElementNotFoundException;
import exceptions.NoteIntersectionException;
import model.Note;
import model.Sequencer;
import model.Track;
import synthesis.*;
import ui.components.MenuBar;
import ui.components.Palette;
import ui.components.Timeline;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.UUID;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

// Represents the current applications state and user interaction
public class SequencerApp extends JFrame {

    public static final AudioFormat FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    private final Player player;
    private Sequencer seq;
    private int tabIndex;

    // EFFECTS: Initializes the application
    public SequencerApp() throws ElementNotFoundException {
        super("Sequencer");

        Instrument instrument = new SinusoidInstrument();
        seq = new Sequencer();
        seq.addInstrument(instrument);
        seq.addTrack(instrument.getUuid());

        player = new Player(FORMAT);
        player.setTarget(seq);

        //Create and set up the window.
        tabIndex = 0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeContent();

    }

    public void reinitializeContent() {
        initializeContent();
        repaint();
        System.gc();
    }

    // With help from https://stackoverflow.com/questions/6799731/jtabbedpane-changelistener
    private void initializeContent() {
        JMenuBar bar = new MenuBar(this);
        setJMenuBar(bar);

        JTabbedPane content = new JTabbedPane();
        content.setOpaque(true);

        content.addTab("Palette", new Palette(this));
        content.addTab("Timeline", new Timeline(this));
        content.setSelectedIndex(tabIndex);

        content.addChangeListener(e -> tabIndex = content.getSelectedIndex());

        setSize(1000,800);
        setContentPane(content);
        setVisible(true);
    }

    public void play() {
        player.start();
    }

    // Getters and Setters

    public Sequencer getSeq() {
        return seq;
    }

    public void setSeq(Sequencer seq) {
        this.seq = seq;
        player.setTarget(seq);
        reinitializeContent();
    }

}
