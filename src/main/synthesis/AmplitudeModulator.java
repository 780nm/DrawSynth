package synthesis;

import persistence.Persistent;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

//Manipulates the amplitude of a synthesized waveform
public interface AmplitudeModulator extends Persistent {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // EFFECTS: Manipulates the given byte array as per the associated amplitude specification
    void applyAmplitudeProfile(double baseAmplitude, ArrayList<Double> wave, AudioFormat format);
}
