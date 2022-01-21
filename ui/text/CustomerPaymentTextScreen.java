package projectx.ui.text;

import java.util.Map.Entry;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.payment.OrderInfo;
import projectx.ui.ScreenResponseAction;

public class CustomerPaymentTextScreen extends BaseTextScreen {
  public CustomerPaymentTextScreen(ProjectXController controller) {
    super(controller);
  }
  
  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println("Your cart:");
    
    OrderInfo orderInfo = controller.getCurrentOrderInfo();
    float total = 0;
    for (Entry<InventoryRecord, Integer> item : orderInfo.getItemsInOrder().entrySet()) {
      float itemTotalCost = (float) (item.getValue() * item.getKey().getPrice());
      total += itemTotalCost;
      System.out.printf("%s x%d @ %.02fea = %.02f\n",
          item.getKey().getName(),
          item.getValue(),
          item.getKey().getPrice(),
          itemTotalCost);
    }
    System.out.printf("Total: %.02f\n", total);
    System.out.printf("Order for %s\n\n", orderInfo.getCustomerName());
    
    
    System.out.println("Select option:");
    System.out.println("p Continue with payment");
    System.out.println("a Abandon order");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();
    String userInput = getTextInput();

    switch (userInput.toLowerCase()) {
    case "p":
      if (controller.processPayment()) {
        // Assumes all payments will be successful
        response.setNextScreen(new CustomerThankYouTextScreen(controller));
      } else {
        System.out.println("Payment unsuccessful");
      }
      break;
      
    case "a":
      response.setNextScreen(new CustomerMainMenuTextScreen(controller, true));
      break;
    
    default:
        System.out.println("Invalid selection");
      break;
    }
    return response;
  }

}
