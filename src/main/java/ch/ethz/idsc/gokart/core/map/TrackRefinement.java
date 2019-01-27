// code by mh
package ch.ethz.idsc.gokart.core.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.ethz.idsc.retina.util.math.SI;
import ch.ethz.idsc.retina.util.math.UniformBSpline2;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Abs;

public class TrackRefinement {
  public abstract class TrackConstraint {
    Tensor controlPointsX = null;
    Tensor controlPointsY = null;
    Tensor radiusControlPoints = null;

    public abstract void compute(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusControlPoints);

    public Tensor getControlPointsX() {
      return controlPointsX;
    }

    public Tensor getControlPointsY() {
      return controlPointsY;
    }

    public Tensor getRadiusControlPoints() {
      return radiusControlPoints;
    }
  }

  public class TrackSplitConstraint extends TrackConstraint {
    private final BSplineTrack track;
    private Scalar trackProg = null;
    private Tensor trackPos = null;
    private Tensor trackDirection = null;

    public TrackSplitConstraint(BSplineTrack track) {
      this.track = track;
    }

    @Override // from TrackConstraint
    public void compute(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusControlPoints) {
      Tensor first = Tensors.of(controlpointsX.Get(0), controlpointsY.Get(0));
      Tensor second = Tensors.of(controlpointsX.Get(1), controlpointsY.Get(1));
      Tensor startPos = Mean.of(Tensors.of(first, second));
      if (Objects.isNull(trackProg) || Objects.isNull(trackPos) || Objects.isNull(trackDirection)) {
        trackProg = track.getNearestPathProgress(startPos);
        trackPos = track.getPosition(trackProg);
        trackDirection = track.getDirection(trackProg);
      }
      Tensor realVector = second.subtract(first);
      Scalar projection = (Scalar) Max.of(realVector.dot(trackDirection), Quantity.of(0, SI.METER)).divide(RealScalar.of(2));
      Tensor correctedFirst = startPos.subtract(trackDirection.multiply(projection));
      Tensor correctedSecond = startPos.add(trackDirection.multiply(projection));
      this.controlPointsX = controlpointsX;
      this.controlPointsY = controlpointsY;
      this.radiusControlPoints = radiusControlPoints;
      controlpointsX.set(correctedFirst.Get(0), 0);
      controlpointsX.set(correctedSecond.Get(0), 1);
      controlpointsY.set(correctedFirst.Get(1), 0);
      controlpointsY.set(correctedSecond.Get(1), 1);
    }
  }

  public class PositionalStartConstraint extends TrackConstraint {
    Tensor wantedPosition = null;
    Tensor wantedDirection = null;

    @Override // from TrackConstraint
    public void compute(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusControlPoints) {
      Tensor first = Tensors.of(controlpointsX.Get(0), controlpointsY.Get(0));
      Tensor second = Tensors.of(controlpointsX.Get(1), controlpointsY.Get(1));
      Tensor startPos = Mean.of(Tensors.of(first, second));
      if (Objects.isNull(wantedPosition)) {
        wantedPosition = startPos;
        wantedDirection = Normalize.with(Norm._2).apply(second.subtract(first));
      }
      Tensor realVector = second.subtract(first);
      Scalar projection = (Scalar) Max.of(realVector.dot(wantedDirection), Quantity.of(0, SI.METER)).divide(RealScalar.of(2));
      Tensor correctedFirst = startPos.subtract(wantedDirection.multiply(projection));
      Tensor correctedSecond = startPos.add(wantedDirection.multiply(projection));
      this.controlPointsX = controlpointsX;
      this.controlPointsY = controlpointsY;
      this.radiusControlPoints = radiusControlPoints;
      controlpointsX.set(correctedFirst.Get(0), 0);
      controlpointsX.set(correctedSecond.Get(0), 1);
      controlpointsY.set(correctedFirst.Get(1), 0);
      controlpointsY.set(correctedSecond.Get(1), 1);
    }
  }

  public class PositionalEndConstraint extends TrackConstraint {
    Tensor wantedPosition = null;
    Tensor wantedDirection = null;

    @Override // from TrackConstraint
    public void compute(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusControlPoints) {
      int lastIndex = controlpointsX.length() - 1;
      int secondLastIndex = lastIndex - 1;
      Tensor first = Tensors.of(controlpointsX.Get(secondLastIndex), controlpointsY.Get(secondLastIndex));
      Tensor second = Tensors.of(controlpointsX.Get(lastIndex), controlpointsY.Get(lastIndex));
      Tensor startPos = Mean.of(Tensors.of(first, second));
      if (Objects.isNull(wantedPosition)) {
        wantedPosition = startPos;
        wantedDirection = Normalize.with(Norm._2).apply(second.subtract(first));
      }
      Tensor realVector = second.subtract(first);
      Scalar projection = (Scalar) Min.of(realVector.dot(wantedDirection), Quantity.of(0, SI.METER)).divide(RealScalar.of(2));
      Tensor correctedFirst = startPos.subtract(wantedDirection.multiply(projection));
      Tensor correctedSecond = startPos.add(wantedDirection.multiply(projection));
      this.controlPointsX = controlpointsX;
      this.controlPointsY = controlpointsY;
      this.radiusControlPoints = radiusControlPoints;
      controlpointsX.set(correctedFirst.Get(0), secondLastIndex);
      controlpointsX.set(correctedSecond.Get(0), lastIndex);
      controlpointsY.set(correctedFirst.Get(1), secondLastIndex);
      controlpointsY.set(correctedSecond.Get(1), lastIndex);
    }
  }

  public TrackRefinement(OccupancyGrid occupancyGrid) {
    this.occupancyGrid = occupancyGrid;
  }

  private final OccupancyGrid occupancyGrid;

  Tensor getRefinedTrack(Tensor trackData, Scalar resolution, int iterations, boolean closed, List<TrackConstraint> constraints) {
    return getRefinedTrack(trackData.get(0), trackData.get(1), trackData.get(2), resolution, iterations, closed, constraints);
  }

  private static final Scalar gdRadiusGrowth = Quantity.of(0.07, SI.METER);
  private static final Scalar gdRegularizer = RealScalar.of(0.01);

  Tensor getRefinedTrack(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusCtrPoints, Scalar resolution, int iterations, boolean closed,
      List<TrackConstraint> constraints) {
    int m = (int) (controlpointsX.length() * resolution.number().doubleValue());
    int n = controlpointsX.length();
    Tensor queryPositions;
    if (closed)
      queryPositions = Tensors.vector(i -> RealScalar.of((n + 0.0) * (i / (m + 0.0))), m);
    else
      // TODO MH try Subdivide.of(0, n-2, m-1) for the below
      queryPositions = Tensors.vector((i) -> RealScalar.of((n - 2.0) * (i / (m - 1.0))), m - 1);
    Tensor splineMatrix = UniformBSpline2.getBasisMatrix(n, queryPositions, 0, closed);
    Tensor splineMatrixTransp = Transpose.of(splineMatrix);
    Tensor splineMatrix1Der = UniformBSpline2.getBasisMatrix(n, queryPositions, 1, closed);
    /* for(int it=0;it<iterations;it++) {
     * Tensor positions = MPCBSpline.getPositions(controlpointsX, controlpointsY, queryPositions, closed, splineMatrix);
     * Tensor sideVectors = MPCBSpline.getSidewardsUnitVectors(controlpointsX, controlpointsY, queryPositions, closed, splineMatrix1Der);
     * Tensor sideLimits = Tensors.vector((i)->getSideLimits(positions.get(i), sideVectors.get(i)),positions.length());
     * } */
    System.out.println("Iterate " + iterations + " times!");
    for (int i = 0; i < iterations; ++i) {
      Tensor corr = getCorrectionVectors(controlpointsX, controlpointsY, radiusCtrPoints, queryPositions, splineMatrix, splineMatrix1Der, resolution, closed);
      if (Objects.isNull(corr))
        return null;
      radiusCtrPoints = radiusCtrPoints.add(splineMatrixTransp.dot(corr.get(2)));
      controlpointsX = controlpointsX.add(splineMatrixTransp.dot(corr.get(0)));
      controlpointsY = controlpointsY.add(splineMatrixTransp.dot(corr.get(1)));
      final Tensor fControl = radiusCtrPoints;
      // TODO JPH/MH simpler way to add scalar
      radiusCtrPoints = Tensors.vector(ii -> fControl.get(ii).add(gdRadiusGrowth), radiusCtrPoints.length());
      controlpointsX = controlpointsX.add(Regularization.of(controlpointsX, gdRegularizer, closed));
      controlpointsY = controlpointsY.add(Regularization.of(controlpointsY, gdRegularizer, closed));
      radiusCtrPoints = radiusCtrPoints.add(Regularization.of(radiusCtrPoints, gdRegularizer, closed));
      if (Objects.nonNull(constraints))
        for (TrackConstraint constraint : constraints) {
          constraint.compute(controlpointsX, controlpointsY, radiusCtrPoints);
          controlpointsX = constraint.getControlPointsX();
          controlpointsY = constraint.getControlPointsY();
          radiusCtrPoints = constraint.getRadiusControlPoints();
        }
    }
    // MPCBSplineTrack track = new MPCBSplineTrack(controlpointsX, controlpointsY, radiusCtrPoints);
    return Tensors.of(controlpointsX, controlpointsY, radiusCtrPoints);
  }

  // for debugging
  // TODO JPH/MH not used
  private static final Scalar defaultRadius = Quantity.of(1, SI.METER);
  private static final Scalar gdLimits = RealScalar.of(0.4);
  private static final Scalar gdRadius = RealScalar.of(0.8);
  private List<Tensor> freeLines = new ArrayList<>();

  private Tensor getCorrectionVectors(Tensor controlpointsX, Tensor controlpointsY, Tensor radiusControlPoints, Tensor queryPositions, Tensor basisMatrix,
      Tensor basisMatrix1Der, Scalar resolution, boolean closed) {
    Tensor positions = BSplineUtil.getPositions(controlpointsX, controlpointsY, basisMatrix);
    Tensor sideVectors = BSplineUtil.getSidewardsUnitVectors(controlpointsX, controlpointsY, basisMatrix1Der);
    Tensor radii = basisMatrix.dot(radiusControlPoints);
    Scalar stepsSize = Quantity.of(0.1, SI.METER);
    freeLines = new ArrayList<>();
    Tensor sideLimits = Tensors.vector(i -> getSideLimits(positions.get(i), sideVectors.get(i), stepsSize, Quantity.of(1, SI.METER)), positions.length());
    boolean hasNoSolution = sideLimits.stream().anyMatch(row -> row.get(0).equals(row.get(1)));
    if (hasNoSolution)
      return null;
    // upwardsforce
    Tensor lowClipping = Tensors.vector(i -> Max.of(sideLimits.get(i).Get(0).add(radii.Get(i)), Quantity.of(0, SI.METER)), queryPositions.length());
    Tensor highClipping = Tensors.vector(i -> Max.of(radii.Get(i).subtract(sideLimits.get(i).Get(1)), Quantity.of(0, SI.METER)), queryPositions.length());
    Tensor sideCorr = lowClipping.subtract(highClipping).multiply(gdLimits.divide(resolution));
    Tensor posCorr = Transpose.of(sideCorr.pmul(sideVectors));
    Tensor radiusCorr = highClipping.add(lowClipping).multiply(gdRadius.divide(resolution)).negate();
    // Tensor upwardsforce = Tensors.vector(list)
    return Tensors.of(posCorr.get(0), posCorr.get(1), radiusCorr);
  }

  private Tensor getSideLimits(Tensor pos, Tensor sidedir, Scalar stepsSize, Scalar maxSearch) {
    // find free space
    Scalar sideStep = Quantity.of(-0.001, SI.METER);
    Tensor testPosition = null;
    Tensor lowPosition;
    Tensor highPosition;
    boolean occupied = true;
    while (occupied) {
      if (Scalars.lessThan(sideStep, Quantity.of(0, SI.METER)))
        sideStep = sideStep.negate();
      else
        sideStep = sideStep.add(stepsSize).negate();
      testPosition = pos.add(sidedir.multiply(sideStep));
      occupied = occupancyGrid.isMember(testPosition);
      if (Scalars.lessThan(maxSearch, Abs.of(sideStep)))
        return Tensors.of(RealScalar.ZERO, RealScalar.ZERO);
    }
    // search in both directions for occupied cell
    // only for debugging
    Tensor freeline = Tensors.empty();
    // negative direction
    while (!occupied) {
      sideStep = sideStep.subtract(stepsSize);
      testPosition = pos.add(sidedir.multiply(sideStep));
      occupied = occupancyGrid.isMember(testPosition);
    }
    freeline.append(testPosition);
    lowPosition = sideStep;
    // negative direction
    occupied = false;
    while (!occupied) {
      sideStep = sideStep.add(stepsSize);
      testPosition = pos.add(sidedir.multiply(sideStep));
      occupied = occupancyGrid.isMember(testPosition);
    }
    highPosition = sideStep;
    freeline.append(testPosition);
    freeLines.add(freeline);
    return Tensors.of(lowPosition, highPosition);
  }
}
