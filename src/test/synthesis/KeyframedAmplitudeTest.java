package synthesis;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class KeyframedAmplitudeTest {

    @Test
    public void testStub() {
        double[] wave = new double[0];
        AudioFormat format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        KeyframedAmplitude ampProfile = new KeyframedAmplitude();
        ampProfile.applyAmplitudeProfile(wave, format);
    }

}
