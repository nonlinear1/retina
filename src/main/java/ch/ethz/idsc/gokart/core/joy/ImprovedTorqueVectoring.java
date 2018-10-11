// code by mh
package ch.ethz.idsc.gokart.core.joy;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Sign;

public class ImprovedTorqueVectoring implements TorqueVectoringInterface{
  private final TorqueVectoringConfig torqueVectoringConfig;

  public ImprovedTorqueVectoring(TorqueVectoringConfig torqueVectoringConfig) {
    this.torqueVectoringConfig = torqueVectoringConfig;
  }


  public Tensor powers(Scalar expectedRotationPerMeterDriven, Scalar meanTangentSpeed, Scalar angularSlip, Scalar power, Scalar realRotation) {
    // compute differential torque (in Arms as we do not use the power function yet)
    Scalar dynamicComponent = angularSlip.multiply(torqueVectoringConfig.dynamicCorrection);
    // System.out.println("Dynamic component: " + dynamicComponent);
    Scalar lateralAcceleration = Times.of(expectedRotationPerMeterDriven, meanTangentSpeed, meanTangentSpeed);
    // System.out.println("lateral Acceleration: " + lateralAcceleration);
    Scalar staticComponent = lateralAcceleration.multiply(torqueVectoringConfig.staticCompensation);
    // System.out.println("Static component: " + staticComponent);
    Scalar wantedZTorque = dynamicComponent.add(staticComponent); // One
    // do we want to break the back axle lose?
    // if we want to stabilize an oversteering gokart, we should have no differential thrust
    // do we want to stabilize?
    if (Sign.isNegative(realRotation.multiply(wantedZTorque))) {
      Scalar s = Clip.unit().apply(realRotation.abs().multiply(torqueVectoringConfig.ks));
      Scalar stabilizerFactor = RealScalar.ONE.subtract(s);
      wantedZTorque = wantedZTorque.multiply(stabilizerFactor);
    }
    // System.out.println("ZTorque: " + wantedZTorque);
    // left and right power
    Scalar powerLeft = power.subtract(wantedZTorque); // One
    Scalar powerRight = power.add(wantedZTorque); // One
    // prefer power over Z-torque
    return SimpleTorqueVectoring.clip(powerLeft, powerRight);
  }
}
