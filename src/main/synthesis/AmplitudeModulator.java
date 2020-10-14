package synthesis;

import javax.sound.sampled.AudioFormat;

//Manipulates the amplitude of a synthesized waveform
public interface AmplitudeModulator {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // EFFECTS: Manipulates the given byte array as per the associated amplitude specification
    void applyAmplitudeProfile(double[] wave, AudioFormat format);
}
