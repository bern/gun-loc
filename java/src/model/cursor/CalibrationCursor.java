package model.cursor;

import java.awt.Point;
import java.util.ArrayList;

import model.Calibration;
import model.platform.Blob;
import model.platform.PlatformReader;

public class CalibrationCursor extends MouseCursor {

  private Point currentPosition;
  private ArrayList<Point> cameraPoints;

  public CalibrationCursor(PlatformReader reader, Blob blob, Calibration calibration) {
    super(reader, blob, calibration);
    cameraPoints = new ArrayList<Point>();
  }

  @Override
  protected void moveMouse(Point mouse) {
    int x = (int) mouse.getX();
    int y = (int) mouse.getY();
    super.getRobot().mouseMove(x, y);
    currentPosition = mouse;
  }

  @Override
  public void pressMouse() {
    super.pressMouse();
    this.addCameraPoint(currentPosition);
  }

  public ArrayList<Point> getCameraPoints() {
    synchronized (cameraPoints) {
      ArrayList<Point> cloned = new ArrayList<Point>(cameraPoints.size());
      cloned.addAll(cameraPoints);
      return cloned;
    }
  }

  protected void addCameraPoint(Point mouse) {
    synchronized (cameraPoints) {
      this.cameraPoints.add(mouse);
    }
  }

}
