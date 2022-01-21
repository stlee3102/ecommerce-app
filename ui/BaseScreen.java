package projectx.ui;

import projectx.ProjectXController;

public abstract class BaseScreen {
  protected ProjectXController controller;
  
  protected BaseScreen(ProjectXController controller) {
    this.controller = controller;
  }
  
  public abstract void initializeScreen();
  public abstract void updateDisplay();
  public abstract ScreenResponseAction waitForInput();
}
