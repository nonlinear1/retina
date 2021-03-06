// code by jph and am
package ch.ethz.idsc.gokart.core.adas;

import java.util.Optional;

import ch.ethz.idsc.gokart.calib.SensorsConfig;
import ch.ethz.idsc.gokart.calib.steer.RimoTireConfiguration;
import ch.ethz.idsc.gokart.dev.linmot.LinmotPutEvent;
import ch.ethz.idsc.gokart.dev.linmot.LinmotPutOperation;
import ch.ethz.idsc.gokart.dev.linmot.LinmotSocket;
import ch.ethz.idsc.gokart.dev.rimo.RimoSocket;
import ch.ethz.idsc.gokart.gui.GokartLcmChannel;
import ch.ethz.idsc.gokart.lcm.BinaryBlobPublisher;
import ch.ethz.idsc.gokart.lcm.VectorFloatBlob;
import ch.ethz.idsc.gokart.lcm.imu.Vmu931ImuLcmClient;
import ch.ethz.idsc.retina.imu.vmu931.Vmu931ImuFrame;
import ch.ethz.idsc.retina.imu.vmu931.Vmu931ImuFrameListener;
import ch.ethz.idsc.retina.joystick.ManualControlInterface;
import ch.ethz.idsc.retina.util.math.SI;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Round;

/** class is used to develop and test anti lock brake logic */
public class AntilockBrakeModule extends AntilockBrakeBaseModule implements Vmu931ImuFrameListener {
  private final BinaryBlobPublisher binaryBlobPublisher = new BinaryBlobPublisher(GokartLcmChannel.LINMOT_ANTILOCK);
  private final Vmu931ImuLcmClient vmu931imuLcmClient = new Vmu931ImuLcmClient();
  private Scalar currentAcceleration = Quantity.of(0, SI.ACCELERATION);

  public AntilockBrakeModule() {
    super(AntilockConfig.GLOBAL);
  }

  public AntilockBrakeModule(AntilockConfig hapticSteerConfig) {
    super(hapticSteerConfig);
  }

  @Override // from AbstractModule
  protected void first() {
    vmu931imuLcmClient.addListener(this);
    vmu931imuLcmClient.startSubscriptions();
    LinmotSocket.INSTANCE.addPutProvider(this);
    RimoSocket.INSTANCE.addGetListener(rimoGetListener);
  }

  @Override // from AbstractModule
  protected void last() {
    RimoSocket.INSTANCE.removeGetListener(rimoGetListener);
    LinmotSocket.INSTANCE.removePutProvider(this);
    vmu931imuLcmClient.stopSubscriptions();
  }

  @Override
  public Optional<LinmotPutEvent> putEvent() {
    Optional<ManualControlInterface> optional = manualControlProvider.getManualControl();
    if (optional.isPresent()) {
      ManualControlInterface manualControlInterface = optional.get();
      if (manualControlInterface.isAutonomousPressed() && lidarLocalizationModule != null && currentAcceleration != null) {
        return smartBraking(rimoGetEvent.getAngularRate_Y_pair(), lidarLocalizationModule.getVelocity());
      }
      // reset to full Braking value for next braking maneuvre
      brakePosition = antilockConfig.fullBraking;
    }
    return Optional.empty();
  }

  /** @param angularRate_Y_pair
   * @param velocityOrigin
   * @return braking command with suitable relative position */
  Optional<LinmotPutEvent> smartBraking(Tensor angularRate_Y_pair, Tensor velocityOrigin) {
    Scalar angularRate_Origin = velocityOrigin.Get(0).divide(RimoTireConfiguration._REAR.radius());
    Tensor angularRate_Origin_pair = Tensors.of(angularRate_Origin, angularRate_Origin);
    Tensor slip = angularRate_Origin_pair.subtract(angularRate_Y_pair); // vector of length 2 with entries of unit [s^-1]
    binaryBlobPublisher.accept(VectorFloatBlob.encode(Flatten.of(Tensors.of( //
        slip, brakePosition, velocityOrigin.Get(0), angularRate_Origin, angularRate_Y_pair, currentAcceleration))));
    System.out.println(slip.map(Round._3) + " " + brakePosition + " " + currentAcceleration + " " + velocityOrigin.Get(0).map(Round._3));
    // the brake cannot be constantly applied otherwise the brake motor heats up too much
    // there is a desired range for slip (in theory 0.1-0.25)
    // if the slip is outside this range, the position of the brake is increased/decreased
    // if (hapticSteerConfig.slipClip().isOutside(slip.Get(0)))
    for (int i = 0; i < 2; ++i) {
      if (Scalars.lessThan(slip.Get(i), antilockConfig.minSlipTheory.multiply(angularRate_Origin))) {
        brakePosition = Clips.unit().apply(brakePosition.add(antilockConfig.incrBraking));
      }
      if (Scalars.lessThan(antilockConfig.maxSlipTheory.multiply(angularRate_Origin), slip.Get(i))) {
        brakePosition = Clips.unit().apply(brakePosition.subtract(antilockConfig.incrBraking));
      }
    }
    LinmotPutEvent relativePosition = LinmotPutOperation.INSTANCE.toRelativePosition(brakePosition);
    return Optional.of(relativePosition);
  }

  @Override // from Vmu931ImuFrameListener
  public void vmu931ImuFrame(Vmu931ImuFrame vmu931ImuFrame) {
    currentAcceleration = SensorsConfig.GLOBAL.getPlanarVmu931Imu().accXY(vmu931ImuFrame).Get(0);
  }
}
