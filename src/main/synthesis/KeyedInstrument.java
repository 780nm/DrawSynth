package synthesis;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

// Represents an instrument with a drawn waveform
public class KeyedInstrument extends KeyedElement implements Instrument {

    // EFFECTS: Constructs a keyframed instrument
    public KeyedInstrument() {
        super();
    }

    // EFFECTS: Constructs a keyframed instrument with the given UUID
    public KeyedInstrument(UUID id) {
        super(id);
    }

    // REQUIRES: format has a valid configuration and encoding is PCM_SIGNED, pitch is not null, frames count >= 2
    // EFFECTS: Return a Double ArrayList representation of the audio source in the given format,
    //          as a function of the given pitch modulator, duration and the stored waveform
    public ArrayList<Double> synthesizeWaveform(double basePitch, PitchModulator pitchMod,
                                                                             int duration, AudioFormat format) {
        int channels = format.getChannels();
        float sampleRate = format.getSampleRate();
        int frameCount = frames.size();
        double scale = Math.pow(2, format.getSampleSizeInBits() - 1);

        ArrayList<Double> output = new ArrayList<>(duration * channels);

        double position = 0;

        while (output.size() / channels < duration) {
            double sample = lerpAt(position % frameCount);
            for (int i = 0; i < channels; i++) {
                output.add(sample * scale);
            }
            double period = pitchMod.getPeriodAtTime(basePitch,
                    output.size() / (double)(channels * duration), sampleRate);
            position += (frameCount / period);
        }
        return output;
    }

    // EFFECTS: Returns the state of the object as a formatted string
    @Override
    public String toString() {
        return "Keyframed Instrument: Frames - " + frames.size();
    }

}
