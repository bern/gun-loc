package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;

import gnu.io.NRSerialPort;

import model.cursor.Cursor;
import model.cursor.CalibrationCursor;
import model.cursor.MouseCursor;
import model.platform.Blob;
import model.platform.PlatformReader;
import model.platform.UartReader;

public class DriverModel {
  
  private String port = "";
  private ArrayList<Cursor> cursors = new ArrayList<Cursor>();
  private Calibration calibration = null;
  private PlatformReader reader;
  private int nCursors;
  
  public void setPort(String port) {
    if (this.port.equals(port) == false) {
      this.port = port;
      if (reader != null) {
        this.disableCursors();
      }
    }
  }

  public Set<String> availableSerialPorts() {
    return NRSerialPort.getAvailableSerialPorts();
  }

  public boolean enableCursors() {
    reader = new UartReader();
    reader.setPort(this.port);
    (new Thread(reader)).start();
    if (nCursors <= 4 && nCursors > 0) {
      for (int i = 0; i < nCursors; ++i) {
        Cursor c = new MouseCursor(reader, Blob.fromInt(i), calibration);
        this.cursors.add(c);
        (new Thread(c)).start();
      }
      return true;
    }
    return false;
  }

  public void disableCursors() {
    for (Cursor c : cursors) {
      c.stop();
      try {
        (new Thread(c)).join();
      } catch (InterruptedException e) {
      }
    }
    cursors.clear();
    reader.stop();
  }

  public void setNumberOfCursors(int nCursors) {
    this.nCursors = nCursors;
  }

  public PlatformReader getPlatformReader() {
    return this.reader;
  }

  protected int getNumberOfCursors() {
    return this.nCursors;
  }

  public void calibrate(ArrayList<Point> screenPoints) {
    ArrayList<Point> cameraPoints = ((CalibrationCursor)this.cursors.get(0)).getCameraPoints();
    this.calibration = new Calibration(screenPoints, cameraPoints);
    this.disableCursors();
    this.enableCursors();
  }

  public void beginCalibration(int numClicks) {
    this.disableCursors();
    reader = new UartReader();
    reader.setPort(this.port);
    (new Thread(reader)).start();
    CalibrationCursor c = new CalibrationCursor(reader, Blob.Blob0, null);
    this.cursors.add(c);
    (new Thread(c)).start();
  }

}
