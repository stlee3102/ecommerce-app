package projectx.ui.text;

import projectx.ProjectXController;
import projectx.ui.ScreenResponseAction;

public class UserSelectTextScreen extends BaseTextScreen {

  public UserSelectTextScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("Please select user mode or 'x' to exit application");
    System.out.println("0 Customer");
    System.out.println("1 Employee");
    System.out.println("x Exit application");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();

    switch (getTextInput()) {
    case "0":
      response.setNextScreen(new CustomerMainMenuTextScreen(controller, true));
      break;
      
    case "1":
      response.setNextScreen(new EmployeeMainTextScreen(controller));
      break;
      
    case "x":
    case "X":
      response.setExitApp(true);
      break;
      
    default:
      System.out.println("Invalid selection");
      break;
    }
    
    return response;
  }

  
}