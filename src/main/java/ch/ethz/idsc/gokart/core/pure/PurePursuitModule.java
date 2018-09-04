// code by jph, mg
package ch.ethz.idsc.gokart.core.pure;

import java.util.Optional;

import ch.ethz.idsc.gokart.core.joy.JoystickConfig;
import ch.ethz.idsc.retina.dev.joystick.GokartJoystickInterface;
import ch.ethz.idsc.retina.dev.joystick.JoystickEvent;
import ch.ethz.idsc.retina.dev.steer.SteerConfig;
import ch.ethz.idsc.retina.lcm.joystick.JoystickLcmProvider;
import ch.ethz.idsc.retina.sys.AbstractClockedModule;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.sca.Clip;

public abstract class PurePursuitModule extends AbstractClockedModule {
  private final JoystickLcmProvider joystickLcmProvider = JoystickConfig.GLOBAL.createProvider();
  final PurePursuitRimo purePursuitRimo = new PurePursuitRimo();
  final PurePursuitSteer purePursuitSteer = new PurePursuitSteer();
  protected final Clip angleClip = SteerConfig.GLOBAL.getAngleLimit();

  @Override // from AbstractModule
  protected final void first() throws Exception {
    protected_first();
    joystickLcmProvider.startSubscriptions();
    purePursuitRimo.start();
    purePursuitSteer.start();
  }

  @Override // from AbstractModule
  protected final void last() {
    purePursuitRimo.stop();
    purePursuitSteer.stop();
    joystickLcmProvider.stopSubscriptions();
    protected_last();
  }

  protected abstract void protected_first() throws Exception;

  protected abstract void protected_last();

  @Override // from AbstractClockedModule
  protected final void runAlgo() {
    final Optional<JoystickEvent> joystick = joystickLcmProvider.getJoystick();
    final boolean isAutonomousPressed;
    if (joystick.isPresent()) { // is joystick button "autonomous" pressed?
      GokartJoystickInterface gokartJoystickInterface = (GokartJoystickInterface) joystick.get();
      isAutonomousPressed = gokartJoystickInterface.isAutonomousPressed();
    } else
      isAutonomousPressed = false;
    // ---
    Optional<Scalar> heading = deriveHeading();
    if (heading.isPresent())
      purePursuitSteer.setHeading(heading.get());
    // ---
    final boolean status = isAutonomousPressed && heading.isPresent();
    purePursuitSteer.setOperational(status);
    if (status) {
      GokartJoystickInterface gokartJoystickInterface = (GokartJoystickInterface) joystick.get();
      // ante 20180604: the ahead average was used in combination with Ramp
      Scalar ratio = gokartJoystickInterface.getAheadAverage(); // in [-1, 1]
      // post 20180604: the forward command is provided by right slider
      Scalar pair = Differences.of(gokartJoystickInterface.getAheadPair_Unit()).Get(0); // in [0, 1]
      // post 20180619: allow reverse driving
      Scalar speed = Clip.absoluteOne().apply(ratio.add(pair));
      purePursuitRimo.setSpeed(PursuitConfig.GLOBAL.rateFollower.multiply(speed));
    }
    purePursuitRimo.setOperational(status);
  }

  @Override // from AbstractClockedModule
  protected final Scalar getPeriod() {
    return PursuitConfig.GLOBAL.updatePeriod;
  }

  /** @return heading with unit "rad"
   * Optional.empty() if autonomous pure pursuit control is not warranted */
  protected abstract Optional<Scalar> deriveHeading();
}
