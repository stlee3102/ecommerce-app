package projectx.ui.text;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.ui.ScreenResponseAction;

public class CustomerQuantitySelectionTextScreen extends BaseTextScreen {
  InventoryRecord item;
  
  public CustomerQuantitySelectionTextScreen(ProjectXController controller, String sku) {
    super(controller);
    item = controller.getInventoryDB().retrieveItemBySku(sku);
  }
  
  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println(item.getName());
    System.out.println(item.getDescription());
    System.out.printf("%.02f\n", item.getPrice());
    System.out.printf("%d available\n", item.getStockQuantity());
    System.out.printf("%d in current order\n\n",
        controller.getCurrentOrderInfo().getItemsInOrder().getOrDefault(item, 0));
    System.out.println("Enter new order amount: ");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();
    String userInput = getTextInput();
    
    try {
      int amount = Integer.parseInt(userInput);
      if (amount < 0) {
        System.out.println("Invalid amount");
      } else if (amount <= item.getStockQuantity()) {
        // Valid amount
        controller.getCurrentOrderInfo().getItemsInOrder().put(item, amount);
        response.setNextScreen(new CustomerMainMenuTextScreen(controller, false));
      } else {
        System.out.println("Quantity not available, try again!");
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input");
    }

    return response;
  }

}
