// code by mg
package ch.ethz.idsc.demo.mg.slam.vis;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.Mat;

import ch.ethz.idsc.demo.mg.slam.MapProvider;
import ch.ethz.idsc.demo.mg.slam.SlamContainer;
import ch.ethz.idsc.owl.math.map.Se2Bijection;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Primitives;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ enum StaticHelper {
  ;
  private static final byte CLEAR_BYTE = -1; // white
  private static final byte ORANGE = (byte) -52;
  private static final byte GREEN = (byte) 30;
  private static final byte BLUE = (byte) 5;
  private static final byte[] LOOKUP = { ORANGE, GREEN, BLUE };
  private static final double curveRadius = 0.1;

  /** sets all frames for the visualization
   * 
   * @param slamMapFrames
   * @param slamContainer
   * @param gokartLidarPose pose with units provided by lidar
   * @return array of BufferedImages of length 2 */
  public static BufferedImage[] constructFrames(SlamMapFrame[] slamMapFrames, SlamContainer slamContainer, Tensor gokartLidarPose) {
    paintRawMap(slamContainer.getOccurrenceMap(), slamMapFrames[0].getBytes());
    slamMapFrames[0].addGokartPose(gokartLidarPose, Color.BLACK);
    slamMapFrames[0].addGokartPose(slamContainer.getPose(), Color.BLUE);
    slamMapFrames[1].drawSlamWaypoints(slamContainer.getSlamWaypoints());
    if (slamContainer.getlookAheadWorldFrame().isPresent())
      // slamMapFrames[1].drawPoint(slamContainer.getlookAheadWorldFrame().get(), Color.BLUE, 0.2);
      slamMapFrames[1].addGokartPose(slamContainer.getPose(), Color.BLUE);
    if (slamContainer.getRefinedWaypointCurve().isPresent())
      drawInterpolate(slamMapFrames[1], slamContainer.getPoseUnitless(), slamContainer.getRefinedWaypointCurve().get());
    BufferedImage[] combinedFrames = new BufferedImage[2];
    for (int i = 0; i < 2; i++)
      combinedFrames[i] = slamMapFrames[i].getFrame();
    return combinedFrames;
  }

  /** paints a MapProvider object
   * 
   * @param map object to be drawn
   * @param bytes representing BufferedImage of type TYPE_BYTE_INDEXED */
  private static void paintRawMap(MapProvider map, byte[] bytes) {
    double[] mapArray = map.getMapArray();
    double maxValue = map.getMaxValue();
    for (int i = 0; i < bytes.length; i++)
      bytes[i] = (byte) (216 + 39 * (1 - mapArray[i] / maxValue));
  }

  /** draws the interpolated curve estimated by the SLAM algorithm
   * 
   * @param slamMapFrame
   * @param pose of vehicle
   * @param refinedWaypointCurve in go kart frame coordinates */
  private static void drawInterpolate(SlamMapFrame slamMapFrame, Tensor poseUnitless, Tensor refinedWaypointCurve) {
    // transform to world frame coordinates for visualization
    TensorUnaryOperator local2World = new Se2Bijection(poseUnitless).forward();
    Tensor globalCurve = Tensor.of(refinedWaypointCurve.stream().map(local2World));
    for (int i = 0; i < globalCurve.length(); ++i) {
      double[] point = Primitives.toDoubleArray(globalCurve.get(i));
      slamMapFrame.drawPoint(point, Color.BLACK, curveRadius);
    }
  }

  /** draws a Mat object
   * 
   * @param processedMat mat object containing e.g. labels of feature extraction
   * @param bytes representing frame content */
  // to be used to visualize the raw processed Mat object from SlamMapProcessing
  @SuppressWarnings("unused")
  private static void setProcessedMat(Mat processedMat, byte[] bytes) {
    byte[] processedByteArray = SlamOpenCVUtil.matToByteArray(processedMat);
    for (int i = 0; i < bytes.length; i++) {
      if (processedByteArray[i] == 0)
        bytes[i] = CLEAR_BYTE;
      else {
        int labelID = processedByteArray[i] % 3;
        bytes[i] = LOOKUP[labelID];
      }
    }
  }
}
