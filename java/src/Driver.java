import java.awt.EventQueue;

import controller.DriverController;

public class Driver {

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        DriverController controller = new DriverController();
        controller.init();
      }
    });
  }

}
