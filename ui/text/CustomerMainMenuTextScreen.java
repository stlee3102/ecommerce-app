package projectx.ui.text;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.ui.ScreenResponseAction;

public class CustomerMainMenuTextScreen extends BaseTextScreen {
  public CustomerMainMenuTextScreen(ProjectXController controller, boolean newSession) {
    super(controller);
    if (newSession) {
      controller.resetCurrentOrderInfo();
    }
  }

  List<InventoryRecord> currentInventory;

  @Override
  public void initializeScreen() {
    super.initializeScreen();

    // Load inventory for this screen session
    currentInventory = controller.getInventoryDB().retrieveItemList().stream()
        .sorted(Comparator.comparing(InventoryRecord::getName))
        .collect(Collectors.toList());
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println("Select option:");

    // List entire inventory
    // Each inventory item will map to a user selection input
    for (int i = 0; i < currentInventory.size(); i++) {
      InventoryRecord item = currentInventory.get(i);
      System.out.println(i + " "
          + item.getName()
          + " (" + controller.getCurrentOrderInfo().getItemsInOrder().getOrDefault(item, 0)
          + " in cart)");
    }
    
    System.out.println("");
    System.out.println("c Checkout");
    System.out.println("r Return to user selection mode");
    System.out.println("x Exit application");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();
    String userInput = getTextInput();

    switch (userInput.toLowerCase()) {
    case "c":
    {
      boolean atLeastOneItem = false;
      for (Entry<InventoryRecord, Integer> info : controller.getCurrentOrderInfo().getItemsInOrder().entrySet()) {
        if (info.getValue() > 0) {
          // There's at least one item in order
          response.setNextScreen(new CustomerUserInfoTextScreen(controller));
          atLeastOneItem = true;
          break;
        }
      }
      
      if (!atLeastOneItem) {
        // else we have nothing to order
        System.out.println("Nothing in cart! Add some items before checking out.");
      }
      
      break;
    }
      
    case "r":
      response.setNextScreen(new UserSelectTextScreen(controller));
      break;
      
    case "x":
      response.setExitApp(true);
      break;
    
    default:
      try {
        int indexSelection = Integer.parseInt(userInput);
        if (indexSelection >= 0 && indexSelection < currentInventory.size()) {
          response.setNextScreen(
              new CustomerQuantitySelectionTextScreen(controller,
                  currentInventory.get(indexSelection).getSku()));
        } else {
          System.out.println("Invalid selection");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid selection");
      }
      break;
    }
    return response;
  }

}
