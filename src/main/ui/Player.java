package ui;

import model.Playable;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

// Instantiates a new thread and plays an Object supporting the Playable interface using the Java Sound API
public class Player implements Runnable {

    private static final int BUF_SIZE = 16384;

    private Playable target;
    private AudioFormat format;

    private SourceDataLine line;
    private Thread thread;

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED
    // EFFECTS: Constructs a new Player object with the given format
    Player(AudioFormat format) {
        this.format = format;
    }

    // EFFECTS: Instantiates a new thread and plays the stored target
    public void start() {
        thread = new Thread(this);
        thread.setName("Playback");
        thread.start();
    }

    // EFFECTS: Stops the running thread, if any
    public void stop(String errMsg) {
        thread = null;
        if (errMsg != null) {
            System.err.println(errMsg);
        }
    }

    // EFFECTS: Instantiates a new SourceDataLine and plays the stored target
    public void run() {

        if (!initOutputLine()) {
            return;
        }

        line.start();
        writeDataToLine(line);

        if (thread != null) {
            line.drain();
        }

        line.stop();
        line.close();
        line = null;
        stop(null);
    }

    // EFFECTS: Attempts to open an audio output line. Returns true if successful, false otherwise
    // Adapted from the official Java Sound API sample program
    private boolean initOutputLine() {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            stop("Error opening line matching " + info);
            return false;
        }

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, BUF_SIZE);
        } catch (LineUnavailableException e) {
            stop("Unable to open line: " + e);
            return false;
        }

        return true;
    }

    // REQUIRES: line is not null, and is started
    // EFFECTS: writes byte buffer to output line
    private void writeDataToLine(SourceDataLine line) {
        byte[] waveform;
        byte[] buffer;

        try {
            waveform = target.synthesizeClip(format);
        } catch (IOException exception) {
            stop("Error during playback: " + exception);
            return;
        }

        int count = 0;
        while (count < waveform.length) {
            buffer = Arrays.copyOfRange(waveform, count, Math.min(waveform.length, count + BUF_SIZE));
            count += buffer.length;
            try {
                line.write(buffer, 0, buffer.length);
            } catch (Exception exception) {
                stop("Error during playback: " + exception);
                return;
            }
        }
    }

    // Getters and Setters

    public Playable getTarget() {
        return target;
    }

    public void setTarget(Playable target) {
        this.target = target;
    }

    public AudioFormat getFormat() {
        return format;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

}
