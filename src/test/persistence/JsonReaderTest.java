package persistence;

import exceptions.ElementNotFoundException;
import exceptions.GeneratorException;
import exceptions.NoteIntersectionException;
import model.Sequencer;
import org.junit.jupiter.api.Test;
import synthesis.*;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static persistence.PersistenceTestUtil.evalFileEquality;

public class JsonReaderTest {

    @Test
    public void testReadJSONTypical() {
        try {
            JsonReader reader = new JsonReader("./data/sample.json");
            Sequencer seq = reader.getSequencer();
            JsonWriter writer = new JsonWriter("./data/temp.json");
            writer.write(seq);

            try {
                assertTrue(evalFileEquality("./data/sample.json", "./data/temp.json"));
            } catch (IOException e) {
                fail("IOException throws erroneously:" + e);
            }
        } catch (Exception e) {
            fail("Exception thrown erroneously: " + e);
        }
    }

    @Test
    public void testReadJSONNoClass() {
        String[] files = {"test-noAmpClass.json","test-noPitchClass.json","test-noInstrumentClass.json"};

        for (String file : files) {
            try {
                JsonReader reader = new JsonReader("./data/" + file);
                fail("GeneratorExceptionNotThrown");
            } catch (GeneratorException e) {
                // Expected
            } catch (IOException e) {
                fail("Exception thrown erroneously: " + e);
            }
        }
    }

}
