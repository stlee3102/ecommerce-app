package projectx.ui.text;

import projectx.ProjectXController;
import projectx.ui.ScreenResponseAction;

public class CustomerUserInfoTextScreen extends BaseTextScreen {
  public CustomerUserInfoTextScreen(ProjectXController controller) {
    super(controller);
  }
  
  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println("Order info for user");
    System.out.println("Name");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();
    String userInput = getTextInput();
    controller.getCurrentOrderInfo().setCustomerName(userInput);
    response.setNextScreen(new CustomerPaymentTextScreen(controller));
    
    return response;
  }

}
