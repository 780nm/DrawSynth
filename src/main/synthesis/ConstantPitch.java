package synthesis;

import org.json.JSONObject;

import java.util.UUID;

// Unchanging PitchModulator
public class ConstantPitch implements PitchModulator {

    private final UUID PMOD_ID;

    public ConstantPitch() {
        PMOD_ID = UUID.randomUUID();
    }

    public ConstantPitch(UUID id) {
        PMOD_ID = id;
    }

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    public double getPeriodAtTime(double basePitch, double time, float sampleRate) {
        return sampleRate / basePitch;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", PMOD_ID.toString());
        return json;
    }

    public UUID getUuid() {
        return PMOD_ID;
    }

}
