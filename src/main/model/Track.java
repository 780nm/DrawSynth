package model;

import synthesis.Instrument;
import synthesis.SampleOps;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

// Represents a track, which contains a set of notes and an associated instrument they may be played with
public class Track implements Playable {
    private Map<Integer, Note> notes;
    private Instrument instrument;

    // REQUIRES: instrument is not null
    // EFFECTS: Constructs a new track with the given instrument and no notes
    public Track(Instrument instrument) {
        this.instrument = instrument;
        this.notes = new TreeMap<>();
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Generates a byte array containing the full encoded audio in the given track.
    //          Throws on failure to initialize output stream.
    //          Returns empty array if no audio is present.
    public byte[] synthesizeWaveform(AudioFormat format) throws IOException {
        double[] buffer;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Set<Integer> keySet = notes.keySet();
        int numSilentSamples;
        Note note;

        for (Integer timeStamp : keySet) {
            numSilentSamples = timeStamp  * format.getFrameSize() - output.size();
            for (int i = 0; i < numSilentSamples; i++) {
                output.write(0);
            }

            note = notes.get(timeStamp);
            buffer = note.synthesizeSample(format, instrument);
            output.write(SampleOps.encodeBytes(buffer, format));
        }

        return output.toByteArray();

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

    // Getters and Setters

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
