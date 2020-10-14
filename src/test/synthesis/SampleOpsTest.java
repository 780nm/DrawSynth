package synthesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.Arrays;

public class SampleOpsTest {

    double[] emptyWave;
    double[] sampleWave;
    private AudioFormat format;

    @BeforeEach
    public void initTests() {
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        emptyWave = new double[10];
        Arrays.fill(emptyWave, 0);
        sampleWave = new double[]{0,0,1000,1000,0,0,-1000,-1000,0,0};

    }

    @Test
    public void testEncodeBytesEmpty() {
        byte[] encodedSample = SampleOps.encodeBytes(emptyWave, format);

        for (int i = 0; i < 20; i++){
            Assertions.assertEquals(0, encodedSample[i]);
        }

    }

    @Test
    public void testEncodeBytesSample() {
        byte[] expected = {0,0,0,0,-24,3,-24,3,0,0,0,0,24,-4,24,-4,0,0,0,0};
        byte[] encodedSample = SampleOps.encodeBytes(sampleWave, format);

        for (int i = 0; i < 20; i++){
            Assertions.assertEquals(expected[i], encodedSample[i]);
        }

    }

}
