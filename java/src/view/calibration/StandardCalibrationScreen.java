package view.calibration;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class StandardCalibrationScreen extends JFrame implements CalibrationScreen {

  private static final long serialVersionUID = 1669113969770992986L;
  protected Point targetLocation;
  protected JLabel target;
  protected int width = 100, height = 100;
  protected int index = 0;
  protected double[][] scalers = {
    {0.15, 0.15},
    {0.85, 0.15},
    {0.15, 0.85},
    {0.85, 0.85}
  };

  public StandardCalibrationScreen() {
    this.setResizable(false);
    this.setUndecorated(true);
    this.setSize(StandardCalibrationScreen.getScreenSize());
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      try {
        Image targetPicture = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("target.png")).getScaledInstance(width, height,
            Image.SCALE_SMOOTH);
        target = new JLabel(new ImageIcon(targetPicture));
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.setLayout(null);
      this.add(target);
      this.nextTarget();
    }
    super.setVisible(visible);
    this.setAlwaysOnTop(visible);
  }

  public Point getTargetLocation() {
    Point centerTarget = new Point((int) (targetLocation.getX() + width / 2), (int) (targetLocation.getY() + width / 2));
    return centerTarget;
  }

  public void nextTarget() {
    Dimension screenSize = StandardCalibrationScreen.getScreenSize();
    double maxX = screenSize.getWidth();
    double maxY = screenSize.getHeight();
    double[] scaler = scalers[index++];
    index %= 4;
    targetLocation = new Point((int) (maxX * scaler[0]), (int) (maxY * scaler[1]));
    this.target.setLocation(targetLocation);
    Dimension size = target.getPreferredSize();
    target.setBounds((int)targetLocation.getX(), (int)targetLocation.getY(), size.width, size.height);
  }

  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

}
