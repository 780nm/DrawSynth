package synthesis;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Persistent;

import java.util.ArrayList;
import java.util.UUID;

// Represents an element with an arbitrary double curve
public abstract class KeyedElement implements Persistent {

    protected ArrayList<Double> frames;
    private UUID keyedId;

    // EFFECTS: Constructs a keyframed element
    public KeyedElement() {
        clear();
        keyedId = UUID.randomUUID();
    }

    // EFFECTS: Constructs a keyframed element with given UUID
    public KeyedElement(UUID id) {
        clear();
        keyedId = id;
    }

    // MODIFIES: this
    // EFFECTS: Removes all frames
    public void clear() {
        frames = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Appends a frame with the given value to the frame list
    public void addFrame(Double frame) {
        frames.add(frame);
    }

    // EFFECTS: Given a position, performs a linear interpolation between
    //          frames adjacent to that position and returns the result
    protected double lerpAt(double position) {
        if (0 < position && position < frames.size() - 1) {
            double prev = frames.get((int) Math.floor(position));
            double next = frames.get((int) Math.ceil(position));

            return prev + (position - Math.floor(position)) * (next - prev);
        } else if (position <= 0) {
            return frames.get(0);
        } else {
            return frames.get(frames.size() - 1);
        }
    }

    // EFFECTS: Returns the state of the Object as a serialized JSONObject
    @Override
    public JSONObject toJson() {
        JSONArray frameArray = new JSONArray();
        for (double frame : frames) {
            frameArray.put(frame);
        }

        JSONObject json = new JSONObject();
        json.put("uuid", keyedId.toString());
        json.put("frames", frameArray);
        json.put("class", this.getClass().getName());
        return json;
    }

    // Getters

    public UUID getUuid() {
        return keyedId;
    }

}
