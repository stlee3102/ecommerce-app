package projectx.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import projectx.ProjectXController;
import projectx.db.OrderRecord;

public class EmployeeMainSwingScreen extends BaseSwingScreen {
  Integer selectedOrderId;
  
  public EmployeeMainSwingScreen(ProjectXController controller) {
    super(controller);
  }
  
  @Override
  public void initializeScreen() {
    super.initializeScreen();

    JFrame frame = makeJFrame();
    frame.setTitle("Employee View");
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
    
    // Button for launching order details screen
    JButton detailsButton = makeActionableButton("", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getScreenResponseAction().setNextScreen(new EmployeeOrderDetailsSwingScreen(controller, selectedOrderId));
        resumeMainThread();
      }
    });
    detailsButton.setEnabled(false);
    panel.add(detailsButton, BorderLayout.SOUTH);
    
    // Main table with order info
    Vector<String> columnNames = new Vector<>(Arrays.asList(
        "Order #",
        "Customer Name",
        "Payment Status",
        "Process Status"
    ));

    Vector<Vector<String>> data = new Vector<>();
    for (OrderRecord rec : controller.getOrderDB().retrieveAllOrders()) {
      Vector<String> row = new Vector<>();
      row.add(Integer.toString(rec.getOrderId()));
      row.add(controller.getCustomerDB().retrieveCustomer(rec.getCustomerId()).getName());
      row.add(rec.getPaymentStatus().name());
      row.add(rec.getProcessStatus().name());
      
      data.add(row);
    }

    final JTable table = new JTable(data, columnNames) {
      public boolean isCellEditable(int row, int column) {                
        return false;               
      };
    };
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
      public void valueChanged(ListSelectionEvent event) {
        selectedOrderId = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        System.out.println("Table selected order #" + selectedOrderId);
        
        detailsButton.setText("See details on order #" + selectedOrderId);
        detailsButton.setEnabled(true);
      }
    });
    
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("employee main screen");
  }
}