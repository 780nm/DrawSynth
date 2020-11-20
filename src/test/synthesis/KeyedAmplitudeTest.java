package synthesis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyedAmplitudeTest {

    private static final double DELTA = 0.01;

    private ArrayList<Double> wave;
    private AudioFormat format;
    private KeyedAmplitude mod;

    @BeforeEach
    public void initTests() {
        mod = new KeyedAmplitude();
        mod.addFrame(0.);
        mod.addFrame(0.7);
        mod.addFrame(1.);
        mod.addFrame(0.3);
        mod.addFrame(0.);

        wave = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            wave.add(1.0);
        }

        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testApplyAmplitudeProfile() {
        double[]  expected = {0.0,0.0,0.66,0.66,0.32,0.32,0.0,0.0};
        mod.applyAmplitudeProfile(.8, wave, format);

        for (int i = 0; i < 8; i++){
            assertEquals(expected[i], wave.get(i), DELTA);
        }
    }

    @Test
    public void testToString() {
        assertEquals("Keyframed Amplitude Modulator: Frames - 5", mod.toString());
    }

}
