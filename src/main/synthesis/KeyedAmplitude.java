package synthesis;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

// Represents an amplitude modulator with a drawn amplitude curve
public class KeyedAmplitude extends KeyedElement implements AmplitudeModulator {

    // EFFECTS: Constructs a keyframed amplitude modulator
    public KeyedAmplitude() {
        super();
    }

    // EFFECTS: Constructs a keyframed amplitude modulator with given UUID
    public KeyedAmplitude(UUID id) {
        super(id);
    }

    // REQUIRES: 0 <= amplitude <= 1, format has a valid configuration and encoding is PCM_SIGNED
    // MODIFIES: wave
    // EFFECTS: Manipulates amplitude of given waveform according to the stored amplitude curve
    public void applyAmplitudeProfile(double amplitude, ArrayList<Double> wave, AudioFormat format) {
        int channels = format.getChannels();
        int size = wave.size();
        int frameCount = getFrameCount();

        for (int i = 0; i < size - channels + 1; i += channels) {
            for (int j = 0; j < channels; j++) {
                double sample = wave.get(i + j);
                sample *= amplitude * lerpAt(i / (double)(size - channels + 1) * frameCount);
                wave.set(i + j, sample);
            }
        }
    }

    // EFFECTS: Returns the state of the object as a formatted string
    @Override
    public String toString() {
        return "Keyframed Amplitude Modulator: Frames - " + frames.size();
    }

}
