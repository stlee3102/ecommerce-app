package projectx.db;

public class InventoryRecord extends BaseRecord {
  String name;
  String description;
  String sku;
  double price;
  int stockQuantity;

  public InventoryRecord(String name, String description, String sku, double price) {
    super();
    this.name = name;
    this.description = description;
    this.sku = sku;
    this.price = price;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }
  /**
   * @return the sku
   */
  public String getSku() {
    return sku;
  }
  /**
   * @return the price
   */
  public double getPrice() {
    return price;
  }
  /**
   * @param stockQuantity the processStatus to set
   */
  public void setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
  }
  /**
   * @return the stockQuantity
   */
  public int getStockQuantity() {
    return stockQuantity;
  }
}
