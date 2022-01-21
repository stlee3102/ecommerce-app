package projectx.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;

public class CustomerPaymentSwingScreen extends BaseSwingScreen {
  public CustomerPaymentSwingScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    super.initializeScreen();

    JFrame frame = makeJFrame();
    frame.setTitle("Payment Summary");
    frame.setSize(100,100);

    JPanel panel = new JPanel();

    panel.setLayout(new BorderLayout());
    frame.setContentPane(panel);

    // Northern area with the per item summary
    Vector<String> columnNames = new Vector<>(Arrays.asList(
	"Product Name",
	"Unit Price",
	"In Cart",
	"Total"
	));

    Vector<Vector<String>> data = new Vector<>();
    double total = 0;
    for (Entry<InventoryRecord, Integer> kv : controller.getCurrentOrderInfo().getItemsInOrder().entrySet()) {
      Vector<String> row = new Vector<>();
      InventoryRecord rec = kv.getKey();
      int orderQuantity = kv.getValue();
      double itemTotalCost = rec.getPrice() * orderQuantity;
      total += itemTotalCost;

      row.add(rec.getName());
      row.add(Double.toString(rec.getPrice()));
      row.add(Integer.toString(orderQuantity));
      row.add(Double.toString(itemTotalCost));

      data.add(row);
    }

    JTable orderSummaryTable = new JTable(data, columnNames) {
      public boolean isCellEditable(int row, int column) {
	return false;               
      };
    };
    orderSummaryTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
    orderSummaryTable.setFillsViewportHeight(true);

    panel.add(new JScrollPane(orderSummaryTable), BorderLayout.NORTH);

    // Center area with the general summary
    JPanel totalCostPanel = new JPanel();
    totalCostPanel.setLayout(new BoxLayout(totalCostPanel, BoxLayout.X_AXIS));
    totalCostPanel.add(new JLabel("Total cost: " + total));
    panel.add(totalCostPanel, BorderLayout.CENTER);


    // Southern area with the options buttons
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
    panel.add(optionsPanel, BorderLayout.SOUTH);


    // Button to abort
    optionsPanel.add(makeActionableButton("Abort order", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	getScreenResponseAction().setNextScreen(new CustomerMainMenuSwingScreen(controller, true));
	resumeMainThread();
      }
    }));

    // Button to confirm
    optionsPanel.add(makeActionableButton("Confirm", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	if (controller.processPayment()) {
	  // Assumes all payments will be successful
	  getScreenResponseAction().setNextScreen(new CustomerThankYouSwingScreen(controller));
	  getScreenResponseAction().setFlushToDb(true);
	  resumeMainThread();
	}
      }
    }));

    //Display the window.
    frame.pack();
    frame.setVisible(true);

  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("customer main menu screen");
  }
}