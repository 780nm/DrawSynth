package ui.components.dialogues;

import ui.actions.FileAction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

// UI for selecting sequencer files
public class FilePickerDialogue {

    private static final String FILE_EXT = "json";
    private static final String FILE_ROOT = "./data/";

    // MODIFIES: performs given FileAction
    // EFFECTS: Create a new dialogue and perform the given FileAction, retry upon failure
    public FilePickerDialogue(JComponent target, FileAction action) {
        JFileChooser fileChooser = new JFileChooser(FILE_ROOT);
        fileChooser.setDialogTitle(action.getActionTitle());
        fileChooser.setApproveButtonToolTipText(action.getActionName());

        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Sequencer Files", FILE_EXT));
        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setMinimumSize(new Dimension(800, 600));

        while (true) {
            if (fileChooser.showDialog(target, action.getActionName()) == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().getName().toLowerCase().endsWith("." + FILE_EXT)) {
                    if (action.process(fileChooser.getSelectedFile().getPath())) {
                        return;
                    } else {
                        JOptionPane.showMessageDialog(fileChooser, "Unable to process file.");
                    }
                } else {
                    JOptionPane.showMessageDialog(fileChooser, "Invalid Filename. Please try again");
                }
            } else {
                return;
            }
        }

    }

}
