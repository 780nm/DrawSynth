package synthesis;

import javax.sound.sampled.AudioFormat;

//generates waveform by interpolating between a set of stored keyframes
public class KeyframedInstrument implements Instrument {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, pitch is not null
    // EFFECTS: Return a double array representation of the audio source in the given format
    public double[] synthesizeSample(PitchModulator pitch, int duration, AudioFormat format) {
        return new double[0]; //Stub
    }

}
