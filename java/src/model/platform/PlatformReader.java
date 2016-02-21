package model.platform;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Subscriber;

public abstract class PlatformReader implements Runnable {

  private boolean running = false;
  private String port;
  private ArrayList<Set<Subscriber>> subscribers = new ArrayList<Set<Subscriber>>();
  private Point[] blobs = new Point[Blob.values().length];
  private final Object runLock = new Object();
  private final Object portLock = new Object();

  public PlatformReader() {
    for (int i = 0; i < Blob.values().length; ++i) {
      subscribers.add(new HashSet<Subscriber>());
      blobs[i] = new Point(1023, 1023);
    }
  }

  public abstract void run();

  public void stop() {
    synchronized (runLock) {
      this.running = false;
    }
  }

  protected void begin() {
    synchronized (runLock) {
      this.running = true;
    }
  }

  protected boolean isRunning() {
    synchronized (runLock) {
      return this.running;
    }
  }

  public void setPort(String port) {
    synchronized (portLock) {
      this.port = port;
    }
  }

  protected String getPort() {
    synchronized (portLock) {
      return this.port;
    }
  }

  public void register(Subscriber subscriber, Blob blob) {
    synchronized (subscribers) {
      subscribers.get(blob.ordinal()).add(subscriber);
    }
  }

  public void unregister(Subscriber subscriber) {
    synchronized (subscribers) {
      for (Set<Subscriber> set : subscribers) {
        set.remove(subscriber);
      }
    }
  }

  private void notifySubscribers(Blob blob) {
    synchronized(subscribers) {
      for (int i = 0; i < subscribers.size(); ++i) {
        for (Subscriber subscriber : subscribers.get(i)) {
          subscriber.alert();
        }
      }
    }
  }

  protected void writeBlob(Blob blob, Point coordinates) {
    notifySubscribers(blob);
  }

  public Point readBlob(Blob blob) {
    synchronized (blobs) {
      return blobs[blob.ordinal()];
    }
  }

}
