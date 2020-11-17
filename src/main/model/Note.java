package model;

import org.json.JSONObject;
import persistence.Persistent;
import synthesis.AmplitudeModulator;
import synthesis.Instrument;
import synthesis.PitchModulator;
import ui.SequencerApp;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

// Represents a single note, which may be played in a track
public class Note implements Persistent {

    private UUID noteID;

    private AmplitudeModulator ampMod;
    private PitchModulator pitchMod;

    private double baseAmplitude;
    private double basePitch;
    
    private int duration;

    // REQUIRES: duration > 0, amplitude and pitch are not null.
    // EFFECTS: Constructs a note with the given amplitude, amplitude modulator,
    //          pitch, pitch modulator, duration and a random UUID.
    public Note(double baseAmplitude, AmplitudeModulator ampMod,
                double basePitch, PitchModulator pitchMod, int duration) {
        noteID = UUID.randomUUID();
        this.baseAmplitude = baseAmplitude;
        this.basePitch = basePitch;
        this.ampMod = ampMod;
        this.pitchMod = pitchMod;
        this.duration = duration;
    }

    // REQUIRES: duration > 0, amplitude, pitch and id are not null.
    // EFFECTS: Constructs a note with the given amplitude, amplitude modulator,
    //          pitch, pitch modulator, duration and UUID.
    public Note(double baseAmplitude, AmplitudeModulator ampMod,
                double basePitch, PitchModulator pitchMod, int duration, UUID id) {
        noteID = id;
        this.baseAmplitude = baseAmplitude;
        this.basePitch = basePitch;
        this.ampMod = ampMod;
        this.pitchMod = pitchMod;
        this.duration = duration;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // EFFECTS: Generates a Double ArrayList containing the full audio of the note,
    //          as played with the given instrument.
    public ArrayList<Double> synthesizeWaveform(AudioFormat format, Instrument instrument) {
        ArrayList<Double> wave = instrument.synthesizeWaveform(basePitch, pitchMod, duration, format);
        ampMod.applyAmplitudeProfile(baseAmplitude, wave, format);
        return wave;
    }

    // EFFECTS: Returns the state of the Note as a serialized JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", noteID.toString());
        json.put("ampMod", ampMod.getUuid());
        json.put("pitchMod", pitchMod.getUuid());
        json.put("bAmp", baseAmplitude);
        json.put("bPitch", basePitch);
        json.put("duration", duration);
        return json;
    }

    @Override
    public String toString() {
        return "Base Pitch: " +  String.format("%.6g", basePitch) + "hz "
                + ", Base Amp: " + String.format("%.4g", baseAmplitude * 100) + "% "
                + ", Duration: "
                + String.format("%.4g", duration / (double) SequencerApp.FORMAT.getFrameRate()) + "s";
    }

    // Getters and Setters

    public UUID getUuid() {
        return noteID;
    }

    public double getBasePitch() {
        return basePitch;
    }

    public void setBasePitch(double basePitch) {
        this.basePitch = basePitch;
    }

    public double getBaseAmplitude() {
        return baseAmplitude;
    }

    public void setBaseAmplitude(double baseAmplitude) {
        this.baseAmplitude = baseAmplitude;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PitchModulator getPitchMod() {
        return pitchMod;
    }

    public void setPitchMod(PitchModulator pitchMod) {
        this.pitchMod = pitchMod;
    }

    public AmplitudeModulator getAmpMod() {
        return ampMod;
    }

    public void setAmpMod(AmplitudeModulator ampMod) {
        this.ampMod = ampMod;
    }

}
