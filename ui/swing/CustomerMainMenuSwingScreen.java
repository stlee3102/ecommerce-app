package projectx.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.*;

import projectx.ProjectXController;
import projectx.db.InventoryRecord;

public class CustomerMainMenuSwingScreen extends BaseSwingScreen {
  JTable inventoryTable;
  JButton checkoutButton;

  public CustomerMainMenuSwingScreen(ProjectXController controller, boolean newSession) {
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

    JFrame frame = makeJFrame();
    frame.setTitle("Customer View");
    frame.setSize(100,100);

    JPanel panel = new JPanel();

    panel.setLayout(new BorderLayout());
    frame.setContentPane(panel);

    // Return to user selection button
    panel.add(makeActionableButton("Return to user selection", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	getScreenResponseAction().setNextScreen(new UserSelectSwingScreen(controller));
	resumeMainThread();
      }
    }), BorderLayout.NORTH);

    // Button for checkout
    checkoutButton = makeActionableButton("Checkout", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	for (int i = 0; i < inventoryTable.getRowCount(); i++) {
	  String itemName = inventoryTable.getValueAt(i, 0).toString();
	  InventoryRecord item = controller.getInventoryDB().retrieveItemByName(itemName);
	  int orderQuantity = Integer.parseInt(inventoryTable.getValueAt(i, 4).toString().trim());
	  controller.getCurrentOrderInfo().getItemsInOrder().put(item, orderQuantity);
	}

	getScreenResponseAction().setNextScreen(new CustomerUserInfoSwingScreen(controller));
	resumeMainThread();
      }
    });
    checkoutButton.setEnabled(false);
    panel.add(checkoutButton, BorderLayout.SOUTH);

    // Main table with product inventory
    Vector<String> columnNames = new Vector<>(Arrays.asList(
	"Product Name",
	"Description",
	"Price",
	"Available",
	"In Cart"
	));

    Vector<Vector<String>> data = new Vector<>();
    for (InventoryRecord rec : currentInventory) {
      Vector<String> row = new Vector<>();
      row.add(rec.getName());
      row.add(rec.getDescription());
      row.add(Double.toString(rec.getPrice()));
      row.add(Integer.toString(rec.getStockQuantity()));
      row.add(Integer.toString(controller.getCurrentOrderInfo().getItemsInOrder().getOrDefault(rec, 0)));

      data.add(row);
    }

    inventoryTable = new JTable(data, columnNames) {
      public boolean isCellEditable(int row, int column) {
	return column == 4;	// Only the "In Cart" column should be editable               
      };
    };
    inventoryTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
    inventoryTable.setFillsViewportHeight(true);
    inventoryTable.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {

      @Override
      public void editingStopped(ChangeEvent e) {
	updateCheckoutButton();
      }

      @Override
      public void editingCanceled(ChangeEvent e) {
	updateCheckoutButton();
      }

    });
    panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
    
    updateCheckoutButton();
  }

  protected void updateCheckoutButton() {
    boolean buttonEnabled = true;
    String buttonText = "Checkout";
    try {
      int totalQuantity = 0;
      for (int i = 0; i < inventoryTable.getRowCount(); i++) {
	int stockQuantity = Integer.parseInt(inventoryTable.getValueAt(i, 3).toString());
	int orderQuantity = Integer.parseInt(inventoryTable.getValueAt(i, 4).toString().trim());

	if (orderQuantity < 0) {
	  buttonEnabled = false;
	  buttonText = "Invalid order value, please fix before checking out!";
	  break;
	} else if (orderQuantity > stockQuantity) {
	  buttonEnabled = false;
	  buttonText = "Not enough stock, please fix before checking out!";
	  break;
	}

	totalQuantity += orderQuantity;
      }

      if (totalQuantity == 0) {
	buttonEnabled = false;
      }
    } catch (NumberFormatException e) {
      buttonEnabled = false;
      buttonText = "Invalid order value, please fix before checking out!";
    }

    checkoutButton.setEnabled(buttonEnabled);
    checkoutButton.setText(buttonText);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("customer main menu screen");
  }
}