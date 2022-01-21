package projectx.ui.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import projectx.ProjectXController;

public class CustomerUserInfoSwingScreen extends BaseSwingScreen {
  JButton payButton;
  JTextField nameTextField;

  public CustomerUserInfoSwingScreen(ProjectXController controller) {
    super(controller);
  }

  @Override
  public void initializeScreen() {
    super.initializeScreen();

    JFrame frame = makeJFrame();
    frame.setTitle("Payment Info");
    frame.setSize(500,100);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    frame.setContentPane(panel);

    JPanel userInfoEntryPanel = new JPanel();
    userInfoEntryPanel.setLayout(new BoxLayout(userInfoEntryPanel, BoxLayout.X_AXIS));
    panel.add(userInfoEntryPanel, BorderLayout.CENTER);

    userInfoEntryPanel.add(new JLabel("Name:"));
    nameTextField = new JTextField();
    nameTextField.setSize(400, 28);
    userInfoEntryPanel.add(nameTextField);
    nameTextField.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {}

      @Override
      public void keyPressed(KeyEvent e) {}

      @Override
      public void keyReleased(KeyEvent e) {
	JTextField textField = (JTextField)e.getSource();
	payButton.setEnabled(!textField.getText().isEmpty());
      }

    });

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
    panel.add(optionsPanel, BorderLayout.SOUTH);

    optionsPanel.add(makeActionableButton("<= Go back", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	getScreenResponseAction().setNextScreen(new CustomerMainMenuSwingScreen(controller, false));
	resumeMainThread();
      }

    }));

    payButton = makeActionableButton("Make payment", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	controller.getCurrentOrderInfo().setCustomerName(nameTextField.getText());
	getScreenResponseAction().setNextScreen(new CustomerPaymentSwingScreen(controller));
	resumeMainThread();
      }

    });
    payButton.setEnabled(false);
    optionsPanel.add(payButton);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void updateDisplay() {
    super.updateDisplay();
    System.out.println("customer main menu screen");
  }
}