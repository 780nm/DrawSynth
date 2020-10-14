package synthesis;

import javax.sound.sampled.AudioFormat;

// Objects implementing the Instrument interface may upon request generate an audio clip as a double array
public interface Instrument {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, pitch is not null
    // EFFECTS: Return a double array representation of the audio source in the given format
    double[] synthesizeSample(PitchModulator pitch, int duration, AudioFormat format);

}
