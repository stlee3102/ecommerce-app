package projectx.ui.swing;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import projectx.ProjectXController;
import projectx.ui.BaseScreen;
import projectx.ui.ScreenResponseAction;

public abstract class BaseSwingScreen extends BaseScreen {
  private ScreenResponseAction response;
  private JFrame frame;
  Object waitObject = new Object();

  BaseSwingScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    System.out.println("========================================");
  }

  @Override
  public void updateDisplay() {
    System.out.println("----------------------------------------");
  }
  
  protected JFrame makeJFrame() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }
  
  protected JButton makeActionableButton(String buttonText, ActionListener actionListner) {
    JButton button = new JButton(buttonText);
    button.addActionListener(actionListner);
    return button;
  }
  
  protected ScreenResponseAction getScreenResponseAction() {
    return response;
  }

  protected void resumeMainThread() {
    synchronized(waitObject) {
      waitObject.notify();
    }
  }

  @Override
  public ScreenResponseAction waitForInput() {
    response = new ScreenResponseAction();
    
    synchronized(waitObject) {
      try {
        waitObject.wait();
        // While waiting, UI might set values to the response object at this point
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    
    if (response.nextScreen() != null && frame != null) {
      frame.dispose();
    }
    
    if (response.isExitApp()) {
      System.exit(0);
    }

    return response;
  }
}
