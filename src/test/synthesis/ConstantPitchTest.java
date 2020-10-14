package synthesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConstantPitchTest {

    private ConstantPitch pitch;

    @BeforeEach
    public void initTests() {
        pitch = new ConstantPitch(60);
    }

    @Test
    public void testChangePitch() {
        Assertions.assertEquals(60, pitch.getPitch());
        pitch.setPitch(120);
        Assertions.assertEquals(120, pitch.getPitch());
    }

    @Test
    public void testGetPeriodAtTime() {
        final float SAMPLE_RATE = 44100;

        Assertions.assertEquals(SAMPLE_RATE / 60, pitch.getPeriodAtTime(0, SAMPLE_RATE));
        Assertions.assertEquals(SAMPLE_RATE / 60, pitch.getPeriodAtTime(0.5, SAMPLE_RATE));
        Assertions.assertEquals(SAMPLE_RATE / 60, pitch.getPeriodAtTime(1, SAMPLE_RATE));
        pitch.setPitch(120);
        Assertions.assertEquals(SAMPLE_RATE / 120, pitch.getPeriodAtTime(0.5, SAMPLE_RATE));
    }

}
