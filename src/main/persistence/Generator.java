package persistence;

import org.json.JSONObject;

// Modular interface for state generation from JSON
public interface Generator {

    // EFFECTS: Generates appropriate object from serialized JSONObject
    void generate(JSONObject json);

}
