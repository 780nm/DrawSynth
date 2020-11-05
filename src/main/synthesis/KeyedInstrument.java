package synthesis;

import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

public class KeyedInstrument extends KeyedElement implements Instrument {

    public ArrayList<Double> synthesizeWaveform(double basePitch, PitchModulator pitchMod,
                                                                             int duration, AudioFormat format) {
        int channels = format.getChannels();
        float sampleRate = format.getSampleRate();
        int frameCount = frames.size();
        double scale = Math.pow(2, format.getSampleSizeInBits() - 1);

        ArrayList<Double> output = new ArrayList<>(duration * channels);

        double position = 0;

        while (output.size() / channels < duration) {
            double sample = lerpAt(position % frameCount);
            for (int i = 0; i < channels; i++) {
                output.add(sample);
            }
            double period = pitchMod.getPeriodAtTime(basePitch,
                    output.size() / (double)(channels * duration), sampleRate);
            position += (frameCount / period) * scale;
        }
        return output;
    }

    // EFFECTS: Returns the state of the Object as a serialized JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        // TODO: Implement this
        return json;
    }


}
