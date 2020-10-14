package synthesis;

import javax.sound.sampled.AudioFormat;

public class SinusoidInstrument implements Instrument {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, pitch is not null
    // EFFECTS: Return a double array representation of the audio source in the given format,
    //          as a function of the given pitch modulator and duration
    public double[] synthesizeSample(PitchModulator pitch, int duration, AudioFormat format) {
        int channels = format.getChannels();
        double[] wave = new double[duration * 2];
        double scale = Math.pow(2, format.getSampleSizeInBits() - 1);
        int finalIndex = duration * channels - channels + 1;

        double period;
        double sample;

        for (int i = 0; i < finalIndex; i += channels) {
            period = pitch.getPeriodAtTime(i / (double)(finalIndex), format.getSampleRate());
            sample = Math.sin(i * Math.PI / period) * scale;
            for (int j = 0; j < channels; j++) {
                wave[i + j] = sample;
            }
        }

        return wave;
    }

}
