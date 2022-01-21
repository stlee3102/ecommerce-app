package projectx.ui.text;

import java.util.Map.Entry;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.db.OrderRecord;
import projectx.db.OrderRecord.ProcessStatus;
import projectx.ui.ScreenResponseAction;

public class EmployeeOrderDetailsTextScreen extends BaseTextScreen {
  OrderRecord orderRecord;

  public EmployeeOrderDetailsTextScreen(ProjectXController controller, int orderId) {
    super(controller);
    orderRecord = controller.getOrderDB().retrieveOrder(orderId);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();

    System.out.println("Order " + orderRecord.getOrderId() + " details");

    System.out.println("Customer Name:" + controller.getCustomerDB().retrieveCustomer(orderRecord.getCustomerId()));

    float total = 0;
    for (Entry<InventoryRecord, Integer> item : orderRecord.getOrderItems().entrySet()) {
      float itemTotalCost = (float) (item.getValue() * item.getKey().getPrice());
      total += itemTotalCost;
      System.out.printf("%s x%d @ %.02fea = %.02f\n",
          item.getKey().getName(),
          item.getValue(),
          item.getKey().getPrice(),
          itemTotalCost);
    }
    System.out.printf("Total: %.02f\n", total);

    System.out.println("\nPayment Status: " + orderRecord.getPaymentStatus().name());
    System.out.println("Process Status: " + orderRecord.getProcessStatus().name());


    System.out.println("\nSelect one of the following options:");
    if (orderRecord.canSetProcessStatus()) {
      System.out.println("p Set process status to PROCESSING");
      System.out.println("c Set process status to CLOSED");
      System.out.println("n Set process status to CANCELLED");
    }
    System.out.println("r Return to order listing");
  }

  @Override
  public ScreenResponseAction waitForInput() {
    ScreenResponseAction response = new ScreenResponseAction();

    switch (getTextInput().toLowerCase()) {
    case "p":
      orderRecord.setProcessStatus(ProcessStatus.PROCESSING);
      System.out.println("Updated processing status to " + ProcessStatus.PROCESSING);
      break;
    case "c":
      orderRecord.setProcessStatus(ProcessStatus.CLOSED);
      System.out.println("Updated processing status to " + ProcessStatus.CLOSED);
      break;
    case "n":
      orderRecord.setProcessStatus(ProcessStatus.CANCELLED);
      System.out.println("Updated processing status to " + ProcessStatus.CANCELLED);
      break;
    case "r":
      response.setNextScreen(new EmployeeMainTextScreen(controller));
      break;
    default:
      System.out.println("Invalid selection");
      break;
    }
    return response;
  }

}
