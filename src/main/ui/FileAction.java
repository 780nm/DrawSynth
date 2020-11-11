package ui;

import java.io.File;

public interface FileAction {

    String getActionName();

    String getActionTitle();

    boolean process(String path);

}
