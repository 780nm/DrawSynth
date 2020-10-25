package synthesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SinusoidInstrumentTest {

    private AudioFormat format;
    private ConstantPitch pitch;
    private SinusoidInstrument sine;

    @BeforeEach
    public void initTests() {
        sine = new SinusoidInstrument();
        pitch = new ConstantPitch(300);
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testsynthesizeWaveform() {
        ArrayList<Double> expected1 = {0,0, 1400.168238773593, 1400.168238773593,2797.778838999154,2797.778838999154};
        ArrayList<Double> expected2 = {0,0, 24722.66468297325, 24722.66468297325,32452.344594262853,32452.344594262853};

        ArrayList<Double> wave1 = sine.synthesizeWaveform(pitch, 3, format);
        pitch.setPitch(6000);
        ArrayList<Double> wave2 = sine.synthesizeWaveform(pitch, 3, format);

        for (int i = 0; i < 6; i++) {
            Assertions.assertEquals(expected1[i], wave1[i]);
            Assertions.assertEquals(expected2[i], wave2[i]);
        }
    }

}
