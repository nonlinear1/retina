// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.lie.se2.Se2ParametricDistance;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

// TODO JPH OWL move to owl
public class DistancesTest extends TestCase {
  public void testR2() {
    Tensor tensor = Distances.of(RnMetric.INSTANCE, Tensors.fromString("{{1, 2}, {2, 2}, {2, 4}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.vector(1, 2));
  }

  public void testSe2() {
    Tensor tensor = Distances.of(Se2ParametricDistance.INSTANCE, Tensors.fromString("{{1, 2, 3}, {2, 2, 4}, {2, 4, 3}}"));
    Chop._12.requireClose(tensor, Tensors.vector(1.042914821466744, 2.085829642933488));
  }
}
