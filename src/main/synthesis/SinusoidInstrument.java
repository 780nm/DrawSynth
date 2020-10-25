package synthesis;

import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

public class SinusoidInstrument implements Instrument {

    private final UUID INSTR_ID;

    public SinusoidInstrument() {
        INSTR_ID = UUID.randomUUID();
    }

    public SinusoidInstrument(UUID id) {
        INSTR_ID = id;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, pitch is not null
    // EFFECTS: Return a double array representation of the audio source in the given format,
    //          as a function of the given pitch modulator and duration
    public ArrayList<Double> synthesizeWaveform(double basePitch, PitchModulator pitchMod, int duration, AudioFormat format) {
        int channels = format.getChannels();
        ArrayList<Double> wave = new ArrayList<Double>(duration * format.getChannels());
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

        assert (wave.size() == duration * format.getChannels());

        return wave;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", INSTR_ID.toString());
        return json;
    }

    public UUID getUuid() {
        return INSTR_ID;
    }

}
