package ui;

import exceptions.ElementNotFoundException;

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
