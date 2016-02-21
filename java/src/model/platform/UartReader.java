package model.platform;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;

import gnu.io.NRSerialPort;

public class UartReader extends PlatformReader {
  private DataInputStream stream;
  private final int baud = 9600;

  @Override
  public void run() {
    this.begin();
    NRSerialPort serialPort = new NRSerialPort(this.getPort(), baud);
    serialPort.connect();
    stream = new DataInputStream(serialPort.getInputStream());
    int[] coordinates = new int[2];
    char[] num = new char[64];
    while (this.isRunning()) {
      if (this.read() == 'O') {
        if (this.read() == 'w') {
          this.writeBlob(Blob.Blob0, new Point(0, 0));
        }
      }
    }
    try {
      this.stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    serialPort.disconnect();
  }

  private char read() {
    try {
      while (stream.available() == 0) {
        Thread.sleep(1);
      }
      return (char) stream.readByte();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return '\0';
  }
;
}
