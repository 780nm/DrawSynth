package synthesis;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Persistent;


import java.util.ArrayList;
import java.util.UUID;

public abstract class KeyedElement implements Persistent {

    protected ArrayList<Double> frames;
    private UUID keyedId;

    public KeyedElement() {
        clear();
        keyedId = UUID.randomUUID();
    }

    public KeyedElement(UUID id) {
        clear();
        keyedId = id;
    }

    public void clear() {
        frames = new ArrayList<>();
    }

    public void addFrame(Double frame) {
        frames.add(frame);
    }

    public int getFrameCount() {
        return frames.size();
    }

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

    public UUID getUuid() {
        return keyedId;
    }

}
