package synthesis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyedInstrumentTest {

    private static final double DELTA = 0.01;

    private KeyedInstrument instr;
    
    @BeforeEach
    public void initTests() {
        instr = new KeyedInstrument();
        instr.addFrame(0.);
        instr.addFrame(1.);
        instr.addFrame(-1.);
    }
    
    @Test
    public void testSynthesizeWaveform() {
        AudioFormat format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        ConstantPitch pitch = new ConstantPitch();

        double[] expected = {0, 0, 22291.16, 22291.16, 9139.38, 9139.38};

        ArrayList<Double> wave = instr.synthesizeWaveform(10000, pitch, 3, format);

        for (int i = 0; i < 6; i++) {
            assertEquals(expected[i], wave.get(i), DELTA);
        }
    }

    @Test
    public void testToString() {
        assertEquals("Keyframed Instrument: Frames - 3", instr.toString());
    }
    
}
