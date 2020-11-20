package synthesis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyedPitchTest {

    private static final double DELTA = 0.01;

    private KeyedPitch mod;
    private KeyedInstrument instr;

    @BeforeEach
    public void initTests() {
        instr = new KeyedInstrument();
        instr.addFrame(0.);
        instr.addFrame(1.);
        instr.addFrame(-1.);

        mod = new KeyedPitch();
        mod.addFrame(1.);
        mod.addFrame(0.7);
        mod.addFrame(1.3);
    }

    @Test
    public void testGetPeriodAtTime() {
        assertEquals(73.5, mod.getPeriodAtTime(600, 0., 44100), DELTA);
        assertEquals(56.54, mod.getPeriodAtTime(600, 2, 44100), DELTA);
        assertEquals(67.85, mod.getPeriodAtTime(500, 2, 44100), DELTA);
        assertEquals(42.40, mod.getPeriodAtTime(800, 10, 44100), DELTA);
    }

    @Test
    public void testToString() {
        assertEquals("Keyframed Pitch Modulator: Frames - 3", mod.toString());
    }

}
