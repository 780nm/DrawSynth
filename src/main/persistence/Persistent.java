package persistence;

import org.json.JSONObject;

import java.util.UUID;

// Represents a Persistent object
public interface Persistent {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();

    // EFFECTS: Returns UUID of object
    UUID getUuid();

}
