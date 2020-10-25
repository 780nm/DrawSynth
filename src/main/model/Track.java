package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Persistent;
import synthesis.Instrument;
import synthesis.SampleUtil;

import javax.sound.sampled.AudioFormat;
import java.util.*;

import static persistence.PersistenceUtil.mapToJson;

// Represents a track, which contains a set of notes and an associated instrument they may be played with
public class Track implements Playable, Persistent {

    private final UUID TRACK_ID;

    private Map<Integer, Note> notes;
    private Instrument instrument;

    private boolean muted;

    // REQUIRES: instrument is not null
    // EFFECTS: Constructs a new track with the given instrument and no notes
    public Track(Instrument instrument) {
        TRACK_ID = UUID.randomUUID();
        this.instrument = instrument;
        this.notes = new TreeMap<>();
        this.muted = false;
    }

    // REQUIRES: instrument is not null
    // EFFECTS: Constructs a new track with the given instrument and UUID and no notes
    public Track(Instrument instrument, UUID id) {
        TRACK_ID = id;
        this.instrument = instrument;
        this.notes = new TreeMap<>();
        this.muted = false;
    }

    public ArrayList<Double> synthesizeWaveform(AudioFormat format) {
        ArrayList<Double> output = new ArrayList<Double>();

        Set<Integer> keySet = notes.keySet();
        int numSilentSamples;
        Note note;

        for (Integer timeStamp : keySet) {
            numSilentSamples = timeStamp  * format.getFrameSize() - output.size();
            for (int i = 0; i < numSilentSamples; i++) {
                output.add(0.);
            }

            note = notes.get(timeStamp);
            output.addAll(note.synthesizeWaveform(format, instrument));
        }

        return output;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Generates a byte array containing the full encoded audio in the given track.
    //          Throws on failure to initialize output stream.
    //          Returns empty array if no audio is present.
    public byte[] synthesizeClip(AudioFormat format) {
        return SampleUtil.encodeBytes(synthesizeWaveform(format), format);
    }

    // REQUIRES: timeStamp >= 0, note is not null and does not overlap any existing notes in the track
    // MODIFIES: this
    // EFFECTS: Adds note to track at specified timeStamp, in samples.
    //          returns true if there was already a note at that timeStamp, false otherwise
    public boolean addNote(Integer timeStamp, Note note) {
        Note prev = notes.put(timeStamp, note);
        return prev != null;
    }

    // REQUIRES: timeStamp >= 0
    // MODIFIES: this
    // EFFECTS: removes the note at the given timestamp, if it exists. returns true if there was a note, false otherwise
    public boolean removeNote(Integer timeStamp) {
        Note prev = notes.remove(timeStamp);
        return prev != null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", TRACK_ID.toString());
        json.put("instrument_uuid", instrument.getUuid().toString());
        json.put("notes", mapToJson(notes));
        return null;
    }

    // Getters and Setters

    public UUID getUuid() {
        return TRACK_ID;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public Map<Integer, Note> getNotes() {
        return notes;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

}
