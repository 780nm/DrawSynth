package synthesis;

import javax.sound.sampled.AudioFormat;

//Applies an amplitude curve based on a set of stored keyframes
public class KeyframedAmplitude implements AmplitudeModulator {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // MODIFIES: wave
    // EFFECTS: Manipulates the given byte array as per the associated amplitude specification
    public void applyAmplitudeProfile(double[] wave, AudioFormat format) {
        return; //stub
    }

}
