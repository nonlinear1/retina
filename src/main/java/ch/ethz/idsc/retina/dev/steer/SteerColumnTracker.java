// code by jph
package ch.ethz.idsc.retina.dev.steer;

import ch.ethz.idsc.retina.dev.zhkart.fuse.SteerEmergencyModule;
import ch.ethz.idsc.retina.sys.SafetyCritical;
import ch.ethz.idsc.retina.util.math.IntervalTracker;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.qty.Quantity;

/** the magic constants defined in the class were found by experimentation
 * 
 * the most safety critical constant is HARD
 * it targets the case when the steering range interval exceeds all
 * previously observed values. in this driving may be dangerous because
 * for instance the concept of "straight ahead", i.e. angle == 0
 * cannot be commanded by the hardware.
 * Therefore, the case when the width of the interval tracker exceeds
 * the HARD threshold is considered an emergency */
@SafetyCritical
public final class SteerColumnTracker implements SteerGetListener, SteerColumnInterface {
  private static final double SOFT = 1.45;
  private static final double HARD = 1.6; // measured 1.538
  // ---
  private final IntervalTracker intervalTracker = new IntervalTracker();

  @Override
  public void getEvent(SteerGetEvent steerGetEvent) {
    intervalTracker.setValue(steerGetEvent.getGcpRelRckPos());
  }

  @Override // from SteerColumnInterface
  public boolean isSteerColumnCalibrated() {
    double width = intervalTracker.getWidth();
    return SOFT < width;
  }

  /** if {@link #isSteerColumnCalibrated()} returns true but {@link #isCalibratedAndHealthy()}
   * returns false, the steering and brake can still be maneuvered, but the gokart
   * should be stopped.
   * 
   * @return false if the interval tracker returns a value outside the nominal range
   * @see SteerEmergencyModule */
  public boolean isCalibratedAndHealthy() {
    return isSteerColumnCalibrated() && intervalTracker.getWidth() < HARD;
  }

  public double getIntervalWidth() {
    return intervalTracker.getWidth();
  }

  @Override // from SteerColumnInterface
  public Scalar getSteerColumnEncoderCentered() {
    if (!isSteerColumnCalibrated())
      throw new RuntimeException();
    return Quantity.of(intervalTracker.getValueCentered(), SteerPutEvent.UNIT_ENCODER);
  }
}