package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import synthesis.ConstantPitch;
import synthesis.EnvelopeAmplitude;
import synthesis.Instrument;
import synthesis.SinusoidInstrument;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.ArrayList;
import java.util.UUID;

public class NoteTest {

    private static final double DELTA = 0.01;

    private EnvelopeAmplitude ampProfile;
    private ConstantPitch pitch;
    private AudioFormat format;
    private Note note;

    @BeforeEach
    public void initTests() {
        ampProfile = new EnvelopeAmplitude(0.25,0, 0.5);
        pitch = new ConstantPitch();
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        note = new Note(1,ampProfile,300,pitch,10,id);
        assertEquals(id, note.getUuid());
    }

    @Test
    public void testGetSet() {
        note = new Note(0, null, 0, null, 0);

        assertEquals(0, note.getBaseAmplitude());
        assertNull(note.getAmpMod());
        assertEquals(0, note.getBasePitch());
        assertNull(note.getPitchMod());
        assertEquals(0, note.getDuration());

        note.setBaseAmplitude(0.5);
        note.setAmpMod(ampProfile);
        note.setBasePitch(300);
        note.setPitchMod(pitch);
        note.setDuration(5);

        assertEquals(0.5, note.getBaseAmplitude());
        assertEquals(ampProfile, note.getAmpMod());
        assertEquals(300, note.getBasePitch());
        assertEquals(pitch, note.getPitchMod());
        assertEquals(5, note.getDuration());
    }

    @Test
    public void testSynthesizeWaveform() {
        note = new Note(1, ampProfile, 600, pitch, 5);
        Instrument sine = new SinusoidInstrument();
        double[] expected1 = {0,0,1119.11,2238.22,2787.56,5575.12,4155.87,8311.75,5493.83,10987.67};
        double[] expected2 = {0,0,560.06,560.06,1398.88,1398.88,2095.13,2095.13,2787.56,2787.56};

        ArrayList<Double> wave1 = note.synthesizeWaveform(format, sine);
        note.setBasePitch(300);
        note.setBaseAmplitude(0.5);
        ampProfile.setBalance(0);
        ArrayList<Double> wave2 = note.synthesizeWaveform(format, sine);

        for (int i = 0; i < 10; i++){
            assertEquals(expected1[i], wave1.get(i), DELTA);
            assertEquals(expected2[i], wave2.get(i), DELTA);
        }

    }

}
