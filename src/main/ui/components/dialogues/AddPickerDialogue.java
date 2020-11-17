package ui.components.dialogues;

import ui.actions.ModelAction;

import javax.swing.*;

public class AddPickerDialogue extends PropertiesDialogue {

    private ModelAction selected;

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
