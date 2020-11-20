package synthesis;

import persistence.Persistent;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

// Objects implementing the Instrument interface may upon request generate an audio clip as a double array
public interface Instrument extends Persistent {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, and pitchMod is not null
    // EFFECTS: Return a Double ArrayList representation of the audio source in the given format
    ArrayList<Double> synthesizeWaveform(double basePitch, PitchModulator pitchMod, int duration, AudioFormat format);

}
