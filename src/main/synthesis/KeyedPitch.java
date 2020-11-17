package synthesis;

import org.json.JSONObject;
import persistence.Persistent;

import java.util.UUID;

public class KeyedPitch extends KeyedElement implements PitchModulator {

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    public double getPeriodAtTime(double basePitch, double time, float sampleRate) {
        return sampleRate / basePitch * lerpAt(time * getFrameCount());
    }

    public String toString() {
        return "Keyframed Pitch Modulator: Frames - " + frames.size();
    }

}
