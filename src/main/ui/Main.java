package ui;

import exceptions.ElementNotFoundException;
import synthesis.SampleUtil;

import javax.sound.sampled.AudioFormat;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class Main {

    public static void main(String[] args) {
        try {
            SequencerApp app = new SequencerApp();
            app.start();
        } catch (ElementNotFoundException exception) {
            System.err.println("Invalid Sequencer Configuration");
        }
    }

}
