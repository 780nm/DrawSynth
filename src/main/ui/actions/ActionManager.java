package ui.actions;

import ui.SequencerApp;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class ActionManager implements ActionListener {

    protected SequencerApp app;
    protected JComponent target;

    public ActionManager(SequencerApp app) {
        this.app = app;
        // Doesn't set target due to construction order
    }

    public void setTarget(JComponent target) {
        this.target = target;
    }

}
