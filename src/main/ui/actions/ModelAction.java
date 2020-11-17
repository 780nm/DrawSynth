package ui.actions;

public interface ModelAction {

    String getActionName();

    void processAction(Object[] params);

}
