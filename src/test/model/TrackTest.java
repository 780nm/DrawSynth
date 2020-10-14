package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import synthesis.*;

import javax.sound.sampled.AudioFormat;

import java.io.IOException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.junit.jupiter.api.Assertions.fail;

public class TrackTest {

    private EnvelopeAmplitude ampProfile;
    private ConstantPitch pitch;
    private AudioFormat format;

    private Note note;
    private Note note2;
    private Instrument instrument;

    private Track track;

    @BeforeEach
    public void initTests() {
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        ampProfile = new EnvelopeAmplitude(1,0.25,0, 0);
        pitch = new ConstantPitch(600);
        note = new Note(ampProfile, pitch, 3);
        note2 = new Note(ampProfile, pitch, 3);

        instrument = new SinusoidInstrument();

        track = new Track(instrument);
    }

    @Test
    public void testGetSetInstrument() {
        Assertions.assertEquals(instrument, track.getInstrument());
        Instrument sine2 = new SinusoidInstrument();
        track.setInstrument(sine2);
        Assertions.assertEquals(sine2, track.getInstrument());
    }

    @Test
    public void testAddNote() {
        Assertions.assertTrue(track.getNotes().isEmpty());
        Assertions.assertFalse(track.addNote(0, note));
        Assertions.assertTrue(track.addNote(0, note2));

        Assertions.assertEquals(1, track.getNotes().size());
        Assertions.assertEquals(note2, track.getNotes().get(0));

        Assertions.assertFalse(track.addNote(6, note));

        Assertions.assertEquals(2, track.getNotes().size());
        Assertions.assertEquals(note2, track.getNotes().get(0));
        Assertions.assertEquals(note, track.getNotes().get(6));
    }

    @Test
    public void testRemoveNote() {
        Assertions.assertTrue(track.getNotes().isEmpty());
        Assertions.assertFalse(track.addNote(0, note));
        track.addNote(0, note2);
        track.addNote(6, note);

        Assertions.assertTrue(track.removeNote(0));

        Assertions.assertEquals(1, track.getNotes().size());
        Assertions.assertEquals(note, track.getNotes().get(6));

        Assertions.assertFalse(track.removeNote(0));

        Assertions.assertEquals(1, track.getNotes().size());
        Assertions.assertEquals(note, track.getNotes().get(6));
    }

    @Test
    public void testSynthesizeWaveform() {
        byte[] expected = {0,0,0,0,-19,10,-19,10,-57,21,-57,21,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-19,10,-19,10,-57,21,-57,21};
        byte[] wave = new byte[0];

        track.addNote(6, note);
        track.addNote(0, note2);

        try {
            wave = track.synthesizeWaveform(format);
        } catch (IOException exception) {
            fail();
        }

        for (int i = 0; i < wave.length; i++){
            Assertions.assertEquals(expected[i], wave[i]);
        }

    }

}
