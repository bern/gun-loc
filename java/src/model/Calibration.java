package model;

import java.awt.Point;
import java.util.ArrayList;

public class Calibration {
  private double[] warpMatrix = new double[16];

  public Calibration(ArrayList<Point> screenPoints, ArrayList<Point> cameraPoints) {
    if (screenPoints.size() != cameraPoints.size()) {
      throw new RuntimeException("Must calibrate with same number of points.");
    }
    if (screenPoints.size() != 4 || cameraPoints.size() != 4) {
      throw new RuntimeException("Calibration must be done with 4 points.");
    }
    computeWarp(screenPoints, cameraPoints);
  }

  // Adapted from Dr. Jonny Chung Lee's Wiimote Project http://johnnylee.net/projects/wii/
  // http://www.cs.cmu.edu/~rahuls/pub/iccv2001-rahuls.pdf
  private void computeWarp(ArrayList<Point> screenPoints, ArrayList<Point> cameraPoints) {
    double[] srcMat = new double[16];
    double[] dstMat = new double[16];
    computeQuadToSquare(cameraPoints, srcMat);
    computeSquareToQuad(screenPoints, dstMat);
    mutlMats(srcMat, dstMat);
  }

  private void mutlMats(double[] srcMat, double[] dstMat) {
    for (int r = 0; r < 4; ++r) {
      int ri = r * 4;
      for (int c = 0; c < 4; ++c) {
        warpMatrix[ri + c] = (
            srcMat[ri] * dstMat[c] +
            srcMat[ri + 1] * dstMat[c + 4] +
            srcMat[ri + 2] * dstMat[c + 8] +
            srcMat[ri + 3] * dstMat[c + 12]
        );
      }
    }
  }

  private void computeQuadToSquare(ArrayList<Point> cameraPoints, double[] srcMat) {
    computeSquareToQuad(cameraPoints, srcMat);

    double a = srcMat[0];
    double d = srcMat[1];
    double g = srcMat[3];
    double b = srcMat[4];
    double e = srcMat[5];
    double h = srcMat[7];
    double c = srcMat[12];
    double f = srcMat[13];

    double A = e - f * h;
    double B = c * h - b;
    double C = b * f - c * e;
    double D = f * g - d;
    double E = a - c * g;
    double F = c * d - a * f;
    double G = d * h - e * g;
    double H = b * g - a * h;
    double I = a * e - b * d;

    double idet = 1.0 / (a * A + b * D + c * G);

    srcMat[0] = A * idet;
    srcMat[1] = D * idet;
    srcMat[2] = 0;
    srcMat[3] = G * idet;
    srcMat[4] = B * idet;
    srcMat[5] = E * idet;
    srcMat[6] = 0;
    srcMat[7] = H * idet;
    srcMat[8] = 0;
    srcMat[9] = 0;
    srcMat[10] = 1;
    srcMat[11] = 0;
    srcMat[12] = C * idet;
    srcMat[13] = F * idet;
    srcMat[14] = 0;
    srcMat[15] = I * idet;
  }

  private void computeSquareToQuad(ArrayList<Point> cameraPoints, double[] mat) {
    double x0 = cameraPoints.get(0).getX();
    double y0 = cameraPoints.get(0).getY();
    double x1 = cameraPoints.get(1).getX();
    double y1 = cameraPoints.get(1).getY();
    double x2 = cameraPoints.get(2).getX();
    double y2 = cameraPoints.get(2).getY();
    double x3 = cameraPoints.get(3).getX();
    double y3 = cameraPoints.get(3).getY();

    double dx1 = x1 - x2;
    double dy1 = y1 - y2;
    double dx2 = x3 - x2;
    double dy2 = y3 - y2;
    double sx = x0 - x1 + x2 - x3;
    double sy = y0 - y1 + y2 - y3;
    double g = (sx * dy2 - dx2 * sy) / (dx1 * dy2 - dx2 * dy1);
    double h = (dx1 * sy - sx * dy1) / (dx1 * dy2 - dx2 * dy1);
    double a = x1 - x0 + g * x1;
    double b = x3 - x0 + h * x3;
    double c = x0;
    double d = y1 - y0 + g * y1;
    double e = y3 - y0 + h * y3;
    double f = y0;

    mat[0] = a;
    mat[1] = d;
    mat[2] = 0;
    mat[3] = g;
    mat[4] = b;
    mat[5] = e;
    mat[6] = 0;
    mat[7] = h;
    mat[8] = 0;
    mat[9] = 0;
    mat[10] = 1;
    mat[11] = 0;
    mat[12] = c;
    mat[13] = f;
    mat[14] = 0;
    mat[15] = 1;
  }

  public Point translatePoint(Point cameraPoint) {
    double[] result = new double[4];
    double z = 0;
    double x = cameraPoint.getX();
    double y = cameraPoint.getY();
    for (int i = 0; i < 4; ++i) {
      result[i] = x * warpMatrix[i] + y * warpMatrix[4 + i] + z * warpMatrix[8 + i] + 1 * warpMatrix[12 + i];
    }
    Point translated = new Point((int)(result[0] / result[3]), (int)(result[1] / result[3]));
    return translated;
  }

}
