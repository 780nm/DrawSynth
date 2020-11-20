package synthesis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class ConstantPitchTest {
    ConstantPitch pitch;

    @Test
    public void testConstructor() {
        pitch = new ConstantPitch();
    }

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        pitch = new ConstantPitch(id);
        assertEquals(id, pitch.getUuid());
    }

    @Test
    public void testToString() {
        pitch = new ConstantPitch();
        assertEquals("Constant Pitch Modulator", pitch.toString());
    }

}
