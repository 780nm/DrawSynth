package persistence;

import exceptions.GeneratorException;
import model.Sequencer;
import org.json.JSONArray;
import org.json.JSONObject;
import synthesis.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static persistence.PersistenceUtil.*;

// Reads saved JSON Configuration files and generates model state
public class JsonReader {

    private String fileName;
    private JSONObject json;

    private Sequencer sequencer;

    // EFFECTS: Constructs a new JsonReader and generates state from the given file.
    //          Throws IOException on file access error, GeneratorException if config. is malformed.
    public JsonReader(String fileName) throws IOException {
        this.fileName = fileName;
        refresh();
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    // EFFECTS: Re-generates state from the given file.
    //          Throws IOException on file access error, GeneratorException if config. is malformed.
    public void refresh() throws IOException {
        String jsonData = readFile(fileName);
        json = new JSONObject(jsonData);
        generate();
    }

    // Lifted from JSONSerializationDemo.
    // EFFECTS: reads source file as string and returns it.
    //          Throws IOException on file access error.
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // MODIFIES: this
    // EFFECTS: Generates a Sequencer from the loaded configuration.
    //          Throws GeneratorException if config. is malformed.
    private void generate() {
        try {
            UUID id = UUID.fromString(json.getString("uuid"));
            sequencer = new Sequencer(id);

            generateAmpMods();
            generatePitchMods();
            generateInstruments();
            generateNotes();
            generateTracks();
        } catch (Exception e) {
            throw new GeneratorException(e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: Generates a list of AmplitudeModulators from the loaded configuration
    //          and writes it to the current sequencer. Throws GeneratorException if config. is malformed.
    private void generateAmpMods() {
        JSONArray modsJson = json.getJSONArray("ampMods");
        forEachEntry(modsJson, this::generateAmpMod);
    }

    // MODIFIES: this
    // EFFECTS: Generates an appropriate AmplitudeModulator from the given configuration
    //          and writes it to the current sequencer. Throws GeneratorException if config. is malformed.
    private void generateAmpMod(JSONObject modJson) {
        UUID id = UUID.fromString(modJson.getString("uuid"));
        String ampModClass = modJson.getString("class");
        AmplitudeModulator ampMod;

        switch (ampModClass) {
            case "synthesis.EnvelopeAmplitude":
                double attack = modJson.getDouble("attack");
                double decay = modJson.getDouble("decay");
                double balance = modJson.getDouble("balance");
                ampMod = new EnvelopeAmplitude(attack, decay, balance, id);
                break;
            case "synthesis.KeyedAmplitude":
                ampMod = new KeyedAmplitude(id);
                populateKeyedElement(modJson, (KeyedAmplitude)ampMod);
                break;
            default:
                throw new GeneratorException("ampMod Class not found");
        }

        sequencer.addAmpMod(ampMod);
    }

    // MODIFIES: this
    // EFFECTS: Generates a list of PitchModulators from the loaded configuration
    //          and writes it to the current sequencer. Throws GeneratorException if config. is malformed.
    private void  generatePitchMods() {
        JSONArray modsJson = json.getJSONArray("pitchMods");
        forEachEntry(modsJson, this::generatePitchMod);
    }

    // MODIFIES: this
    // EFFECTS: Generates an appropriate PitchModulator from the given configuration
    //          and writes it to the current sequencer. Throws GeneratorException if config. is malformed.
    private void generatePitchMod(JSONObject modJson) {
        UUID id = UUID.fromString(modJson.getString("uuid"));
        String pitchModClass = modJson.getString("class");
        PitchModulator pitchMod;

        switch (pitchModClass) {
            case "synthesis.ConstantPitch":
                pitchMod = new ConstantPitch(id);
                break;
            case "synthesis.KeyedPitch":
                pitchMod = new KeyedPitch(id);
                populateKeyedElement(modJson, (KeyedPitch)pitchMod);
                break;
            default:
                throw new GeneratorException("pitchMod Class not found");
        }

        sequencer.addPitchMod(pitchMod);
    }

    // MODIFIES: this
    // EFFECTS: Generates a list of Instruments from the loaded configuration and writes it to the current sequencer.
    //          Throws GeneratorException if config. is malformed.
    private void generateInstruments() {
        JSONArray instrumentsJson = json.getJSONArray("instruments");
        forEachEntry(instrumentsJson, this::generateInstrument);
    }

    // MODIFIES: this
    // EFFECTS: Generates an appropriate Instrument from the given configuration and writes it to the current sequencer.
    //          Throws GeneratorException if config. is malformed.
    private void generateInstrument(JSONObject instrJson) {
        UUID id = UUID.fromString(instrJson.getString("uuid"));
        String instrumentClass = instrJson.getString("class");
        Instrument instr;

        switch (instrumentClass) {
            case "synthesis.SinusoidInstrument":
                instr = new SinusoidInstrument(id);
                break;
            case "synthesis.KeyedInstrument":
                instr = new KeyedInstrument(id);
                populateKeyedElement(instrJson, (KeyedInstrument)instr);
                break;
            default:
                throw new GeneratorException("Instr. Gen");
        }

        sequencer.addInstrument(instr);
    }

    // MODIFIES: this
    // EFFECTS: Generates a list of Notes from the loaded configuration and writes it to the current sequencer.
    //          Throws ElementNotFoundException if config. is malformed.
    private void generateNotes() {
        JSONArray notesJson = json.getJSONArray("notes");
        forEachEntry(notesJson, this::generateNote);
    }

    // MODIFIES: this
    // EFFECTS: Generates a Note from the given configuration and writes it to the current sequencer.
    //          Throws ElementNotFoundException if config. is malformed
    private void generateNote(JSONObject noteJson) {
        UUID noteID = UUID.fromString(noteJson.getString("uuid"));

        double baseAmplitude = noteJson.getDouble("bAmp");
        UUID ampModID = UUID.fromString(noteJson.getString("ampMod"));
        double basePitch = noteJson.getDouble("bPitch");
        UUID pitchModID = UUID.fromString(noteJson.getString("pitchMod"));
        int duration = noteJson.getInt("duration");

        sequencer.addNote(noteID, baseAmplitude, ampModID, basePitch, pitchModID, duration);

    }

    // MODIFIES: this
    // EFFECTS: Generates a list of Tracks from the loaded configuration and writes it to the current sequencer.
    //          Throws ElementNotFoundException if config. is malformed.
    private void generateTracks() {
        JSONArray tracksJson = json.getJSONArray("tracks");
        forEachEntry(tracksJson, this::generateTrack);
    }

    // MODIFIES: this
    // EFFECTS: Generates a Track from the given configuration and writes it to the current sequencer.
    //          Throws ElementNotFoundException if config. is malformed.
    private void generateTrack(JSONObject trackJson) {
        UUID trackID = UUID.fromString(trackJson.getString("uuid"));
        UUID instrumentID = UUID.fromString(trackJson.getString("instrument"));

        sequencer.addTrack(trackID, instrumentID);
        populateTrackNotes(trackJson, trackID);
    }

    // MODIFIES: this
    // EFFECTS: Retrieves Notes with the UUID corresponding to those stored in the Track.
    //          configuration and populates the track.
    //          Throws ElementNotFoundException, NoteIntersectionException if config. is malformed.
    private void populateTrackNotes(JSONObject trackJson, UUID trackID) {
        JSONObject notesJson = trackJson.getJSONObject("notes");

        for (String time : notesJson.keySet()) {
            UUID noteID = UUID.fromString(notesJson.getString(time));
            sequencer.insertNote(Integer.parseInt(time), noteID, trackID);
        }
    }

    private void populateKeyedElement(JSONObject json, KeyedElement element) {
        json.getJSONArray("frames").forEach(frame -> element.addFrame(Double.valueOf(frame.toString())));
    }

}
