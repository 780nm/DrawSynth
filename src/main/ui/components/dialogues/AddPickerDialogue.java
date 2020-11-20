package ui.components.dialogues;

import ui.actions.ModelAction;

import javax.swing.*;

// UI for selecting which of a list of elements should be added to the application
public class AddPickerDialogue extends PropertiesDialogue {

    private ModelAction selected;

    // MODIFIES: performs one of given ModelActions
    // EFFECTS: Create a new picker dialogue and perform the selected action
    public AddPickerDialogue(JComponent target, ModelAction[] actions) {
        ButtonGroup group = new ButtonGroup();

        for (ModelAction action : actions) {
            JRadioButton button = new JRadioButton(action.getActionName());
            button.addItemListener(e -> selected = action);
            panel.add(button);
            group.add(button);
        }

        int choice = JOptionPane.showConfirmDialog(target, panel, "Add Element", JOptionPane.OK_CANCEL_OPTION);
        if (choice == 0 && selected != null) {
            selected.processAction(null);
        }
    }

}
