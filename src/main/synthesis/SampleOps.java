package synthesis;

import javax.sound.sampled.AudioFormat;

public class SampleOps {

    public static byte[] encodeBytes(double[] waveform, AudioFormat format) {
        int bytesPerSample = format.getFrameSize() / format.getChannels();
        byte[] output = new byte[bytesPerSample * waveform.length];

        for (int i = 0; i < waveform.length; i++) {
            int sample = (int) waveform[i];
            for (int j = 0; j < bytesPerSample; j++) {
                output[i * bytesPerSample + j] = (byte) (sample >> (8 * j));
            }
        }

        return output;
    }

}
