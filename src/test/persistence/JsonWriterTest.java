package persistence;

import exceptions.ElementNotFoundException;
import exceptions.NoteIntersectionException;
import model.Sequencer;
import org.junit.jupiter.api.Test;
import synthesis.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static persistence.PersistenceTestUtil.evalFileEquality;

public class JsonWriterTest {

    @Test
    public void testWriteJSONTypical() {
        Sequencer seq = new Sequencer(UUID.fromString("89c16b13-c8b3-4840-9d0f-e6beab09c5ed"));

        UUID ampProfile1ID = UUID.fromString("9f5b6062-664f-4487-a146-26723f8c4939");
        UUID ampProfile2ID = UUID.fromString("00b78b37-e508-4500-bbbd-51aaa7343d56");
        UUID ampProfile3ID = UUID.fromString("8b1093d4-fa02-4e49-9dbc-42453ec7c5db");
        UUID pitchProfile1ID = UUID.fromString("27831d45-d67e-4e81-978f-46fc45ab732a");
        UUID pitchProfile2ID = UUID.fromString("825c643c-582f-43e3-a7c6-4a68776f3a0f");
        UUID instrumentID = UUID.fromString("923055bd-c8e4-4f40-bb1c-889e435a470f");

        AmplitudeModulator ampProfile1 = new EnvelopeAmplitude(0.1, 0.5, 0.3,  ampProfile1ID);
        AmplitudeModulator ampProfile2 = new EnvelopeAmplitude(0.2, 0.2, 0, ampProfile2ID);
        AmplitudeModulator ampProfile3 = new EnvelopeAmplitude(0.1, 0.5, -0.3, ampProfile3ID);
        PitchModulator pitchProfile1 = new ConstantPitch(pitchProfile1ID);
        PitchModulator pitchProfile2 = new ConstantPitch(pitchProfile2ID);
        Instrument instrument = new SinusoidInstrument(instrumentID);

        seq.addAmpMod(ampProfile1);
        seq.addAmpMod(ampProfile2);
        seq.addAmpMod(ampProfile3);
        seq.addPitchMod(pitchProfile1);
        seq.addPitchMod(pitchProfile2);
        seq.addInstrument(instrument);


        UUID note1ID = UUID.fromString("eebd8764-5eff-46d9-8635-77c7b4db536e");
        UUID note2ID = UUID.fromString("ff199d0d-4833-47f9-aed9-ff9c324c4ba5");
        UUID note3ID = UUID.fromString("1d08e5c2-e161-472c-be05-fa63a8d9bbad");
        try {
            seq.addNote(note1ID, 0.5, ampProfile1ID, 200, pitchProfile1ID, 20000);
            seq.addNote(note2ID, 0.5, ampProfile3ID, 500, pitchProfile2ID, 20000);
            seq.addNote(note3ID, 0.3, ampProfile2ID, 600, pitchProfile1ID, 30000);
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        }

        UUID track1ID = UUID.fromString("b9801e13-3527-4b41-abc3-5cc999616010");
        UUID track2ID = UUID.fromString("e0693b83-9bcf-4e36-97f6-1d88a4ee05cc");
        try {
            seq.addTrack(track1ID, instrumentID);
            seq.addTrack(track2ID, instrumentID);

            seq.insertNote(0, note1ID, track1ID);
            seq.insertNote(30000, note3ID, track1ID);
            seq.insertNote(0, note2ID, track2ID);
        } catch (ElementNotFoundException exception) {
            fail("ElementNotFoundException thrown erroneously");
        } catch (NoteIntersectionException exception) {
            fail("NoteIntersectionException thrown erroneously");
        }

        JsonWriter writer = new JsonWriter("./data/temp.json");
        try {
            writer.write(seq);
        } catch (FileNotFoundException exception) {
           fail("FileNotFoundException thrown erroneously");
        }

        try {
            assertTrue(evalFileEquality("./data/sample.json", "./data/temp.json"));
        } catch (IOException e) {
            fail("IOException throws erroneously:" + e);
        }

    }

    // Lifted from JsonSerializationDemo project
    @Test
    public void testWriterInvalidFile() {
        try {
            Sequencer seq = new Sequencer();
            JsonWriter writer = new JsonWriter("my\0illegal:fileName.json");
            writer.write(seq);
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

}
