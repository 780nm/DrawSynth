package synthesis;

import org.json.JSONObject;

import java.util.UUID;

// Unchanging PitchModulator
public class ConstantPitch implements PitchModulator {

    private UUID pitchModId;

    // EFFECTS: Constructs a constant pitch modulator
    public ConstantPitch() {
        pitchModId = UUID.randomUUID();
    }

    // EFFECTS: Constructs a constant pitch modulator with given UUID
    public ConstantPitch(UUID id) {
        pitchModId = id;
    }

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    public double getPeriodAtTime(double basePitch, double time, float sampleRate) {
        return sampleRate / basePitch;
    }

    // EFFECTS: Returns the state of the Object as a serialized JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", pitchModId.toString());
        json.put("class", this.getClass().getName());
        return json;
    }

    // EFFECTS: Returns the state of the object as a formatted string
    @Override
    public String toString() {
        return "Constant Pitch Modulator";
    }

    // Getters

    public UUID getUuid() {
        return pitchModId;
    }

}
