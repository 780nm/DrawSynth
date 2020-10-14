package model;

import org.junit.jupiter.api.Test;
import synthesis.SinusoidInstrument;

import javax.sound.sampled.AudioFormat;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SequencerTest {

    @Test
    public void testStub() {
        Sequencer sequencer = new Sequencer(new Track[0]);
        AudioFormat format = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        sequencer.addTracks(1, new SinusoidInstrument());
        sequencer.removeTrack(0);
        sequencer.synthesizeWaveform(format);
    }


}
