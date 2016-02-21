package model.cursor;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import model.Calibration;
import model.platform.Blob;
import model.platform.PlatformReader;

public class MouseCursor extends Cursor {
  private Robot robot;

  public MouseCursor(PlatformReader reader, Blob blob, Calibration calibration) {
    super(reader, blob, calibration);
    try {
      this.robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  @Override
  protected void handleMouseUpdate(Point mouse) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL("http://gun-loc.azurewebsites.net/hit");
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Content-Length", "0");
      connection.setRequestMethod("POST");
      connection.setUseCaches(false);
      connection.setDoOutput(true);

      OutputStream o = connection.getOutputStream();
      o.write("Ow".getBytes());
      o.close();
      
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();

      System.out.println(response.toString());
      System.out.println("Received");
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected boolean validPoint(Point mouse) {
    return (int) mouse.getX() != 1023 && (int) mouse.getY() != 1023;
  }

  protected void moveMouse(Point mouse) {
    if (this.getCalibration() != null) {
      mouse = this.getCalibration().translatePoint(mouse);
    }
    int x = (int) mouse.getX();
    int y = (int) mouse.getY();
    this.robot.mouseMove(x, y);
  }

  protected void pressMouse() {
    this.getRobot().mousePress(InputEvent.BUTTON1_MASK);
  }

  protected void releaseMouse() {
    this.getRobot().mouseRelease(InputEvent.BUTTON1_MASK);
  }

  protected Robot getRobot() {
    return this.robot;
  }

}
