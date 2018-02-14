// code by jph
package ch.ethz.idsc.retina.gui.gokart.top;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.SquareMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2GridPointTest extends TestCase {
  public void testSimple() {
    Se2GridPoint se2GridPoint = new Se2GridPoint(DoubleScalar.of(0.8), DoubleScalar.of(0.2), 1, 2, -3);
    assertEquals(se2GridPoint.index(), Tensors.vector(1, 2, -3));
    assertTrue(Chop._10.close(se2GridPoint.tangent(), Tensors.vector(0.8, 1.6, -0.6)));
    assertTrue(SquareMatrixQ.of(se2GridPoint.matrix()));
  }
}
