package ui;

import exceptions.ElementNotFoundException;
import exceptions.GeneratorException;
import exceptions.NoteIntersectionException;
import model.Note;
import model.Sequencer;
import model.Track;
import persistence.JsonReader;
import persistence.JsonWriter;
import synthesis.*;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

// Represents the current applications state and user interaction
public class SequencerApp extends JFrame {

    public static final AudioFormat FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    private InputHandler input;
    private Player player;
    private Sequencer seq;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeContent();

    }

    public void reinitializeContent() {
        initializeContent();
        repaint();
        System.gc();
    }

    private void initializeContent() {
        //Create and set up the content pane.

        JMenuBar bar = new MenuBar(this);
        setJMenuBar(bar);

        JTabbedPane content = new JTabbedPane();
        content.setOpaque(true);

        content.addTab("Palette", new Palette(this));

        setSize(1000,800);
        setContentPane(content);
        setVisible(true);
    }

    public void play() {
        player.start();
    }




    public Sequencer getSeq() {
        return seq;
    }

    public void setSeq(Sequencer seq) {
        this.seq = seq;
        player.setTarget(seq);
        reinitializeContent();
    }




























    // MODIFIES: this
    // EFFECTS: Add a new Constant Pitch Profile to the pitch bank
    private void processAddPitchProfile() {
        System.out.println("Creating new Constant Pitch Profile...");
        int index = seq.addPitchMod(new ConstantPitch());
        System.out.println("Profile added at index " + index);
    }

    // MODIFIES: this
    // EFFECTS: Add a new Note to the notes bank
    private void processAddNote() {
        System.out.println("Creating new Note...");

        ArrayList<AmplitudeModulator> ampMods = seq.getAmpMods();
        ArrayList<PitchModulator> pitchMods = seq.getPitchMods();

        if (ampMods.size() + pitchMods.size() < 2) {
            System.out.println("One of Amp and Pitch profile not created. Add them and come back");
            return;
        }

        DoubleEvaluator evalAmp = input -> 0 <= input && input <= 1;
        double baseAmp = input.getSafeDouble(evalAmp, "Amplitude: ", "Invalid input");

        DoubleEvaluator evalAmpIndex = input -> 0 <= input && input <= ampMods.size() - 1;
        int amp = input.getSafeInt(evalAmpIndex, "Amplitude Profile index: ", "Invalid input");

        DoubleEvaluator evalPitch = input -> 20 <= input && input <= 20000;
        double basePitch = input.getSafeDouble(evalPitch, "Pitch: ", "Invalid input");

        DoubleEvaluator evalPitchIndex = input -> 0 <= input && input <= pitchMods.size() - 1;
        int pitch = input.getSafeInt(evalPitchIndex, "Pitch Profile index: ", "Invalid input");

        DoubleEvaluator evalDuration = input -> 0 < input;
        int duration = input.getSafeInt(evalDuration, "Duration (frames): ", "Invalid input");

        addNote(baseAmp, ampMods.get(amp).getUuid(), basePitch, pitchMods.get(pitch).getUuid(), duration);
    }

    private void addNote(double baseAmp, UUID ampMod, double basePitch, UUID pitchMod, int duration) {
        try {
            int index = seq.addNote(baseAmp, ampMod,
                    basePitch, pitchMod, duration);
            System.out.println("Note added at index " + index);
        } catch (ElementNotFoundException exception) {
            System.err.println("Couldn't find required profile");
        }
    }

    private void processAddTrack() {
        ArrayList<Instrument> instrs = seq.getInstruments();

        DoubleEvaluator evalIndex = input -> 0 <= input && input <= instrs.size() - 1;
        int instr = input.getSafeInt(evalIndex, "Instrument index: ", "Invalid input");

        try {
            seq.addTrack(instrs.get(instr).getUuid());
        } catch (ElementNotFoundException exception) {
            System.err.println("Couldn't find instrument");
        }
    }

    // MODIFIES: this
    // EFFECTS: Add a new Note to the current track at the specified position, checking for collision
    private void processInsert() {
        System.out.println("Inserting Note...");
        ArrayList<Note> notes = seq.getNotes();
        ArrayList<Track> tracks = seq.getTracks();

        if (notes.size() < 1) {
            System.out.println("No notes to add. Add some and come back");
            return;
        }

        DoubleEvaluator evalNote = input -> 0 <= input && input <= notes.size() - 1;
        int note = input.getSafeInt(evalNote, "Note index: ", "Invalid input");
        DoubleEvaluator evalTrack = input -> 0 <= input && input <= tracks.size() - 1;
        int track = input.getSafeInt(evalTrack, "Track index: ", "Invalid input");

        insertNote(notes.get(note).getUuid(), tracks.get(track).getUuid());
    }

    private void insertNote(UUID note, UUID track) {
        DoubleEvaluator evalPosition =
                input -> {
                    try {
                        seq.insertNote((int) input, note, track);
                        return true;
                    } catch (NoteIntersectionException exception) {
                        return false;
                    } catch (ElementNotFoundException exception) {
                        System.err.println("Couldn't find required elements");
                        return false;
                    }
                };
        int position = input.getSafeInt(evalPosition, "Position (frames): ",
                "Invalid input");

        System.out.println("Note added at: " + position);
    }

}
