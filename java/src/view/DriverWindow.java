package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.DriverController;

public class DriverWindow extends JFrame {

  private static final long serialVersionUID = 4851854411789454130L;

  public void init(DriverController controller) {
    // Window Components
    JComboBox<String> portPicker = new JComboBox<String>();
    JButton refreshButton = new JButton("Refresh");
    JButton enableButton = new JButton("Enable");

    // Panel
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // Dropdown and Refresh button
    JPanel portPanel = new JPanel();
    portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.X_AXIS));
    portPicker.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
    portPanel.add(portPicker);
    portPanel.add(refreshButton);
    panel.add(portPanel);


    // Calibrate and Enable buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(enableButton);
    panel.add(buttonPanel);
    
    this.add(panel);

    // Controller
    controller.registerPortPicker(portPicker);
    controller.registerRefreshButton(refreshButton);
    controller.registerEnableButton(enableButton);

    // Window
    this.pack();
    this.setTitle("Smartboard Driver");
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
  }

}
