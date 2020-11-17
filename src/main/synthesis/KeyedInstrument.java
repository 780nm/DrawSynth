package synthesis;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

public class KeyedInstrument extends KeyedElement implements Instrument {

    public KeyedInstrument() {
        super();
    }

    public KeyedInstrument(UUID id) {
        super(id);
    }

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
                output.add(sample * scale);
            }
            double period = pitchMod.getPeriodAtTime(basePitch,
                    output.size() / (double)(channels * duration), sampleRate);
            position += (frameCount / period);
        }
        return output;
    }

    @Override
    public String toString() {
        return "Keyframed Instrument: Frames - " + frames.size();
    }

}
