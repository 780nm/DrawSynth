package persistence;

import model.Sequencer;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Writes current model state to a file on disk
public class JsonWriter {

    private static final int INDENT_FACTOR = 4;

    private String fileName;

    // EFFECTS: Initializes writer with given filename
    public JsonWriter(String fileName) {
        this.fileName = fileName;
    }

    // EFFECTS: Initializes output and writes serialized model data to file
    public void write(Sequencer seq) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(fileName));
        JSONObject json = seq.toJson();
        writer.print(json.toString(INDENT_FACTOR));
        writer.close();
    }

}
