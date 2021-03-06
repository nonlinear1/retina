// code by jph
package ch.ethz.idsc.retina.util.math;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** implementation is immutable and thread safe */
public class AxisAlignedBox implements Serializable {
  private static final Scalar ZERO = RealScalar.of(0.0);
  private static final Scalar HALF = RealScalar.of(0.5);
  // ---
  private final Scalar pos;
  private final Scalar neg;

  /** @param width of box orthogonal to direction of given value */
  public AxisAlignedBox(Scalar width) {
    pos = width.multiply(HALF);
    neg = pos.negate();
  }

  public Tensor alongX(Scalar value) {
    return Tensors.matrix(new Scalar[][] { //
        { ZERO, neg }, //
        { value, neg }, //
        { value, pos }, //
        { ZERO, pos } });
  }

  public Tensor alongY(Scalar value) {
    return Tensors.matrix(new Scalar[][] { //
        { pos, ZERO }, //
        { pos, value }, //
        { neg, value }, //
        { neg, ZERO } //
    });
  }
}
