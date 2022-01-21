package projectx.ui;

public class ScreenResponseAction {
  BaseScreen nextScreen;
  boolean flushToDb;
  boolean exitApp;

  public void setNextScreen(BaseScreen nextScreen) {
    this.nextScreen = nextScreen;
  }

  /**
   * @return screen object to transition to, null if no transition required
   */
  public BaseScreen nextScreen() {
    return this.nextScreen;
  }

  /**
   * @return the flushToDb
   */
  public boolean isFlushToDb() {
    return flushToDb;
  }

  /**
   * @param flushToDb the flushToDb to set
   */
  public void setFlushToDb(boolean flushToDb) {
    this.flushToDb = flushToDb;
  }

  /**
   * @return the exitApp
   */
  public boolean isExitApp() {
    return exitApp;
  }

  /**
   * @param exitApp the exitApp to set
   */
  public void setExitApp(boolean exitApp) {
    this.exitApp = exitApp;
  }
}
