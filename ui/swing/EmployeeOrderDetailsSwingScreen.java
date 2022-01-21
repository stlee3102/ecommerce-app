package projectx.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;
import projectx.db.OrderRecord;

public class EmployeeOrderDetailsSwingScreen extends BaseSwingScreen {
  // UI elements that can update on the fly
  JButton processingButton;
  JButton closedButton;
  JButton cancelledButton;
  JLabel processingStatus;
  
  OrderRecord orderRecord;
  int orderId;

  public EmployeeOrderDetailsSwingScreen(ProjectXController controller, int orderId) {
    super(controller);
    this.orderId = orderId;
  }

  protected Component makeDetailsPage() {
    JPanel detailsPanel = new JPanel();
    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

    detailsPanel.add(new JLabel("Order #" + orderRecord.getOrderId() +
        " for " + controller.getCustomerDB().retrieveCustomer(orderRecord.getCustomerId()).getName()));

    double total = 0;
    // Item details table
    Vector<String> columnNames = new Vector<>(Arrays.asList(
        "Item Name",
        "Item SKU",
        "Quantity",
        "Unit Price",
        "Total"
        ));

    Vector<Vector<String>> data = new Vector<>();
    for (Entry<InventoryRecord, Integer> kv : orderRecord.getOrderItems().entrySet()) {
      InventoryRecord rec = kv.getKey();
      int quantity = kv.getValue();

      Vector<String> row = new Vector<>();
      row.add(rec.getName());
      row.add(rec.getSku());
      row.add(Integer.toString(quantity));
      row.add(Double.toString(rec.getPrice()));

      double itemTotal = quantity * rec.getPrice();
      total += itemTotal;
      row.add(Double.toString(itemTotal));

      data.add(row);
    }
    final JTable table = new JTable(data, columnNames) {
      public boolean isCellEditable(int row, int column) {                
        return false;               
      };
    };
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);
    detailsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

    detailsPanel.add(new JLabel("Total: " + total));
    detailsPanel.add(new JLabel("Payment status: " + orderRecord.getPaymentStatus().name()));
     
    detailsPanel.add(processingStatus = new JLabel(""));

    return detailsPanel;
  }

  protected JButton makeProcessingButton(OrderRecord.ProcessStatus processStatus) {
    return makeActionableButton("Set to " + processStatus.name(), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.getOrderDB().updateOrderProcessStatus(orderId, processStatus);
        getScreenResponseAction().setFlushToDb(true);
        updateContentPanels();
      }
    });
  }

  protected Component makeProcessingUpdatePanel() {
    JPanel processingUpdatePanel = new JPanel();
    processingUpdatePanel.setLayout(new BoxLayout(processingUpdatePanel, BoxLayout.Y_AXIS));

    processingUpdatePanel.add(new JLabel("Processing update options"));

    processingButton = makeProcessingButton(OrderRecord.ProcessStatus.PROCESSING);
    closedButton = makeProcessingButton(OrderRecord.ProcessStatus.CLOSED);
    cancelledButton = makeProcessingButton(OrderRecord.ProcessStatus.CANCELLED);

    processingUpdatePanel.add(processingButton);
    processingUpdatePanel.add(closedButton);
    processingUpdatePanel.add(cancelledButton);

    return processingUpdatePanel;
  }

  protected void updateContentPanels() {
    if (!orderRecord.canSetProcessStatus()) {
      processingButton.setEnabled(false);
      closedButton.setEnabled(false);
      cancelledButton.setEnabled(false);
    }
    
    processingStatus.setText("Processing status: " + orderRecord.getProcessStatus().name());
  }

  @Override
  public void initializeScreen() {
    super.initializeScreen();
    orderRecord = controller.getOrderDB().retrieveOrder(orderId);

    JFrame frame = makeJFrame();
    frame.setTitle("Order Details");
    frame.setSize(100,100);

    JPanel panel = new JPanel();

    panel.setLayout(new BorderLayout());
    frame.setContentPane(panel);

    // Return to user selection button
    panel.add(makeActionableButton("Return to employee view", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getScreenResponseAction().setNextScreen(new EmployeeMainSwingScreen(controller));
        resumeMainThread();
      }
    }), BorderLayout.NORTH);
    
    // Details panel
    panel.add(makeDetailsPage(), BorderLayout.CENTER);
    
    // Processing update panel
    panel.add(makeProcessingUpdatePanel(), BorderLayout.EAST);
    
    updateContentPanels();

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("employee order details");
  }
}