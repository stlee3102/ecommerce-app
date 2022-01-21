package projectx.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDB extends BaseDB {
  public final static String dbfilename = "db_order.dat";
  
  List<OrderRecord> orders;
  
  public OrderDB() {
    super(dbfilename);
    
    orders = new ArrayList<>();
  }
  
  /** Create a new order with the given customer ID, inventory SKU and quantity
   * @param customerId
   * @param inventoryItemSku
   * @param quantity
   * @return
   */
  public OrderRecord createOrder(CustomerRecord customerRecord, Map<InventoryRecord, Integer> items) {
    int orderId = orders.size();
    OrderRecord rec = new OrderRecord(orderId, customerRecord.id, items);
    orders.add(rec);
    return rec;
  }
  
  /** Retrieve order by ID
   * @param orderId
   * @return
   */
  public OrderRecord retrieveOrder(int orderId) {
    if (orderId >= orders.size()) {
      return null;
    } else {
      return orders.get(orderId);
    }
  }
  
  /**
   * @return list of order records
   */
  public List<OrderRecord> retrieveAllOrders() {
    return orders;
  }
  
  /** Update order process status given the order ID
   * @param orderId
   * @param status
   * @return true if order was found, false otherwise
   */
  public boolean updateOrderProcessStatus(int orderId, OrderRecord.ProcessStatus status) {
    OrderRecord rec = retrieveOrder(orderId);
    if (rec != null) {
      rec.setProcessStatus(status);
      return true;
    } else {
      return false;
    }
  }
  
  /** Update order payment status given the order ID
   * @param orderId
   * @param status
   * @return true if order was found, false otherwise
   */
  public boolean updateOrderPaymentStatus(int orderId, OrderRecord.PaymentStatus status) {
    OrderRecord rec = retrieveOrder(orderId);
    if (rec != null) {
      rec.setPaymentStatus(status);
      return true;
    } else {
      return false;
    }
  }
  
  public void deleteOrderPayment(int orderId) {
    if (orderId < orders.size()) {
      // Remove the item at the given index and set it to null
      // Leaves a "hole" in the list but preserves the index values for all records
      orders.set(orderId, null);
    }
  }
}
