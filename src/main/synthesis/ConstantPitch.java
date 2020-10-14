package synthesis;

// Unchanging PitchModulator
public class ConstantPitch implements PitchModulator {

    double pitch;

    // REQUIRES: pitch > 0
    // EFFECTS: Constructs a ConstantPitch object with the given pitch
    public ConstantPitch(double pitch) {
        this.pitch = pitch;
    }

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    public double getPeriodAtTime(double time, float sampleRate) {
        return sampleRate / pitch;
    }

    // Getters and Setters

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

}
