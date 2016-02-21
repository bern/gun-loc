package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import model.Subscriber;
import model.platform.Blob;
import model.platform.PlatformReader;

public class GraphView extends JPanel implements Subscriber {
  private static final long serialVersionUID = 1L;

  private PlatformReader reader;
  private int padding = 25;
  private final Color[] pointColors = {
    Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
  };

  public GraphView() {
    this.setPreferredSize(new Dimension(200, 200));
  }

  @Override
  public void paint(Graphics g) {
    int width = this.getWidth();
    int height = this.getHeight();
    // Draw title
    String title = "Camera View";
    FontMetrics fm = g.getFontMetrics();
    Rectangle2D stringBounds = fm.getStringBounds(title, g);
    g.setColor(Color.BLACK);
    g.drawString(title, (int) ((width - stringBounds.getWidth()) / 2), padding / 2);
    // Draw Graph
    g.setColor(Color.lightGray);
    g.fillRect(padding, padding, width - 2 * padding, height - 2 * padding);
    // Draw Hash marks
    g.setColor(Color.BLACK);
    for (int i = 0; i <= 25; ++i) {
      g.drawLine(i * ((width - 2 * padding) / 25) + padding, height - padding - 4,
          i * ((width - 2 * padding) / 25) + padding, height - padding);
      g.drawLine(padding + 4, i * ((height - 2 * padding) / 25) + padding, 
          padding, i * ((height - 2 * padding) / 25) + padding);
    }
    // Draw points
    if (reader != null) {
      for (Blob blob : Blob.values()) {
        Point coordinate = reader.readBlob(blob);
        double x = coordinate.getX();
        double y = coordinate.getY();
        if (x != 1023 && y != 1023) {
          g.setColor(pointColors[blob.ordinal()]);
          g.fillOval((int) (x / 1024 * (width - 2 * padding) + padding),
              (int) (y / 1024 * (height- 2 * padding) + padding), 10, 10);
        }
      }
    }
  }

  @Override
  public void alert() {
    this.repaint();
  }

  public void subscribe(PlatformReader reader) {
    this.reader = reader;
    for (Blob blob : Blob.values()) {
      reader.register(this, blob);
    }
  }

}
