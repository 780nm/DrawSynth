package ui;

import model.Note;
import model.Track;
import synthesis.*;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.Map;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

// Represents the current applications state and user interaction
public class SequencerApp {

    private static final AudioFormat FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    private InputHandler input;
    private Track track;
    private Player player;

    private ArrayList<AmplitudeModulator> ampModulators;
    private ArrayList<PitchModulator> pitchModulators;
    private ArrayList<Note> notes;

    // EFFECTS: Initializes the application
    public SequencerApp() {
        ampModulators = new ArrayList<>();
        pitchModulators = new ArrayList<>();
        notes = new ArrayList<>();

        Instrument instrument = new SinusoidInstrument();
        track = new Track(instrument);
        player = new Player(FORMAT);

        player.setTarget(track);

        input = new InputHandler();
    }

    // EFFECTS: Polls the user for command input
    public void start() {
        System.out.println("Welcome!");

        while (true) {
            String command = input.getSafeString(new String[] {"add", "insert", "play", "exit"},
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
                    break;
            }

        }

    }

    // EFFECTS: Polls the user for what they would like to add, and calls the appropriate method
    private void processAdd() {
        String command = input.getSafeString(new String[] {"a-mod", "p-mod", "note", "exit"},
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

        DoubleEvaluator evalRatio = input -> 0 <= input && input <= 1;
        double amp = input.getSafeDouble(evalRatio, "Amplitude: ", "Invalid input");
        double attack = input.getSafeDouble(evalRatio, "Attack: ", "Invalid input");

        DoubleEvaluator evalDecay = input -> 0 <= input && input <= 1 && input + attack <= 1;
        double decay = input.getSafeDouble(evalDecay, "Decay: ", "Invalid input");

        DoubleEvaluator evalBal = input -> -1 <= input && input <= 1;
        double bal = input.getSafeDouble(evalBal, "Balance: ", "Invalid input");

        ampModulators.add(new EnvelopeAmplitude(amp, attack, decay, bal));
        System.out.println("Profile added at index " + (ampModulators.size() - 1));
    }

    // MODIFIES: this
    // EFFECTS: Add a new Constant Pitch Profile to the pitch bank
    private void processAddPitchProfile() {
        System.out.println("Creating new Constant Pitch Profile...");

        DoubleEvaluator evalPitch = input -> 0 < input;
        double pitch = input.getSafeDouble(evalPitch, "Pitch: ", "Invalid input");

        pitchModulators.add(new ConstantPitch(pitch));
        System.out.println("Profile added at index " + (pitchModulators.size() - 1));
    }

    // MODIFIES: this
    // EFFECTS: Add a new Note to the notes bank
    private void processAddNote() {
        System.out.println("Creating new Note...");

        if (ampModulators.size() + pitchModulators.size() < 2) {
            System.out.println("One of Amp and Pitch profile not created. Add them and come back");
            return;
        }

        DoubleEvaluator evalPitchIndex = input -> 0 <= input && input <= pitchModulators.size() - 1;
        int pitch = input.getSafeInt(evalPitchIndex, "Pitch Profile index: ", "Invalid input");

        DoubleEvaluator evalAmpIndex = input -> 0 <= input && input <= ampModulators.size() - 1;
        int amp = input.getSafeInt(evalAmpIndex, "Amplitude Profile index: ", "Invalid input");

        DoubleEvaluator evalDuration = input -> 0 < input;
        int duration = input.getSafeInt(evalDuration, "Duration (frames): ", "Invalid input");

        notes.add(new Note(ampModulators.get(amp), pitchModulators.get(pitch), duration));
        System.out.println("Note added at index " + (notes.size() - 1));
    }

    // MODIFIES: this
    // EFFECTS: Add a new Note to the current track at the specified position, checking for collision
    private void processInsert() {
        System.out.println("Inserting Note...");

        if (notes.size() < 1) {
            System.out.println("No notes to add. Add some and come back");
            return;
        }

        DoubleEvaluator evalNote = input -> 0 <= input && input <= notes.size() - 1;
        int note = input.getSafeInt(evalNote, "Note index: ", "Invalid input");

        DoubleEvaluator evalPosition =
                input -> {
                    for (Map.Entry<Integer, Note> entry : track.getNotes().entrySet()) {
                        if (entry.getKey() <= input && entry.getKey() + entry.getValue().getDuration() >= input) {
                            return false;
                        }
                    }
                    return true;
                };
        int position = input.getSafeInt(evalPosition, "Position (frames): ",
                "Invalid input, note overlaps existing note");

        track.addNote(position, notes.get(note));
        System.out.println("Note added at: " + position);
    }

}
