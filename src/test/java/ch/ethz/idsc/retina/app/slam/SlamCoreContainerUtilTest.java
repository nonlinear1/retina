// code by jph
package ch.ethz.idsc.retina.app.slam;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class SlamCoreContainerUtilTest extends TestCase {
  public void testSimple() {
    SlamParticle[] slamParticles = SlamParticles.allocate(300);
    Tensor pose = Tensors.fromString("{3.4[m], 5.6[m], 3.345}");
    SlamCoreContainerUtil.setInitialDistribution(slamParticles, pose);
    for (int index = 0; index < 10; ++index) {
      Scalar angVel = slamParticles[index].getAngVel();
      Clips.absoluteOne().requireInside(angVel);
    }
  }
}
