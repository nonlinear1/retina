// code by jph
package ch.ethz.idsc.gokart.core.man;

import ch.ethz.idsc.gokart.dev.u3.StaticManualControlProvider;
import ch.ethz.idsc.retina.joystick.ManualControlProvider;
import ch.ethz.idsc.retina.util.math.NonSI;
import ch.ethz.idsc.retina.util.math.SI;
import ch.ethz.idsc.retina.util.sys.AppResources;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.ref.FieldClip;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Clips;

/** parameters for PI controller of torque control */
public class ManualConfig {
  public static final ManualConfig GLOBAL = AppResources.load(new ManualConfig());
  /***************************************************/
  /** the physical maximum torque limit is 2316[ARMS]
   * the torque limit is used in {@link RimoThrustManualModule} */
  @FieldClip(min = "0[ARMS]", max = "2315[ARMS]")
  public final Scalar torqueLimit = Quantity.of(2315, NonSI.ARMS);
  public final Scalar timeout = Quantity.of(0.2, SI.SECOND);
  /** torquePerGyro factor is used in {@link DriftThrustManualModule} */
  public Scalar torquePerGyro = Quantity.of(-2, SI.SECOND);
  /** when should drift be avoided */
  public Scalar driftAvoidStart = RealScalar.of(0.5);
  /** how strong should the drift be avoided */
  public Scalar driftAvoidRamp = RealScalar.of(5);

  /***************************************************/
  /** @return clip interval for permitted torque */
  public Clip torqueLimitClip() {
    return Clips.absolute(torqueLimit);
  }

  /** .
   * ante 20181211: the GenericXboxPad joystick was used
   * post 20181211: throttle pedal and boost button
   * 
   * @return manual control as configured on the gokart */
  public ManualControlProvider getProvider() {
    return StaticManualControlProvider.INSTANCE;
  }
}
