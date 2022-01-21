package projectx.payment;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import projectx.db.InventoryRecord;
import projectx.db.OrderRecord;

public class OrderInfo {
  Map<InventoryRecord, Integer> itemsInOrder = new TreeMap<>(Comparator.comparing(InventoryRecord::getName));
  String customerName;
  OrderRecord finalizedOrderRecord;

  /**
   * @return the customerName
   */
  public String getCustomerName() {
    return customerName;
  }
  /**
   * @param customerName the customerName to set
   */
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
  /**
   * @return the itemsInOrder
   */
  public Map<InventoryRecord, Integer> getItemsInOrder() {
    return itemsInOrder;
  }
  /**
   * @return order ID if order was completed, NULL otherwise
   */
  public Integer getOrderId() {
    if (finalizedOrderRecord == null) {
      return -1;    // No order was made yet
    } else {
      return finalizedOrderRecord.getOrderId();
    }
  }
}
