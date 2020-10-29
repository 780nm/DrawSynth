package model;

import exceptions.NoteIntersectionException;
import org.json.JSONObject;
import persistence.Persistent;
import synthesis.Instrument;
import synthesis.SampleUtil;

import javax.sound.sampled.AudioFormat;
import java.util.*;

import static persistence.PersistenceUtil.mapToJson;

// Represents a track, which contains a set of notes and an associated instrument they may be played with
public class Track implements Playable, Persistent {

    private UUID trackID;

    private Map<Integer, Note> notes;
    private Instrument instrument;

    private boolean muted;

    // REQUIRES: instrument is not null
    // EFFECTS: Constructs a new track with the given instrument, no notes and a random UUID
    public Track(Instrument instrument) {
        trackID = UUID.randomUUID();
        this.instrument = instrument;
        this.notes = new TreeMap<>();
        this.muted = false;
    }

    // REQUIRES: instrument is not null
    // EFFECTS: Constructs a new track with the given instrument and UUID and no notes
    public Track(Instrument instrument, UUID id) {
        trackID = id;
        this.instrument = instrument;
        this.notes = new TreeMap<>();
        this.muted = false;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Generates a Double ArrayList containing the full encoded audio in the given track.
    public ArrayList<Double> synthesizeWaveform(AudioFormat format) {
        ArrayList<Double> output = new ArrayList<>();

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
    public byte[] synthesizeClip(AudioFormat format) {
        return SampleUtil.encodeBytes(synthesizeWaveform(format), format);
    }

    // REQUIRES: timeStamp >= 0, note is not null
    // MODIFIES: this
    // EFFECTS: Adds note to track at specified timeStamp, in samples.
    //          Returns true if there was already a note at that timeStamp, false otherwise.
    //          Throws NoteIntersectionException is note overlaps with notes already in the track
    public boolean addNote(Integer timeStamp, Note note) throws NoteIntersectionException {
        for (Map.Entry<Integer, Note> entry : notes.entrySet()) {
            if (entry.getKey() < timeStamp && entry.getKey() + entry.getValue().getDuration() > timeStamp) {
                throw new NoteIntersectionException();
            }
        }
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

    // EFFECTS: Returns the state of the Note as a serialized JSONObject
    @Override
    public JSONObject toJson() {
        Map<Integer, String> noteUuids = new TreeMap<>();

        for (Map.Entry<Integer, Note> entry : notes.entrySet()) {
            noteUuids.put(entry.getKey(), entry.getValue().getUuid().toString());
        }

        JSONObject json = new JSONObject();
        json.put("uuid", trackID.toString());
        json.put("instrument", instrument.getUuid().toString());
        json.put("notes", mapToJson(noteUuids));
        return json;
    }

    // Getters and Setters

    public UUID getUuid() {
        return trackID;
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
