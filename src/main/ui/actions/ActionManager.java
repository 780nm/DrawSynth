package ui.actions;

import ui.SequencerApp;

import javax.swing.*;
import java.awt.event.ActionListener;

// Represents an object dedicated to processing the actions fired from an associated UI component
public abstract class ActionManager implements ActionListener {

    protected SequencerApp app;
    protected JComponent target;

    // EFFECTS: Constructs a new action manager operating on the given model
    public ActionManager(SequencerApp app) {
        this.app = app;
        // Doesn't set target due to construction order
    }

    public void setTarget(JComponent target) {
        this.target = target;
    }

}
