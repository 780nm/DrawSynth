package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import synthesis.ConstantPitch;
import synthesis.EnvelopeAmplitude;
import synthesis.Instrument;
import synthesis.SinusoidInstrument;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class NoteTest {

    private EnvelopeAmplitude ampProfile;
    private ConstantPitch pitch;
    private AudioFormat format;
    private Note note;

    @BeforeEach
    public void initTests() {
        ampProfile = new EnvelopeAmplitude(1,0.25,0, 0.5);
        pitch = new ConstantPitch(600);
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testGetSet() {
        note = new Note(null, null, 0);

        Assertions.assertNull(note.getAmplitude());
        Assertions.assertNull(note.getPitch());
        Assertions.assertEquals(0, note.getDuration());

        note.setAmplitude(ampProfile);
        note.setPitch(pitch);
        note.setDuration(5);

        Assertions.assertEquals(ampProfile, note.getAmplitude());
        Assertions.assertEquals(pitch, note.getPitch());
        Assertions.assertEquals(5, note.getDuration());
    }

    @Test
    public void testSynthesizeSample() {
        note = new Note(ampProfile, pitch, 5);
        Instrument sine = new SinusoidInstrument();
        double[] expected1 = {0,0,1119.1115355996615,2238.223071199323,2787.562296387709,5575.124592775418,
                4155.876702773368,8311.753405546737,5493.83939527089,10987.67879054178};
        double[] expected2 = {0,0,560.0672955094373,560.0672955094373,1398.889419499577,1398.889419499577,
                2095.1394170389462,2095.1394170389462,2787.562296387709,2787.562296387709};

        double[] wave1 = note.synthesizeSample(format, sine);
        pitch.setPitch(300);
        ampProfile.setAmplitude(0.5);
        ampProfile.setBalance(0);
        double[] wave2 = note.synthesizeSample(format, sine);

        for (int i = 0; i < 10; i++){
            Assertions.assertEquals(expected1[i], wave1[i]);
            Assertions.assertEquals(expected2[i], wave2[i]);
        }

    }

}
