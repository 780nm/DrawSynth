package model;

import exceptions.ElementNotFoundException;
import exceptions.NoteIntersectionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import synthesis.*;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SequencerTest {

    private AudioFormat format;
    private Sequencer sequencer;

    private EnvelopeAmplitude ampProfile;
    private EnvelopeAmplitude ampProfile2;
    private ConstantPitch pitchProfile;
    private Instrument instrument;

    @BeforeEach
    public void initTests() {
        format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        sequencer = new Sequencer();

        ampProfile = new EnvelopeAmplitude(0.25,0, 0);
        ampProfile2 = new EnvelopeAmplitude(0.25,0, 0);
        pitchProfile = new ConstantPitch();
        instrument = new SinusoidInstrument();

        sequencer.addAmpMod(ampProfile);
        sequencer.addAmpMod(ampProfile);
        sequencer.addPitchMod(pitchProfile);
        sequencer.addInstrument(instrument);
    }

    @Test
    public void testCustomUuid() {
        UUID id = UUID.randomUUID();
        sequencer = new Sequencer(id);
        assertEquals(id, sequencer.getUuid());
    }

    @Test
    public void testAddAmpMod() {
        Sequencer seq= new Sequencer();
        assertEquals(0, seq.addAmpMod(ampProfile));
        ArrayList<AmplitudeModulator> ampMods = seq.getAmpMods();
        assertEquals(1, ampMods.size());
        assertEquals(ampProfile, ampMods.get(0));
        assertEquals(1, seq.addAmpMod(ampProfile2));
        assertEquals(2, ampMods.size());
        assertEquals(ampProfile, ampMods.get(0));
        assertEquals(ampProfile2, ampMods.get(1));
    }

    @Test
    public void testAddPitchMod() {
        Sequencer seq = new Sequencer();
        assertEquals(0, seq.addPitchMod(pitchProfile));
        ArrayList<PitchModulator> pitchMods = seq.getPitchMods();
        assertEquals(1, pitchMods.size());
        assertEquals(pitchProfile, pitchMods.get(0));
        assertEquals(1, seq.addPitchMod(pitchProfile));
        assertEquals(2, pitchMods.size());
        assertEquals(pitchProfile, pitchMods.get(1));
    }

    @Test
    public void testAddInstrument() {
        Sequencer seq = new Sequencer();
        assertEquals(0, seq.addInstrument(instrument));
        ArrayList<Instrument> instruments = seq.getInstruments();
        assertEquals(1, instruments.size());
        assertEquals(instrument, instruments.get(0));
        assertEquals(1, seq.addInstrument(instrument));
        assertEquals(2, instruments.size());
        assertEquals(instrument, instruments.get(1));
    }

    @Test
    public void testAddNote() {
        try {
            sequencer.addNote(1, ampProfile.getUuid(), 100, pitchProfile.getUuid(), 10);
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        }

        ArrayList<Note> notes = sequencer.getNotes();

        assertEquals(1, notes.size());

        Note note = notes.get(0);
        assertEquals(1, note.getBaseAmplitude());
        assertEquals(ampProfile, note.getAmpMod());
        assertEquals(100, note.getBasePitch());
        assertEquals(pitchProfile, note.getPitchMod());
        assertEquals(10, note.getDuration());

        try {
            sequencer.addNote(0.5,ampProfile.getUuid(), 200, pitchProfile.getUuid(), 20);
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        }

        assertEquals(2, notes.size());
        note = notes.get(1);
        assertEquals(0.5, note.getBaseAmplitude());
        assertEquals(ampProfile, note.getAmpMod());
        assertEquals(200, note.getBasePitch());
        assertEquals(pitchProfile, note.getPitchMod());
        assertEquals(20, note.getDuration());
    }

    @Test
    public void testAddNoteInvalidUUID() {
        UUID randomID = UUID.randomUUID();
        try {
            sequencer.addNote(1,randomID, 100, randomID, 10);
            fail("ElementNotFoundException not thrown");
        } catch (ElementNotFoundException exception) {
            // expected
        }
    }

    @Test
    public void testAddTrack() {
        try {
            sequencer.addTrack(instrument.getUuid());
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        }

        ArrayList<Track> tracks = sequencer.getTracks();
        assertEquals(1, tracks.size());
        Track track = tracks.get(0);
        assertEquals(instrument, track.getInstrument());
    }

    @Test
    public void testAddTrackInvalidUUID() {
        UUID randomID = UUID.randomUUID();
        try {
            sequencer.addTrack(randomID);
            fail("ElementNotFoundException not thrown");
        } catch (ElementNotFoundException exception) {
            // expected
        }
    }

    @Test
    public void testInsertNote() {
        try {
            sequencer.addNote(1, ampProfile.getUuid(), 600, pitchProfile.getUuid(), 3);
            sequencer.addNote(0.5, ampProfile.getUuid(), 300, pitchProfile.getUuid(), 10);
            sequencer.addTrack(instrument.getUuid());

            ArrayList<Note> notes = sequencer.getNotes();
            Track track = sequencer.getTracks().get(0);

            sequencer.insertNote(0, notes.get(0).getUuid(), track.getUuid());
            sequencer.insertNote(3, notes.get(1).getUuid(), track.getUuid());

            Map<Integer, Note> insertedNotes = track.getNotes();
            assertEquals(2, insertedNotes.size());
            assertEquals(notes.get(0), insertedNotes.get(0));
            assertEquals(notes.get(1), insertedNotes.get(3));
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        } catch (NoteIntersectionException exception) {
            fail("NoteIntersectionException thrown erroneously");
        }
    }

    @Test
    public void testInsertNoteInvalidUUID(){
        try {
            UUID id = UUID.randomUUID();
            sequencer.insertNote(0, id, id);
            fail("ElementNotFoundException not thrown");
        } catch (ElementNotFoundException exception) {
            // expected
        } catch (NoteIntersectionException exception) {
            fail("NoteIntersectionException thrown erroneously");
        }
    }

    @Test
    public void testInsertNoteOverlap() {
        try {
            sequencer.addNote(1, ampProfile.getUuid(), 600, pitchProfile.getUuid(), 3);
            sequencer.addNote(0.5, ampProfile.getUuid(), 300, pitchProfile.getUuid(), 10);
            sequencer.addTrack(instrument.getUuid());

            ArrayList<Note> notes = sequencer.getNotes();
            UUID trackID = sequencer.getTracks().get(0).getUuid();

            sequencer.insertNote(0, notes.get(1).getUuid(), trackID);
            sequencer.insertNote(3, notes.get(0).getUuid(), trackID);
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        } catch (NoteIntersectionException exception) {
            //Expected
        }
    }

    @Test
    public void testSynthesizeClip() {
        try {
            sequencer.addNote(1, ampProfile.getUuid(), 600, pitchProfile.getUuid(), 3);
            sequencer.addNote(0.5, ampProfile.getUuid(), 300, pitchProfile.getUuid(), 2);
            sequencer.addTrack(instrument.getUuid());
            sequencer.addTrack(instrument.getUuid());

            ArrayList<Note> notes = sequencer.getNotes();
            ArrayList<Track> tracks = sequencer.getTracks();

            sequencer.insertNote(0, notes.get(0).getUuid(), tracks.get(0).getUuid());
            sequencer.insertNote(3, notes.get(1).getUuid(), tracks.get(0).getUuid());
            sequencer.insertNote(0, notes.get(1).getUuid(), tracks.get(1).getUuid());
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        } catch (NoteIntersectionException exception) {
            fail("NoteIntersectionException thrown erroneously");
        }

        byte[] expected = {0,0,0,0,-87,13,-87,13,-57,21,-57,21,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-68,2,-68,2};
        byte[] wave = sequencer.synthesizeClip(format);

        for (int i = 0; i < wave.length; i++){
            assertEquals(expected[i], wave[i]);
        }

    }

}
