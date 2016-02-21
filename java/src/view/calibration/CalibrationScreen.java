package view.calibration;

import java.awt.Point;
import java.awt.event.MouseListener;

public interface CalibrationScreen {

  // Gets the X, Y coordinates of target image on screen.
  public Point getTargetLocation();

  // Advances the target to its next position.
  public void nextTarget();

  // Makes the calibration screen fullscreen.
  public void setVisible(boolean visible);

  public void addMouseListener(MouseListener l);

  // Destroys the Window
  public void dispose();

}
