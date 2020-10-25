package persistence;

import org.json.JSONObject;

import java.util.UUID;

public interface Persistent {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();

    UUID getUuid();

}
