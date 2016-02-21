package view.calibration;

public class OSXCalibrationScreen extends StandardCalibrationScreen implements CalibrationScreen {
  private static final long serialVersionUID = -6594520138147208306L;

  public OSXCalibrationScreen() {
    super();
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
    com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
  }

}
