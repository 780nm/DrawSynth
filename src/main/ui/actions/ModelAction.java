package ui.actions;

// Represents an action performed on the application model
public interface ModelAction {

    // EFFECTS: Returns the name of the action
    String getActionName();

    // EFFECTS: Processes the action given an array of parameters
    void processAction(Object[] params);

}
