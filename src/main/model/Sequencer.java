package model;

import synthesis.Instrument;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

// Represents a collection of tracks, which may be played synchronously
public class Sequencer implements Playable {

    private ArrayList<Track> tracks;

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, initTracks >= 0
    // EFFECTS: Constructs a Sequencer object with the given number of initial Tracks
    public Sequencer(Track[] initTracks) {
        //stub
    }

    // REQUIRES: count >= 0, Instrument is not null
    // MODIFIES: this
    // EFFECTS: Constructs and adds the specified number of tracks to the Sequencer, with the given instrument.
    public void addTracks(int count, Instrument instrument) {
        return; //stub
    }

    // REQUIRES: index is a valid track index
    // MODIFIES: this
    // EFFECTS: removes the track at the given index
    public void removeTrack(int index) {
        return; //stub
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Generates a byte array containing the full encoded audio in the given Sequencer.
    //          If no notes are present in any tracks, returns an empty array
    public byte[] synthesizeWaveform(AudioFormat format) {
        return new byte[0]; //stub
    }

}
