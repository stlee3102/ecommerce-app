package projectx.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseDB implements Serializable {
  private String filename;

  protected BaseDB(String dbfilename) {
    filename = dbfilename;
  }

  /**
   * Attempts to deserialize data from disk that was stored as a DB object.
   * 
   * @param dbFilename
   * @return deserialized data from disk, null if file doesn't exists
   */
  public static <T extends BaseDB> T initializeDB(String dbFilename) {
    T ret = null;
    try {
      FileInputStream fileIn;
      fileIn = new FileInputStream(dbFilename);
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      ret = (T)objectIn.readObject();
      objectIn.close();
    } catch (IOException | ClassNotFoundException e) {
      // Start fresh in case of any read errors or data incompatibilities
      ret = null;
    }
    return ret;
  }

  /**
   * Serializes current object to disk
   * @throws IOException 
   */
  public void flush() throws IOException {
    FileOutputStream fileOut = new FileOutputStream(filename);
    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
    
    objectOut.writeObject(this);
    objectOut.close();
  }
}
