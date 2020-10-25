package model;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

// Objects implementing the Playable interface may upon request generate an audio clip as a byte array
public interface Playable {

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Return a byte array representation of the audio source in the given format
    byte[] synthesizeClip(AudioFormat format);

}
