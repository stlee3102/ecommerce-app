package projectx.payment;

import java.util.Map.Entry;

import projectx.ProjectXController;
import projectx.db.*;
import projectx.db.OrderRecord.PaymentStatus;
import projectx.db.OrderRecord.ProcessStatus;

public class PaymentProcessor {
  ProjectXController controller;
  
  public PaymentProcessor(ProjectXController controller) {
    this.controller = controller;
  }
  
  /**
   * interfaces with a 3rd party payment system and submits the
   * charge on the customer's account. We will assume that the
   * charge will always be successful
   * 
   * @param order
   * @return
   */
  public boolean processOrderPayment() {
    OrderInfo order = controller.getCurrentOrderInfo();
    
    // Prepare the order by creating the customer and order records
    CustomerRecord customerRecord = controller.getCustomerDB().createCustomer(order.customerName);
    OrderRecord orderRecord = controller.getOrderDB().createOrder(customerRecord, order.getItemsInOrder());
    
    // ... Sends payment request to 3rd party ... receive success from 3rd party ...
    
    // Update inventory stock
    for (Entry<InventoryRecord, Integer> item : order.getItemsInOrder().entrySet()) {
      String sku = item.getKey().getSku();
      int currentQuantity = item.getKey().getStockQuantity();
      
      controller.getInventoryDB().updateItemBySku(sku, null, null, null, null, currentQuantity - item.getValue());
    }
    
    // Update order status
    orderRecord.setPaymentStatus(PaymentStatus.COMPLETE);
    order.finalizedOrderRecord = orderRecord;
    
    return true;
  }
  
  public void refundOrder(int orderId) {
    OrderRecord orderRecord = controller.getOrderDB().retrieveOrder(orderId);
    
    // Update inventory stock
    for (Entry<InventoryRecord, Integer> item : orderRecord.getOrderItems().entrySet()) {
      String sku = item.getKey().getSku();
      int currentQuantity = item.getKey().getStockQuantity();
      
      controller.getInventoryDB().updateItemBySku(sku, null, null, null, null, currentQuantity + item.getValue());
    }
    
    // Update order status
    orderRecord.setPaymentStatus(PaymentStatus.REFUNDED);
    orderRecord.setProcessStatus(ProcessStatus.CANCELLED);
  }
}
