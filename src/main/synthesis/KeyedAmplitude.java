package synthesis;

import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

public class KeyedAmplitude extends KeyedElement implements AmplitudeModulator {

    public void applyAmplitudeProfile(double amplitude, ArrayList<Double> wave, AudioFormat format) {
        int channels = format.getChannels();
        int size = wave.size();
        int frameCount = getFrameCount();

        for (int i = 0; i < size - channels + 1; i += channels) {
            for (int j = 0; j < channels; j++) {
                double sample = wave.get(i + j);
                sample *= amplitude * lerpAt(i / (double)(size - channels + 1) * frameCount);
                wave.set(i + j, sample);
            }
        }
    }

    @Override
    public String toString() {
        return "Keyframed Amplitude Modulator: Frames - " + frames.size();
    }

}
