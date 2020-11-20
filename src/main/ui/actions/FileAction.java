package ui.actions;

// Represents an action related to manipulating persistent state
public interface FileAction {

    // EFFECTS: Returns the name of the action
    String getActionName();

    // EFFECTS: Returns the displayed title of the action
    String getActionTitle();

    // REQUIRES: path is a valid path to a supported file format (not nec. extant)
    // EFFECTS: Processes the action given a file path, return true if successful
    boolean process(String path);

}
