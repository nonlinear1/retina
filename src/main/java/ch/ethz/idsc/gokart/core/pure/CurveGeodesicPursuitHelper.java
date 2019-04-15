// code by gjoel
package ch.ethz.idsc.gokart.core.pure;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import ch.ethz.idsc.owl.bot.se2.glc.DynamicRatioLimit;
import ch.ethz.idsc.owl.math.planar.ArgMinVariable;
import ch.ethz.idsc.owl.math.planar.Extract2D;
import ch.ethz.idsc.owl.math.planar.GeodesicPursuit;
import ch.ethz.idsc.owl.math.planar.GeodesicPursuitInterface;
import ch.ethz.idsc.owl.math.planar.TrajectoryEntryFinder;
import ch.ethz.idsc.sophus.group.Se2GroupElement;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorScalarFunction;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

/* package */ enum CurveGeodesicPursuitHelper {
  ;
  /** @param pose of vehicle {x[m], y[m], angle}
   * @param speed of vehicle TODO GJOEL state unit
   * @param curve in world coordinates
   * @param isForward driving direction, true when forward or stopped, false when driving backwards
   * @param geodesicInterface type of planned curve
   * @param entryFinder strategy to find best re-entry point
   * @param ratioLimits depending on pose and speed
   * @return ratio rate with interpretation rad*m^-1 TODO GJOEL state unit */
  static Optional<Scalar> getRatio(Tensor pose, Scalar speed, Tensor curve, boolean isForward, //
      GeodesicInterface geodesicInterface, TrajectoryEntryFinder entryFinder, List<DynamicRatioLimit> ratioLimits) {
    TensorUnaryOperator tensorUnaryOperator = new Se2GroupElement(pose).inverse()::combine;
    Tensor tensor = Tensor.of(curve.stream().map(tensorUnaryOperator));
    if (!isForward)
      mirrorAndReverse(tensor);
    Predicate<Scalar> isCompliant = isCompliant(ratioLimits, pose, speed);
    TensorScalarFunction mapping = vector -> { //
      GeodesicPursuitInterface geodesicPursuit = new GeodesicPursuit(geodesicInterface, vector);
      Tensor ratios = geodesicPursuit.ratios();
      if (ratios.stream().map(Tensor::Get).allMatch(isCompliant))
        return Norm._2.ofVector(Extract2D.FUNCTION.apply(vector));
      return RealScalar.of(Double.MAX_VALUE); // TODO GJOEL unitless?
    };
    Scalar var = ArgMinVariable.using(entryFinder, mapping, 25).apply(tensor);
    Optional<Tensor> lookAhead = entryFinder.on(tensor).apply(var).point;
    return lookAhead.map(vector -> new GeodesicPursuit(geodesicInterface, vector).firstRatio().orElse(null));
  }

  /** mirror the points along the y axis and invert their orientation
   * @param se2points curve given by points {x,y,a} */
  private static void mirrorAndReverse(Tensor se2points) {
    se2points.set(Scalar::negate, Tensor.ALL, 0);
    se2points.set(Scalar::negate, Tensor.ALL, 2);
  }

  /** @param ratioLimits depending on pose and speed
   * @param pose of vehicle
   * @param speed of vehicle
   * @return predicate to determine whether ratio is compliant with all posed turning ratio limits */
  private static Predicate<Scalar> isCompliant(List<DynamicRatioLimit> ratioLimits, Tensor pose, Scalar speed) {
    return ratio -> ratioLimits.stream().map(c -> c.at(pose, speed)).allMatch(c -> c.isInside(ratio));
  }
}