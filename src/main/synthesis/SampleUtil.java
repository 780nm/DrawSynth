package synthesis;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

public abstract class SampleUtil {

    // REQUIRES: waveform generated using the same format given, encoding is PCM_SIGNED
    // EFFECTS: Converts double sample array to a PCM_SIGNED encoded byte array, and returns it
    public static byte[] encodeBytes(ArrayList<Double> waveform, AudioFormat format) {
        int bytesPerSample = format.getFrameSize() / format.getChannels();
        int size = waveform.size();

        byte[] output = new byte[bytesPerSample * size];

        for (int i = 0; i < size; i++) {
            int sample = waveform.get(i).intValue();
            for (int j = 0; j < bytesPerSample; j++) {
                output[i * bytesPerSample + j] = (byte) (sample >> (8 * j));
            }
        }

        return output;
    }

    // REQUIRES: waveforms all synthesized using given format, encoding is PCM_SIGNED
    // EFFECTS: returns a single Double ArrayList representing the composite of the given waveforms
    public static ArrayList<Double> downMix(ArrayList<ArrayList<Double>> waveforms, AudioFormat format) {
        int tracks = waveforms.size();
        int maxLength = 0;
        double maxSample = 0;

        int[] lengths = new int[tracks];
        ArrayList<Double> output = new ArrayList<>();

        for (int i = 0; i < tracks; i++) {
            lengths[i] = waveforms.get(i).size();
            maxLength = Math.max(lengths[i], maxLength);
        }

        for (int i = 0; i < maxLength; i++) {
            double sample = 0;
            for (int j = 0; j < tracks; j++) {
                if (i < lengths[j]) {
                    sample +=  waveforms.get(j).get(i);
                }
            }
            maxSample = Math.max(Math.abs(sample), maxSample);
            output.add(sample);
        }

        applyNormalization(output, maxSample, format);

        return output;
    }

    // MODIFIES: wave
    // EFFECTS: Normalizes wave to given format, with a given maxSample as reference
    private static void applyNormalization(ArrayList<Double> wave, double maxSample, AudioFormat format) {
        double maxVal = Math.pow(2, format.getSampleSizeInBits() - 1);
        if (maxVal < maxSample) {
            double ratio = maxVal / maxSample;
            for (int i = 0; i < wave.size(); i++) {
                wave.set(i, Math.min(maxVal, wave.get(i) * ratio));
            }
        }
    }

}
