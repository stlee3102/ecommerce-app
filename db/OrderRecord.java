package projectx.db;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class OrderRecord extends BaseRecord {
  public enum ProcessStatus {
    NEW,
    PROCESSING,
    CLOSED,
    CANCELLED
  }

  public enum PaymentStatus {
    NEW,
    PROCESSING,
    COMPLETE,
    REFUNDED
  }

  int orderId;
  int customerId;
  Map<InventoryRecord, Integer> orderItems;
  ProcessStatus processStatus;
  PaymentStatus paymentStatus;

  public OrderRecord(int orderId, int customerId, Map<InventoryRecord, Integer> items) {
    super();
    this.orderId = orderId;
    this.customerId = customerId;
    this.processStatus = ProcessStatus.NEW;
    this.paymentStatus = PaymentStatus.NEW;

    this.orderItems = new TreeMap<>(
        (Comparator<InventoryRecord> & Serializable) (o1, o2) -> {
          return ((InventoryRecord)o1).getName().compareTo(((InventoryRecord)o2).getName());
        }
        );
    this.orderItems.putAll(items);
  }

  /**
   * @return the processStatus
   */
  public ProcessStatus getProcessStatus() {
    return processStatus;
  }
  
  /**
   * @return true if this order record can have its process status updated 
   */
  public boolean canSetProcessStatus() {
    switch (processStatus) {
    case NEW:
    case PROCESSING:
      return true;
    default:
      return false;
    }
  }
  /**
   * Will update process status only if it is allowed
   * @param processStatus the processStatus to set
   */
  public void setProcessStatus(ProcessStatus processStatus) {
    if (canSetProcessStatus())
      this.processStatus = processStatus;
  }

  /**
   * @return the paymentStatus
   */
  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  /**
   * @param paymentStatus the paymentStatus to set
   */
  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  /**
   * @return the orderId
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * @return the customerId
   */
  public int getCustomerId() {
    return customerId;
  }

  /**
   * @return the orderItems
   */
  public Map<InventoryRecord, Integer> getOrderItems() {
    return orderItems;
  }
}
