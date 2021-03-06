// code by mg
package ch.ethz.idsc.retina.app.slam.core;

import ch.ethz.idsc.owl.gui.win.GeometricLayer;
import ch.ethz.idsc.retina.app.slam.MapProvider;
import ch.ethz.idsc.sophus.lie.se2.Se2Matrix;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;

/* package */ enum SlamReactiveMapStepUtil {
  ;
  /** update reactive occurrence map. part of the map which is more then {@link lookBehindDistance} behind go kart is set to zero
   * 
   * @param gokartPose unitless representation
   * @param occurrenceMap
   * @param lookBehindDistance interpreted as [m] */
  public static void clearNonvisibleOccurrenceMap(Tensor gokartPose, MapProvider occurrenceMap, double lookBehindDistance) {
    GeometricLayer worldToGokartLayer = GeometricLayer.of(Inverse.of(Se2Matrix.of(gokartPose)));
    double[] mapArray = occurrenceMap.getMapArray();
    for (int i = 0; i < mapArray.length; i++)
      if (mapArray[i] != 0) {
        double[] worldCoord = occurrenceMap.getCellCoord(i);
        Tensor gokartCoordTensor = worldToGokartLayer.toVector(worldCoord[0], worldCoord[1]);
        double gokartCoordXPos = gokartCoordTensor.Get(0).number().doubleValue();
        if (gokartCoordXPos < lookBehindDistance)
          occurrenceMap.setValue(i, 0);
      }
  }
}
