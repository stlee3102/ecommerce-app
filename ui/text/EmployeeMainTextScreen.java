package projectx.ui.text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import projectx.ProjectXController;
import projectx.db.OrderRecord;
import projectx.ui.ScreenResponseAction;

public class EmployeeMainTextScreen extends BaseTextScreen {
  
  Map<Integer, OrderRecord> currentOrderRecords;

  public EmployeeMainTextScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    super.initializeScreen();
    currentOrderRecords = new LinkedHashMap<>();
    for (OrderRecord rec : controller.getOrderDB().retrieveAllOrders()) {
      if (rec != null) {
        currentOrderRecords.put(rec.getOrderId(), rec);
      }
    }
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    
    System.out.println("Orders");
    
    // Headers
    String[] headers = {
        "Order #",
        "Customer Name",
        "Payment Status",
        "Process Status"
        };
    System.out.println(String.join("\t\t", headers));
    
    // List orders
    for (OrderRecord rec : currentOrderRecords.values()) {
      List<String> col = new ArrayList<>();
      col.add(String.valueOf(rec.getOrderId()));
      col.add(controller.getCustomerDB().retrieveCustomer(rec.getCustomerId()).getName());
      col.add(rec.getPaymentStatus().name());
      col.add(rec.getProcessStatus().name());
      System.out.println(String.join("\t", col));
    }
    
    System.out.println("\nEnter order number for details\nor select one of the following options:");
    System.out.println("r Return to user selection mode");
    System.out.println("x Exit application");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();

    String userInput = getTextInput().toLowerCase();
    switch (userInput) {
    case "r":
      response.setNextScreen(new UserSelectTextScreen(controller));
      break;
    case "x":
      response.setExitApp(true);
      break;
    default:
      try {
        int orderId = Integer.parseInt(userInput);
        if (currentOrderRecords.keySet().contains(orderId)) {
          response.setNextScreen(new EmployeeOrderDetailsTextScreen(controller, orderId));
        } else {
          System.out.println("Invalid order number");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid selection");
      }
      break;
    }
    return response;
  }

}
