package projectx.ui.text;

import java.util.Scanner;

import projectx.ProjectXController;
import projectx.ui.BaseScreen;

public abstract class BaseTextScreen extends BaseScreen {

  BaseTextScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    System.out.println("========================================");
  }

  @Override
  public void updateDisplay() {
    System.out.println("----------------------------------------");
  }
  
  protected String getTextInput() {
    Scanner input = new Scanner(System.in);
    return input.nextLine();
  }
}
