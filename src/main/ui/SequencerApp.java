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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

// Represents the current applications state and user interaction
public class SequencerApp {

    private static final AudioFormat FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private static final String FILENAME_REGEX = "^[a-zA-z0-9]*[.]json$";
    private static final String FILE_ROOT = "./data/";

    private InputHandler input;
    private Player player;
    private Sequencer seq;

    // EFFECTS: Initializes the application
    public SequencerApp() throws ElementNotFoundException {

        Instrument instrument = new SinusoidInstrument();
        seq = new Sequencer();
        seq.addInstrument(instrument);
        seq.addTrack(instrument.getUuid());

        player = new Player(FORMAT);
        player.setTarget(seq);

        input = new InputHandler();
    }

    // EFFECTS: Polls the user for command input
    public void start() {
        System.out.println("Welcome!");

        while (true) {
            String command = input.getSafeString(new String[] {"add", "insert", "play", "save", "load", "exit"},
                    "Enter a command: ", "Invalid command");
            switch (command.toLowerCase()) {
                case "add":
                    processAdd();
                    break;
                case "insert":
                    processInsert();
                    break;
                case "play":
                    System.out.println("Playing...");
                    player.start();
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    return;
                default:
                    processSaveLoad(command);
                    break;
            }
        }
    }

    // EFFECTS: Polls the user for what they would like to add, and calls the appropriate method
    private void processAdd() {
        String command = input.getSafeString(new String[] {"a-mod", "p-mod", "note", "track", "exit"},
                "What would you like to add? ", "Invalid selection");
        switch (command.toLowerCase()) {
            case "a-mod":
                processAddAmpProfile();
                break;
            case "p-mod":
                processAddPitchProfile();
                break;
            case "note":
                processAddNote();
                break;
            case "track":
                processAddTrack();
                break;
            case "exit":
                System.out.println("Returning...");
                return;
            default:
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Add a new Enveloped Amplitude Profile to the amp. profile bank
    private void processAddAmpProfile() {
        System.out.println("Creating new Enveloped Amplitude Profile...");

        DoubleEvaluator evalAttack = input -> 0 <= input && input <= 1;
        double attack = input.getSafeDouble(evalAttack, "Attack: ", "Invalid input");

        DoubleEvaluator evalDecay = input -> 0 <= input && input <= 1 && input + attack <= 1;
        double decay = input.getSafeDouble(evalDecay, "Decay: ", "Invalid input");

        DoubleEvaluator evalBal = input -> -1 <= input && input <= 1;
        double bal = input.getSafeDouble(evalBal, "Balance: ", "Invalid input");

        int index = seq.addAmpMod(new EnvelopeAmplitude(attack, decay, bal));
        System.out.println("Profile added at index " + index);
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


    // This had to be factored out to get past the maximum method length, otherwise this switch would be in start().
    private void processSaveLoad(String command) {
        switch (command) {
            case "save":
                processSave();
                return;
            case "load":
                processLoad();
        }
    }

    private void processSave() {
        String filename = input.getSafeString(FILENAME_REGEX, "Please input file name: ", "Invalid name");
        try {
            JsonWriter writer = new JsonWriter(FILE_ROOT + filename);
            writer.write(seq);
        } catch (FileNotFoundException exception) {
            System.out.println("Unable to create output file.");
        }
    }

    private void processLoad() {
        String filename = input.getSafeString(FILENAME_REGEX, "Please input file name: ", "Invalid name");
        try {
            JsonReader reader = new JsonReader(FILE_ROOT + filename);
            seq = reader.getSequencer();
            player.setTarget(seq);
        } catch (IOException exception) {
            System.out.println("Unable to load file.");
        } catch (GeneratorException exception) {
            System.out.println("Unable to generate Sequencer, malformed config. Cause: " + exception);
        }
    }
}
