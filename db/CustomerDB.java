package projectx.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDB extends BaseDB {
  public final static String dbfilename = "db_customer.dat";
  
  protected int nextId;
  Map<Integer, CustomerRecord> customers;

  public CustomerDB() {
    super(dbfilename);
    nextId = 0;
    customers = new HashMap<>();
  }
  
  public CustomerRecord createCustomer(String name) {
    CustomerRecord rec = new CustomerRecord(nextId, name);
    customers.put(nextId, rec);
    nextId++;
    return rec;
  }
  
  public CustomerRecord retrieveCustomer(int id) {
    return customers.get(id);
  }
  
  /**
   * Returns a list of all customer records that matches with the given name
   * @param name
   * @return
   */
  public List<CustomerRecord> retrieveCustomer(String name) {
    List<CustomerRecord> ret = new ArrayList<>();
    
    for (CustomerRecord cr : customers.values()) {
      if (cr.getName().equals(name)) {
        ret.add(cr);
      }
    }
    
    return ret;
  }
  
  /**
   * Updates the customer record's name with the given ID
   * 
   * @param id
   * @param name
   * @return true if record was found and updated
   */
  public boolean updateCustomer(int id, String name) {
    CustomerRecord cr = customers.get(id);
    if (cr == null) {
      return false;
    } else {
      cr.setName(name);
      return true;
    }
  }
  
  /**
   * Deletes the customer record with the given ID
   * 
   * @param id
   */
  public void deleteCustomer(int id) {
    customers.remove(id);
  }
}
