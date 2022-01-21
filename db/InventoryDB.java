package projectx.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryDB extends BaseDB {
  public final static String dbfilename = "db_inventory.dat";
  
  Map<String, InventoryRecord> inventoryByName;
  Map<String, InventoryRecord> inventoryBySku;

  public InventoryDB() {
    super(dbfilename);
    this.inventoryByName = new HashMap<>();
    this.inventoryBySku = new HashMap<>();
  }

  public InventoryRecord createItem(String name, String description, String sku, double price) {
    // Check if we already have an item with the same name OR sku
    if (retrieveItemByName(name) != null || retrieveItemBySku(sku) != null) {
      return null;
    }
    
    InventoryRecord rec = new InventoryRecord(name, description, sku, price);
    inventoryByName.put(name, rec);
    inventoryBySku.put(sku, rec);
    return rec;
  }
  
  public Collection<InventoryRecord> retrieveItemList() {
    return inventoryByName.values();
  }
  
  /** Lookup by name
   * @param name
   * @return
   */
  public InventoryRecord retrieveItemByName(String name) {
    return inventoryByName.get(name);
  }
  
  /** Lookup by SKU
   * @param sku
   * @return
   */
  public InventoryRecord retrieveItemBySku(String sku) {
    return inventoryBySku.get(sku);
  }
  
  /** Helper internal method to update a given InventoryRec with the new values
   *  Any new values set to null will be ignored in the update
   * @param rec
   * @param name
   * @param description
   * @param sku
   * @param price
   */
  protected void updateItem(InventoryRecord rec, String name, String description, String sku, Double price, Integer stockQuantity) {
    if (name != null)
      rec.name = name;
    
    if (description != null)
      rec.description = description;
    
    if (sku != null)
      rec.sku = sku;
    
    if (price != null)
      rec.price = price;
    
    if (stockQuantity != null)
      rec.stockQuantity = stockQuantity;
  }
  
  /** Update record given a lookup name
   *  New name/description/sku/price/quantity will be used. If any values are null, they are ignored in the update.
   * @param lookupName
   * @param name
   * @param description
   * @param sku
   * @param price
   * @param stockQuantity
   * @return true if item was found and updated
   */
  public boolean updateItemByName(String lookupName, String name, String description, String sku, Double price, Integer stockQuantity) {
    InventoryRecord rec = retrieveItemByName(lookupName);
    if (rec != null) {
      // Make sure we don't have any conflicts with the update
      if (name != null) {
        InventoryRecord newNameRecord = retrieveItemByName(name);
        if (newNameRecord != null)
          return false;
      }
      if (sku != null) {
        InventoryRecord newSkuRecord = retrieveItemBySku(sku);
        if (newSkuRecord != null)
          return false;
      }
      
      // Update lookup table
      if (name != null && !name.equals(lookupName)) {
        inventoryByName.remove(lookupName);
        inventoryByName.put(name, rec);
      }
      if (sku != null) {
        inventoryBySku.remove(rec.sku);
        inventoryBySku.put(sku, rec);
      }
      updateItem(rec, name, description, sku, price, stockQuantity);
      return true;
    } else {
      return false;
    }
  }
  
  /** Update record given a lookup sku
   *  New name/description/sku/price/quantity will be used. If any values are null, they are ignored in the update.
   * @param lookupSku
   * @param name
   * @param description
   * @param sku
   * @param price
   * @param stockQuantity
   * @return
   */
  public boolean updateItemBySku(String lookupSku, String name, String description, String sku, Double price, Integer stockQuantity) {
    InventoryRecord rec = retrieveItemBySku(lookupSku);
    if (rec != null) {
   // Make sure we don't have any conflicts with the update
      if (name != null) {
        InventoryRecord newNameRecord = retrieveItemByName(name);
        if (newNameRecord != null)
          return false;
      }
      if (sku != null) {
        InventoryRecord newSkuRecord = retrieveItemBySku(sku);
        if (newSkuRecord != null)
          return false;
      }
      
      // Update lookup table
      if (sku != null && !sku.equals(lookupSku)) {
        inventoryBySku.remove(rec.sku);
        inventoryBySku.put(sku, rec);
      }
      if (name != null) {
        inventoryByName.remove(rec.name);
        inventoryByName.put(name, rec);
      }
      
      updateItem(rec, name, description, sku, price, stockQuantity);
      return true;
    } else {
      return false;
    }
  }
  
  
  /** Delete item by name
   * @param name
   */
  public void deleteItemByName(String name) {
    InventoryRecord rec = retrieveItemByName(name);
    if (rec != null) {
      inventoryByName.remove(rec.name);
      inventoryBySku.remove(rec.sku);
    }
  }
  
  /** Delete item by SKU
   * @param sku
   */
  public void deleteItemBySku(String sku) {
    InventoryRecord rec = retrieveItemBySku(sku);
    if (rec != null) {
      inventoryBySku.remove(rec.sku);
      inventoryByName.remove(rec.name);
    }
  }
}
