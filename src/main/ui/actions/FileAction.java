package ui.actions;

public interface FileAction {

    String getActionName();

    String getActionTitle();

    boolean process(String path);

}
