package synthesis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.ArrayList;
import java.util.UUID;

public class EnvelopeAmplitudeTest {

    private EnvelopeAmplitude amplitudeOne;
    private EnvelopeAmplitude amplitudeTwo;
    private ArrayList<Double> wave;
    private AudioFormat format;

    @BeforeEach
    public void initTests() {
        amplitudeOne = new EnvelopeAmplitude(0, 0, 0.5);

        wave = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            wave.add(1.0);
        }

        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        amplitudeTwo = new EnvelopeAmplitude(0, 0, 0.5,id);
        assertEquals(id, amplitudeTwo.getUuid());
    }

    @Test
    public void testGetSet() {
        assertEquals(0, amplitudeOne.getAttack());
        assertEquals(0, amplitudeOne.getDecay());
        assertEquals(0.5, amplitudeOne.getBalance());

        amplitudeOne.setAttack(0.05);
        amplitudeOne.setDecay(0.55);
        amplitudeOne.setBalance(0.1);

        assertEquals(0.05, amplitudeOne.getAttack());
        assertEquals(0.55, amplitudeOne.getDecay());
        assertEquals(0.1, amplitudeOne.getBalance());
    }

    @Test
    public void testApplyAmplitudeProfileOne() {
        double[]  expected = {0.5,1,0.5,1,0.5,1,0.5,1,0.5,1};
        amplitudeOne.applyAmplitudeProfile(1, wave, format);

        for (int i = 0; i < 10; i++){
            assertEquals(expected[i], wave.get(i));
        }
    }

    @Test
    public void testApplyAmplitudeProfileTwo() {
        double[] expected = {0,0,0.5,0.5,0.5,0.5,0.4,0.4,0.2,0.2};
        amplitudeTwo = new EnvelopeAmplitude(0.1, 0.5, 0);
        amplitudeTwo.applyAmplitudeProfile(0.5, wave, format);

        for (int i = 0; i < 10; i++){
            assertEquals(expected[i], wave.get(i));
        }

    }

    @Test
    public void testApplyAmplitudeProfileMono() {
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 1, 4, 44100, false);

        amplitudeOne.applyAmplitudeProfile(1, wave, format);

        for (int i = 0; i < 10; i++){
            assertEquals(1, wave.get(i));
        }
    }

}
