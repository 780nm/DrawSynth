package synthesis;

import java.util.UUID;

// Represents a pitch modulator with a drawn pitch curve
public class KeyedPitch extends KeyedElement implements PitchModulator {

    // EFFECTS: Constructs a keyframed pitch modulator
    public KeyedPitch() {
        super();
    }

    // EFFECTS: Constructs a keyframed pitch modulator with the given UUID
    public KeyedPitch(UUID id) {
        super(id);
    }

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    public double getPeriodAtTime(double basePitch, double time, float sampleRate) {
        return sampleRate / (basePitch * lerpAt(time * frames.size()));
    }

    // EFFECTS: Returns the state of the object as a formatted string
    public String toString() {
        return "Keyframed Pitch Modulator: Frames - " + frames.size();
    }

}
