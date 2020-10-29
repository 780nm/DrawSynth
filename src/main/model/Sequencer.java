package model;

import exceptions.ElementNotFoundException;
import exceptions.NoteIntersectionException;
import org.json.JSONObject;
import persistence.Persistent;
import synthesis.AmplitudeModulator;
import synthesis.Instrument;
import synthesis.PitchModulator;
import synthesis.SampleUtil;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

import static persistence.PersistenceUtil.arrayToJson;
import static persistence.PersistenceUtil.getElementWithID;

public class Sequencer implements Playable, Persistent {

    private UUID sequencerID;

    private ArrayList<AmplitudeModulator> ampMods;
    private ArrayList<PitchModulator> pitchMods;

    private ArrayList<Note> notes;
    private ArrayList<Instrument> instruments;

    private ArrayList<Track> tracks;

    // EFFECTS: Constructs an empty Sequencer with a random UUID
    public Sequencer() {
        sequencerID = UUID.randomUUID();
        ampMods = new ArrayList<>();
        pitchMods = new ArrayList<>();
        notes = new ArrayList<>();
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
    }

    // EFFECTS: Constructs an empty Sequencer with the given initial parameters
    public Sequencer(UUID id) {
        sequencerID = id;
        ampMods = new ArrayList<>();
        pitchMods = new ArrayList<>();
        notes = new ArrayList<>();
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: byte array containing the full encoded audio in the given track.
    public byte[] synthesizeClip(AudioFormat format) {
        return SampleUtil.encodeBytes(synthesizeWaveform(format), format);
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Generates a Double ArrayList containing the full encoded audio in the given track.
    public ArrayList<Double> synthesizeWaveform(AudioFormat format) {
        ArrayList<ArrayList<Double>> waveforms = new ArrayList<>();
        for (Track track : tracks) {
            waveforms.add(track.synthesizeWaveform(format));
        }
        return SampleUtil.downMix(waveforms, format);
    }

    // MODIFIES: this
    // EFFECTS: Adds the AmplitudeModulator to the sequencer and returns its index
    public int addAmpMod(AmplitudeModulator ampMod) {
        ampMods.add(ampMod);
        return ampMods.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: Adds the PitchModulator to the sequencer and returns its index
    public int addPitchMod(PitchModulator pitchMod) {
        pitchMods.add(pitchMod);
        return pitchMods.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: Adds the Instrument to the sequencer and returns its index
    public int addInstrument(Instrument instrument) {
        instruments.add(instrument);
        return instruments.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: Creates a new Note and adds to the sequencer and returns its index.
    //          Throws ElementNotFoundException if provided amp and pitch mod UUIDs are not present in the sequencer.
    public int addNote(double baseAmp, UUID ampModID, double basePitch, UUID pitchModID, int duration)
            throws ElementNotFoundException {

        return addNote(UUID.randomUUID(), baseAmp, ampModID, basePitch, pitchModID, duration);
    }

    // MODIFIES: this
    // EFFECTS: Creates a new Note with the given UUID and adds to the sequencer and returns its index.
    //          Throws ElementNotFoundException if provided amp and pitch mod UUIDs are not present in the sequencer.
    public int addNote(UUID id, double baseAmp, UUID ampModID, double basePitch, UUID pitchModID, int duration)
            throws ElementNotFoundException {

        Note note = new Note(baseAmp, getElementWithID(ampMods, ampModID),
                basePitch, getElementWithID(pitchMods, pitchModID), duration, id);
        notes.add(note);
        return notes.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: Creates a new Track and adds to the sequencer and returns its index.
    //          Throws ElementNotFoundException if provided instrument UUID is not present in the sequencer.
    public int addTrack(UUID instrumentID) throws ElementNotFoundException {
        return addTrack(UUID.randomUUID(), instrumentID);
    }

    // MODIFIES: this
    // EFFECTS: Creates a new Track with the given UUID and adds to the sequencer and returns its index.
    //          Throws ElementNotFoundException if provided instrument UUID is not present in the sequencer.
    public int addTrack(UUID trackID, UUID instrumentID) throws ElementNotFoundException {
        Track track = new Track(getElementWithID(instruments, instrumentID), trackID);
        tracks.add(track);
        return tracks.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: Inserts a note with the given UUID into the track with the given UUID at the given time.
    //          Throws ElementNotFoundException if provided note.track UUIDs are not present in the sequencer.
    public boolean insertNote(int time, UUID noteID, UUID trackID)
            throws ElementNotFoundException, NoteIntersectionException {
        Track track = getElementWithID(tracks, trackID);
        Note note = getElementWithID(notes, noteID);

        return track.addNote(time, note);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", sequencerID.toString());
        json.put("ampMods", arrayToJson(ampMods));
        json.put("pitchMods", arrayToJson(pitchMods));
        json.put("notes", arrayToJson(notes));
        json.put("instruments", arrayToJson(instruments));
        json.put("tracks", arrayToJson(tracks));
        return json;
    }

    // Getters and Setters

    public ArrayList<AmplitudeModulator> getAmpMods() {
        return ampMods;
    }

    public ArrayList<PitchModulator> getPitchMods() {
        return pitchMods;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public UUID getUuid() {
        return sequencerID;
    }

}
