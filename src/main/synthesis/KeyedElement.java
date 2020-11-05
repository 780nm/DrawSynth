package synthesis;

import persistence.Persistent;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

public abstract class KeyedElement implements Persistent {
    ArrayList<Double> frames;
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
        if (0 < position && position < frames.size()) {
            double prev = frames.get((int) Math.floor(position));
            double next = frames.get((int) Math.ceil(position));

            return prev + (position - Math.floor(position)) * (next - prev);
        } else if (position <= 0) {
            return frames.get(0);
        } else {
            return frames.get(frames.size() - 1);
        }
    }

    public UUID getUuid() {
        return keyedId;
    }

}
