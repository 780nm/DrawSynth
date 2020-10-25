package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.PersistenceUtil;
import persistence.Persistent;
import synthesis.AmplitudeModulator;
import synthesis.PitchModulator;
import synthesis.SampleUtil;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

import static persistence.PersistenceUtil.arrayToJson;

public class Sequencer implements Playable, Persistent {

    private final UUID SEQ_ID;

    private ArrayList<AmplitudeModulator> ampMods;
    private ArrayList<PitchModulator> pitchMods;
    private ArrayList<Note> notes;
    private ArrayList<PitchModulator> instruments;

    private ArrayList<Track> tracks;

    public Sequencer() {
        SEQ_ID = UUID.randomUUID();
        ampMods = new ArrayList<>();
        pitchMods = new ArrayList<>();
        notes = new ArrayList<>();
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
    }

    public Sequencer(UUID id) {
        SEQ_ID = id;
        ampMods = new ArrayList<>();
        pitchMods = new ArrayList<>();
        notes = new ArrayList<>();
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
    }

    public byte[] synthesizeClip(AudioFormat format) {
        return SampleUtil.encodeBytes(synthesizeWaveform(format), format);
    }

    public ArrayList<Double> synthesizeWaveform(AudioFormat format) {
        ArrayList<ArrayList<Double>> waveforms = new ArrayList<>();
        for (Track track : tracks) {
            waveforms.add(track.synthesizeWaveform(format));
        }
        return SampleUtil.downMix(waveforms, format);
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ampMods", arrayToJson(ampMods));
        json.put("pitchMods", arrayToJson(pitchMods));
        json.put("notes", arrayToJson(notes));
        json.put("tracks", arrayToJson(tracks));
        return json;
    }

    public UUID getUuid() {
        return SEQ_ID;
    }

}
