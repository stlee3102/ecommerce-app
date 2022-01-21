package projectx.ui.text;

import java.util.Map.Entry;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.payment.OrderInfo;
import projectx.ui.ScreenResponseAction;

public class CustomerThankYouTextScreen extends BaseTextScreen {
  public CustomerThankYouTextScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    OrderInfo orderInfo = controller.getCurrentOrderInfo();
    
    System.out.println("Thank you!\n");
    System.out.println("Order #" + orderInfo.getOrderId() + " for " + orderInfo.getCustomerName());
    
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

    System.out.println("Select option:");
    System.out.println("c Confirm done");
    System.out.println("r Refund this order");
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

    case "r":
      response.setNextScreen(new CustomerCancelledTextScreen(controller));
      break;

    default:
      System.out.println("Invalid selection");
      break;
    }
    return response;
  }

}
