// code by jph and mh
package ch.ethz.idsc.gokart.core.man;

import java.util.Optional;

import ch.ethz.idsc.gokart.calib.power.PowerLookupTable;
import ch.ethz.idsc.gokart.calib.steer.RimoTwdOdometry;
import ch.ethz.idsc.gokart.dev.rimo.RimoGetEvent;
import ch.ethz.idsc.gokart.dev.rimo.RimoGetListener;
import ch.ethz.idsc.gokart.dev.rimo.RimoPutEvent;
import ch.ethz.idsc.gokart.dev.rimo.RimoPutHelper;
import ch.ethz.idsc.gokart.dev.rimo.RimoPutTires;
import ch.ethz.idsc.gokart.dev.rimo.RimoSocket;
import ch.ethz.idsc.gokart.dev.steer.SteerColumnInterface;
import ch.ethz.idsc.retina.joystick.ManualControlInterface;
import ch.ethz.idsc.retina.util.math.Magnitude;
import ch.ethz.idsc.retina.util.math.SI;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.qty.Quantity;

public class LookupTableRimoThrustManualModule extends GuideManualModule<RimoPutEvent> implements RimoGetListener {
  private final PowerLookupTable powerLookupTable = PowerLookupTable.getInstance();
  private Scalar meanTangentSpeed = Quantity.of(0, SI.VELOCITY);

  @Override // from AbstractModule
  protected void first() {
    RimoSocket.INSTANCE.addPutProvider(this);
    RimoSocket.INSTANCE.addGetListener(this);
  }

  @Override // from AbstractModule
  protected void last() {
    RimoSocket.INSTANCE.removePutProvider(this);
    RimoSocket.INSTANCE.removeGetListener(this);
  }

  /***************************************************/
  @Override // from GuideJoystickModule
  Optional<RimoPutEvent> control( //
      SteerColumnInterface steerColumnInterface, ManualControlInterface manualControlInterface) {
    Scalar pair = Differences.of(manualControlInterface.getAheadPair_Unit()).Get(0);
    // get the wanted acceleration
    Scalar wantedAcceleration = powerLookupTable.getNormalizedAccelerationTorqueCentered(pair, meanTangentSpeed);
    // get current
    Scalar current = powerLookupTable.getNeededCurrent(wantedAcceleration, meanTangentSpeed);
    short arms_raw = Magnitude.ARMS.toShort(current); // confirm that units are correct
    if (RimoPutTires.isTorqueValid(arms_raw))
      return Optional.of(RimoPutHelper.operationTorque( //
          (short) -arms_raw, // sign left invert
          (short) +arms_raw // sign right id
      ));
    System.err.println("out of range: arms_raw=" + arms_raw);
    return Optional.empty();
  }

  @Override // from RimoGetListener
  public final void getEvent(RimoGetEvent rimoGetEvent) {
    meanTangentSpeed = RimoTwdOdometry.tangentSpeed(rimoGetEvent);
  }
}
