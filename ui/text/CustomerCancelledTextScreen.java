package projectx.ui.text;

import projectx.ProjectXController;
import projectx.ui.ScreenResponseAction;

public class CustomerCancelledTextScreen extends BaseTextScreen {
  public CustomerCancelledTextScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    super.initializeScreen();
    
    controller.refundOrder(controller.getCurrentOrderInfo().getOrderId());
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println("Order refunded");
    System.out.println("c Continue and start a new order");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();
    String userInput = getTextInput();
    
    switch (userInput.toLowerCase()) {
    case "c":
      response.setNextScreen(new CustomerMainMenuTextScreen(controller, true));
      response.setFlushToDb(true);
      break;

    default:
      System.out.println("Invalid selection");
      break;
    }
    return response;
  }

}
