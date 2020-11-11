package ui;

import exceptions.ElementNotFoundException;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(SequencerApp::new);
    }

}
