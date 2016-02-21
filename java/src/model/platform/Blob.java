package model.platform;

public enum Blob {
  Blob0, Blob1, Blob2, Blob3;
  private static final Blob[] values = Blob.values();

  public static Blob fromInt(int i) {
    if (i >= 0 && i < values.length) {
      return values[i];
    }
    return null;
  }
}
