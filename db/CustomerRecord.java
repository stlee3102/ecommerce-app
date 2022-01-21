package projectx.db;

public class CustomerRecord extends BaseRecord {
  int id;
  String name;

  public CustomerRecord(int id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
