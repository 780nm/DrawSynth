package synthesis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.ArrayList;
import java.util.UUID;

public class SinusoidInstrumentTest {

    private static final double DELTA = 0.01;

    private AudioFormat format;
    private ConstantPitch pitch;
    private SinusoidInstrument sine;

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        sine = new SinusoidInstrument(id);
        assertEquals(id, sine.getUuid());
    }

    @Test
    public void testSynthesizeWaveform() {
        sine = new SinusoidInstrument();
        pitch = new ConstantPitch();
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        double[] expected1 = {0,0, 1400.16, 1400.16,2797.77,2797.77};
        double[] expected2 = {0,0, 24722.66, 24722.66,32452.34,32452.34};

        ArrayList<Double> wave1 = sine.synthesizeWaveform(300, pitch, 3, format);
        ArrayList<Double> wave2 = sine.synthesizeWaveform(6000, pitch, 3, format);

        for (int i = 0; i < 6; i++) {
            assertEquals(expected1[i], wave1.get(i), DELTA);
            assertEquals(expected2[i], wave2.get(i), DELTA);
        }
    }

}
