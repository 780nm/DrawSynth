package model;

import exceptions.NoteIntersectionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import synthesis.*;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.util.UUID;

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

        ampProfile = new EnvelopeAmplitude(0.25,0, 0);
        pitch = new ConstantPitch();
        note = new Note(1, ampProfile, 600, pitch, 3);
        note2 = new Note(1, ampProfile, 600, pitch, 3);

        instrument = new SinusoidInstrument();

        track = new Track(instrument);
    }

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        track = new Track(instrument, id);
        assertEquals(id, track.getUuid());
    }

    @Test
    public void testGetSetInstrument() {
        assertEquals(instrument, track.getInstrument());
        Instrument sine2 = new SinusoidInstrument();
        track.setInstrument(sine2);
        assertEquals(sine2, track.getInstrument());
    }

    @Test
    public void testAddNoteNoOverlap() {
        assertTrue(track.getNotes().isEmpty());

        try {
            assertFalse(track.addNote(0, note));
            assertTrue(track.addNote(0, note2));
        } catch (NoteIntersectionException exception) {
            fail("Note intersection exception thrown erroneously");
        }

        assertEquals(1, track.getNotes().size());
        assertEquals(note2, track.getNotes().get(0));

        try {
            assertFalse(track.addNote(3, note));
        } catch (NoteIntersectionException exception) {
            fail("Note intersection exception thrown erroneously");
        }

        assertEquals(2, track.getNotes().size());
        assertEquals(note2, track.getNotes().get(0));
        assertEquals(note, track.getNotes().get(3));
    }

    @Test
    public void testAddNoteOverlap() {
        try {
            assertFalse(track.addNote(0, note));
        } catch (NoteIntersectionException exception) {
            fail("Note intersection exception thrown erroneously");
        }

        try {
            track.addNote(2, note2);
            fail("Note intersection exception not thrown");
        } catch (NoteIntersectionException exception) {
            // expected
        }
    }

    @Test
    public void testRemoveNote() {
        assertTrue(track.getNotes().isEmpty());

        try {
            assertFalse(track.addNote(0, note));
            track.addNote(0, note2);
            track.addNote(6, note);
        } catch (NoteIntersectionException exception) {
            fail("Note intersection exception thrown erroneously");
        }

        assertTrue(track.removeNote(0));

        assertEquals(1, track.getNotes().size());
        assertEquals(note, track.getNotes().get(6));

        assertFalse(track.removeNote(0));

        assertEquals(1, track.getNotes().size());
        assertEquals(note, track.getNotes().get(6));
    }

    @Test
    public void testSynthesizeClip() {
        byte[] expected = {0,0,0,0,-19,10,-19,10,-57,21,-57,21,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-19,10,-19,10,-57,21,-57,21};

        try {
            track.addNote(3, note);
            track.addNote(0, note2);
        } catch (NoteIntersectionException exception) {
            fail("Note intersection exception thrown erroneously");
        }

        byte[] wave = track.synthesizeClip(format);

        for (int i = 0; i < wave.length; i++){
            assertEquals(expected[i], wave[i]);
        }

    }

}
