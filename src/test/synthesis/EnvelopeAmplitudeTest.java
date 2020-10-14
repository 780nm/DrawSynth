package synthesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class EnvelopeAmplitudeTest {

    private EnvelopeAmplitude amplitudeOne;
    private EnvelopeAmplitude amplitudeTwo;
    private double[] wave;
    private AudioFormat format;

    @BeforeEach
    public void initTests() {
        amplitudeOne = new EnvelopeAmplitude(1,0,0, 0.5);
        wave = new double[10];
        Arrays.fill(wave, 1);
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testGetSet() {
        Assertions.assertEquals(1, amplitudeOne.getAmplitude());
        Assertions.assertEquals(0, amplitudeOne.getAttack());
        Assertions.assertEquals(0, amplitudeOne.getDecay());
        Assertions.assertEquals(0.5, amplitudeOne.getBalance());

        amplitudeOne.setAmplitude(0.2);
        amplitudeOne.setAttack(0.05);
        amplitudeOne.setDecay(0.55);
        amplitudeOne.setBalance(0.1);

        Assertions.assertEquals(0.2, amplitudeOne.getAmplitude());
        Assertions.assertEquals(0.05, amplitudeOne.getAttack());
        Assertions.assertEquals(0.55, amplitudeOne.getDecay());
        Assertions.assertEquals(0.1, amplitudeOne.getBalance());
    }

    @Test
    public void testApplyAmplitudeProfileOne() {
        double[] expected = {0.5,1,0.5,1,0.5,1,0.5,1,0.5,1};
        amplitudeOne.applyAmplitudeProfile(wave, format);

        for (int i = 0; i < 10; i++){
            Assertions.assertEquals(expected[i], wave[i]);
        }
    }

    @Test
    public void testApplyAmplitudeProfileTwo() {
        double[] expected = {0,0,0.5,0.5,0.5,0.5,0.4,0.4,0.2,0.2};
        amplitudeTwo = new EnvelopeAmplitude(0.5, 0.1, 0.5, 0);
        amplitudeTwo.applyAmplitudeProfile(wave, format);

        for (int i = 0; i < 10; i++){
            Assertions.assertEquals(expected[i], wave[i]);
        }

    }

    @Test
    public void testApplyAmplitudeProfileMono() {
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 1, 4, 44100, false);

        amplitudeOne.applyAmplitudeProfile(wave, format);

        for (int i = 0; i < 10; i++){
            Assertions.assertEquals(1, wave[i]);
        }
    }

}
