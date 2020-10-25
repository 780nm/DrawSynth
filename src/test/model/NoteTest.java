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

        Assertions.assertNull(note.getAmpMod());
        Assertions.assertNull(note.getPitchMod());
        Assertions.assertEquals(0, note.getDuration());

        note.setAmpMod(ampProfile);
        note.setPitchMod(pitch);
        note.setDuration(5);

        Assertions.assertEquals(ampProfile, note.getAmpMod());
        Assertions.assertEquals(pitch, note.getPitchMod());
        Assertions.assertEquals(5, note.getDuration());
    }

    @Test
    public void testsynthesizeWaveform() {
        note = new Note(ampProfile, pitch, 5);
        Instrument sine = new SinusoidInstrument();
        ArrayList<Double> expected1 = {0,0,1119.1115355996615,2238.223071199323,2787.562296387709,5575.124592775418,
                4155.876702773368,8311.753405546737,5493.83939527089,10987.67879054178};
        ArrayList<Double> expected2 = {0,0,560.0672955094373,560.0672955094373,1398.889419499577,1398.889419499577,
                2095.1394170389462,2095.1394170389462,2787.562296387709,2787.562296387709};

        ArrayList<Double> wave1 = note.synthesizeWaveform(format, sine);
        pitch.setPitch(300);
        ampProfile.setAmplitude(0.5);
        ampProfile.setBalance(0);
        ArrayList<Double> wave2 = note.synthesizeWaveform(format, sine);

        for (int i = 0; i < 10; i++){
            Assertions.assertEquals(expected1[i], wave1[i]);
            Assertions.assertEquals(expected2[i], wave2[i]);
        }

    }

}
