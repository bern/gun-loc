package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;

import model.DriverModel;

import view.DriverWindow;

public class DriverController {
  private DriverModel model;
  private DriverWindow view;
  private JButton enableButton;
  private JButton refreshButton;
  private JComboBox<String> portPicker;
  private boolean enabled;

  public DriverController() {
    view = new DriverWindow();
    model = new DriverModel();
    enabled = false;
  }

  public void init() {
    view.init(this);
    view.setVisible(true);
    this.refresh();
  }

  public void registerPortPicker(JComboBox<String> portPicker) {
    this.portPicker = portPicker;
    this.portPicker.addItem("Select a Serial Port");
    this.portPicker.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DriverController.this.changePort();
      }
    });
  }

  public void registerRefreshButton(JButton refreshButton) {
    this.refreshButton = refreshButton;
    this.refreshButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DriverController.this.refresh();
      }
    });
  }

  public void registerEnableButton(JButton enableButton) {
    this.enableButton = enableButton;
    this.enableButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DriverController.this.enable();
      }
    });
  }

  protected void changePort() {
    String newPort = (String) this.portPicker.getSelectedItem();
    if (newPort != null && newPort.equals("Select a Serial Port") == false) {
      this.model.setPort(newPort);
      this.enableButton.setEnabled(true);
    } else {
      this.enableButton.setEnabled(false);
    }
  }

  protected void refresh() {
    String currentlySelected = (String) this.portPicker.getSelectedItem();
    this.portPicker.removeAllItems();
    this.portPicker.addItem("Select a Serial Port");
    this.portPicker.setSelectedIndex(0);
    Iterator<String> availableSerialPorts = this.model.availableSerialPorts().iterator();
    while (availableSerialPorts.hasNext()) {
      String port = availableSerialPorts.next();
      this.portPicker.addItem(port);
      if (currentlySelected.equals(port)) {
        this.portPicker.setSelectedItem(port);
      }
    }
  }

  // Enable mouse control
  protected void enable() {
    if (enabled) {
      this.model.disableCursors();
      enabled = false;
      this.enableButton.setText("Enable");
      this.portPicker.setEnabled(true);
      this.refreshButton.setEnabled(true);
    } else {
      this.model.setNumberOfCursors(1);
      if (this.model.enableCursors()) {
        this.enableButton.setText("Disable");
        this.portPicker.setEnabled(false);
        this.refreshButton.setEnabled(false);
        enabled = true;
      }
    }
  }

}
