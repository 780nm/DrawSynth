package synthesis;

import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

// Represents an instrument with a sinusoidal waveform
public class SinusoidInstrument implements Instrument {

    private UUID instrumentID;

    // EFFECTS: Constructs a sinusoidal instrument
    public SinusoidInstrument() {
        instrumentID = UUID.randomUUID();
    }

    // EFFECTS: Constructs a sinusoidal instrument with the given UUID
    public SinusoidInstrument(UUID id) {
        instrumentID = id;
    }

    // REQUIRES: format has a valid configuration and encoding is PCM_SIGNED, pitch is not null
    // EFFECTS: Return a Double ArrayList representation of the audio source in the given format,
    //          as a function of the given pitch modulator and duration
    public ArrayList<Double> synthesizeWaveform(double basePitch, PitchModulator pitchMod,
                                                int duration, AudioFormat format) {
        int channels = format.getChannels();
        ArrayList<Double> wave = new ArrayList<>(duration * format.getChannels());
        double scale = Math.pow(2, format.getSampleSizeInBits() - 1);
        int finalIndex = duration * channels - channels + 1;

        double period;
        double sample;

        for (int i = 0; i < finalIndex; i += channels) {
            period = pitchMod.getPeriodAtTime(basePitch,i / (double)(finalIndex), format.getSampleRate());
            sample = Math.sin(i * Math.PI / period) * scale;
            for (int j = 0; j < channels; j++) {
                wave.add(sample);
            }
        }

        return wave;
    }

    // EFFECTS: Returns the state of the Object as a serialized JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", instrumentID.toString());
        json.put("class", this.getClass().getName());
        return json;
    }

    // EFFECTS: Returns the state of the object as a formatted string
    @Override
    public String toString() {
        return "Sinusoid Instrument";
    }

    // Getters
    public UUID getUuid() {
        return instrumentID;
    }

}
