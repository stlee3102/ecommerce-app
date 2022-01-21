package projectx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projectx.db.*;
import projectx.db.OrderRecord.PaymentStatus;
import projectx.db.OrderRecord.ProcessStatus;
import projectx.payment.*;
import projectx.ui.*;
import projectx.ui.swing.*;
import projectx.ui.text.*;

public class ProjectXController {

  CustomerDB customerDB;
  InventoryDB inventoryDB;
  OrderDB orderDB;
  List<BaseDB> databases;

  OrderInfo currentCustomerOrderInfo;
  PaymentProcessor paymentProcessor;

  BaseScreen screen;

  ProjectXController() {
    initializeDBs();
    initializeProcessors();
    initializeUI();

    resetCurrentOrderInfo();
  }

  protected void initializeDBs() {
    databases = new ArrayList<>();

    customerDB = BaseDB.<CustomerDB>initializeDB(CustomerDB.dbfilename);
    if (customerDB == null) {
      customerDB = new CustomerDB();
    }
    databases.add(customerDB);

    inventoryDB = BaseDB.<InventoryDB>initializeDB(InventoryDB.dbfilename);
    if (inventoryDB == null) {
      inventoryDB = new InventoryDB();
    }
    databases.add(inventoryDB);

    orderDB = BaseDB.<OrderDB>initializeDB(OrderDB.dbfilename);
    if (orderDB == null) {
      orderDB = new OrderDB();
    }
    databases.add(orderDB);

    // Populate inventory DB with demo data
    populateDemoData();
  }

  private void populateDemoData() {
    if (inventoryDB.retrieveItemList().isEmpty() &&
	customerDB.retrieveCustomer(0) == null &&
	orderDB.retrieveAllOrders().isEmpty()) {
      // Inventory
      InventoryRecord chocolate = inventoryDB.createItem("Chocolate cookies",
	  "Delicious dark chocolate delights (NOT gluten free)",
	  "DRKCOCO112233", 25.0);
      chocolate.setStockQuantity(999);

      InventoryRecord vanilla = inventoryDB.createItem("Vanilla candies",
	  "Pure white vanilla candies (NOT sugar free)",
	  "VRYPLNANDWHT8", 10.0);
      vanilla.setStockQuantity(123);

      InventoryRecord matcha = inventoryDB.createItem("Matcha dip cookies",
	  "Authentic traditional sweets (NOT caffine free)",
	  "GRNGOBBLEYUM5", 35.0);
      matcha.setStockQuantity(25);

      // Customers & Orders
      OrderRecord order = orderDB.createOrder(customerDB.createCustomer("Tony S."),
	  Map.of(chocolate, 300, vanilla, 25, matcha, 99));
      order.setPaymentStatus(PaymentStatus.COMPLETE);
      order.setProcessStatus(ProcessStatus.CLOSED);

      order = orderDB.createOrder(customerDB.createCustomer("Peter P."),
	  Map.of(matcha, 10));
      order.setPaymentStatus(PaymentStatus.REFUNDED);
      order.setProcessStatus(ProcessStatus.CANCELLED);

      order = orderDB.createOrder(customerDB.createCustomer("Stephen S."),
	  Map.of(chocolate, 40, matcha, 34));
      order.setPaymentStatus(PaymentStatus.COMPLETE);
      order.setProcessStatus(ProcessStatus.PROCESSING);

      order = orderDB.createOrder(customerDB.createCustomer("Natasha R."),
	  Map.of(vanilla, 22, matcha, 65));
      order.setPaymentStatus(PaymentStatus.COMPLETE);

      order = orderDB.createOrder(customerDB.createCustomer("Clint B."),
	  Map.of(vanilla, 5, matcha, 10));
      order.setPaymentStatus(PaymentStatus.COMPLETE);

      order = orderDB.createOrder(customerDB.createCustomer("Bruce B."),
	  Map.of(chocolate, 88));
      order.setPaymentStatus(PaymentStatus.COMPLETE);

      order = orderDB.createOrder(customerDB.createCustomer("Steve R."),
	  Map.of(vanilla, 11));
      order.setPaymentStatus(PaymentStatus.COMPLETE);
    }
  }

  protected void initializeProcessors() {
    paymentProcessor = new PaymentProcessor(this);
  }

  protected void initializeUI() {
    // First screen when starting the app
    //    screen = new UserSelectTextScreen(this);
    screen = new UserSelectSwingScreen(this);

    // For testing
    //    screen = new CustomerMainMenuScreen(this, true);
    //    screen = new EmployeeMainScreen(this);
    //    screen = new EmployeeOrderDetailsScreen(this, 0);
  }

  /**
   * @return the customerDB
   */
  public CustomerDB getCustomerDB() {
    return customerDB;
  }

  /**
   * @return the inventoryDB
   */
  public InventoryDB getInventoryDB() {
    return inventoryDB;
  }

  /**
   * @return the orderDB
   */
  public OrderDB getOrderDB() {
    return orderDB;
  }

  /**
   * @return the currentCustomerOrderInfo
   */
  public OrderInfo getCurrentOrderInfo() {
    return currentCustomerOrderInfo;
  }

  /**
   * Resets current customer session's order quantities
   */
  public void resetCurrentOrderInfo() {
    this.currentCustomerOrderInfo = new OrderInfo();
  }

  /**
   * @return true if payment was successful
   */
  public boolean processPayment() {
    return paymentProcessor.processOrderPayment();
  }

  /**
   * @param orderId
   */
  public void refundOrder(int orderId) {
    paymentProcessor.refundOrder(orderId);
  }

  protected void flushDBs() throws IOException {
    for (BaseDB db : databases) {
      db.flush();
    }
  }

  void run() throws InterruptedException, IOException {
    // Controller main loop

    // Set this to true in final code if you want to keep the system running forever
    ScreenResponseAction response;

    do {
      screen.initializeScreen();

      do {
	screen.updateDisplay();
	response = screen.waitForInput();

	// After user's interaction completes, we flush updates to the DB (file)
	if (response.isFlushToDb()) {
	  flushDBs();
	}

	if (response.isExitApp()) {
	  break;
	}
      } while (response.nextScreen() == null);  // If null, no need to reset the screen

      screen = response.nextScreen();

    } while(response != null && !response.isExitApp());

    // Always flush DBs upon exiting
    flushDBs();

    System.out.println("Goodbye!");
  }
}
