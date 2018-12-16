// code by mh
package ch.ethz.idsc.gokart.core.sound;

import ch.ethz.idsc.gokart.core.sound.GokartSoundCreator.Exciter;
import ch.ethz.idsc.gokart.core.sound.GokartSoundCreator.MotorState;

public class TestExciter extends Exciter {
  private static final float TWO_PI = (float) (2 * Math.PI);
  // ---
  private final float absFrequency;
  private final float relFrequency;
  private final float powerFactor; // TODO MH not used
  private float sinePosition = 0;
  private float dsinePosition;

  public TestExciter(float absFrequency, float relFrequency, float powerFactor) {
    this.absFrequency = absFrequency;
    this.relFrequency = relFrequency;
    this.powerFactor = powerFactor;
  }

  @Override
  public float getNextValue(MotorState state, float dt) {
    dsinePosition = dt * (state.speed * relFrequency + absFrequency);
    sinePosition += dsinePosition;
    if (sinePosition > TWO_PI) {
      sinePosition -= TWO_PI;
      return 1;
    }
    return 0;
  }
}
