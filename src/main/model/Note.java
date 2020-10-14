package model;

import synthesis.AmplitudeModulator;
import synthesis.Instrument;
import synthesis.PitchModulator;
import synthesis.SampleOps;

import javax.sound.sampled.AudioFormat;

// Represents a single note, which may be played in a track
public class Note {

    private AmplitudeModulator amplitude;
    private PitchModulator pitch;

    private int duration;

    // REQUIRES: duration > 0, amplitude and pitch are not null
    // EFFECTS: Constructs a note with the given amplitude, pitch and duration
    public Note(AmplitudeModulator amplitude, PitchModulator pitch, int duration) {
        this.amplitude = amplitude;
        this.pitch = pitch;
        this.duration = duration;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // EFFECTS: Generates a double array containing the full audio of the note,
    //          as played with the given instrument.
    public double[] synthesizeSample(AudioFormat format, Instrument instrument) {
        double[] wave = instrument.synthesizeSample(pitch, duration, format);
        amplitude.applyAmplitudeProfile(wave, format);
        return wave;
    }

    // Getters and Setters

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PitchModulator getPitch() {
        return pitch;
    }

    public void setPitch(PitchModulator pitch) {
        this.pitch = pitch;
    }

    public AmplitudeModulator getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(AmplitudeModulator amplitude) {
        this.amplitude = amplitude;
    }

}
