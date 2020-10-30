package synthesis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.ArrayList;

public class SampleUtilTest {

    AudioFormat format;

    @BeforeEach
    public void initTests() {
        SampleUtil util = new SampleUtil(); // Note that all methods are static
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testDownMix() {
        format = new AudioFormat(PCM_SIGNED, 44100, 4, 2, 4, 44100, false);
        double[] sampleWave = {0,0,1000,1000,0,0,-1000,-1000,0,0};
        double[] sampleWave2 = {0, 0,0,1000,1000,0,0,-1000,-1000,0};
        ArrayList<Double> sample = new ArrayList<>();
        ArrayList<Double> sample2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sample.add(sampleWave[i]);
            sample2.add(sampleWave2[i]);
        }
        ArrayList<ArrayList<Double>> samples = new ArrayList<>();
        samples.add(sample);
        samples.add(sample2);

        double[] expected = {0.0, 0.0, 4.0, 8.0, 4.0, 0.0, -4.0, -8.0, -4.0, 0.0};

        ArrayList<Double> encodedSample = SampleUtil.downMix(samples, format);

        for (int i = 0; i < 10; i++){
            assertEquals(expected[i], encodedSample.get(i));
        }
    }

    @Test
    public void testEncodeBytesEmpty() {
        ArrayList<Double> emptyWave = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            emptyWave.add(0.0);
        }

        byte[] encodedSample = SampleUtil.encodeBytes(emptyWave, format);

        for (int i = 0; i < 20; i++){
            assertEquals(0, encodedSample[i]);
        }

    }

    @Test
    public void testEncodeBytesSample() {
        double[] sampleWave = {0,0,1000,1000,0,0,-1000,-1000,0,0};
        ArrayList<Double> sample = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sample.add(sampleWave[i]);
        }

        byte[] expected = {0,0,0,0,-24,3,-24,3,0,0,0,0,24,-4,24,-4,0,0,0,0};
        byte[] encodedSample = SampleUtil.encodeBytes(sample, format);

        for (int i = 0; i < 20; i++){
            assertEquals(expected[i], encodedSample[i]);
        }

    }

}
