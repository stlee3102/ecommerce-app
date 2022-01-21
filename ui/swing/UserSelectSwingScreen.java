package projectx.ui.swing;

import java.awt.event.*;

import javax.swing.*;

import projectx.ProjectXController;

public class UserSelectSwingScreen extends BaseSwingScreen {
  public UserSelectSwingScreen(ProjectXController controller) {
    super(controller);
  }
  
  @Override
  public void initializeScreen() {
    super.initializeScreen();

    JFrame frame = makeJFrame();
    frame.setTitle("User Selection");
    frame.setSize(200,110);

    JPanel panel = new JPanel();
    
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    frame.setContentPane(panel);
    
    panel.add(makeActionableButton("Customer", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getScreenResponseAction().setNextScreen(new CustomerMainMenuSwingScreen(controller, true));
        resumeMainThread();
      }
    }));
    
    panel.add(makeActionableButton("Employee", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getScreenResponseAction().setNextScreen(new EmployeeMainSwingScreen(controller));
        resumeMainThread();
      }
    }));
    
    panel.add(makeActionableButton("Exit", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getScreenResponseAction().setExitApp(true);
        resumeMainThread();
      }
    }));

    //Display the window.
    frame.setVisible(true);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("select user");
  }
}