package ui;

import exceptions.ElementNotFoundException;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Schedules the application to be run
        SwingUtilities.invokeLater(SequencerApp::new);
    }

}
