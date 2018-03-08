// code by jph
package ch.ethz.idsc.gokart.gui.top;

import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.alg.VectorQ;
import junit.framework.TestCase;

public class SensorsConfigTest extends TestCase {
  public void testSimple() {
    VectorQ.ofLength(SensorsConfig.GLOBAL.urg04lx, 3);
    VectorQ.ofLength(SensorsConfig.GLOBAL.vlp16, 3);
  }

  public void testVlp16FrontFacing() {
    assertTrue(Scalars.isZero(SensorsConfig.GLOBAL.vlp16.Get(2)));
  }
}
