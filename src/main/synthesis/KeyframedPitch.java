package synthesis;

// modulates pitch by interpolating between a set of stored keyframes
public class KeyframedPitch implements PitchModulator {

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    //          and the current time as a ratio of the total clip duration.
    public double getPeriodAtTime(double time, float sampleRate) {
        return 0; //stub
    }

}
