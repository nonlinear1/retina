// code by jph, ta
package ch.ethz.idsc.owl.car.slip;

import java.io.Serializable;

import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sin;

/** The Pacejka "Magic Formula" tire models
 * 
 * Pacejka3 depends on 3 parameters: B, C, D
 * 
 * https://en.wikipedia.org/wiki/Hans_B._Pacejka
 * 
 * Examples:
 * Time-Optimal Vehicle Posture Control to Mitigate Unavoidable
 * Collisions Using Conventional Control Inputs
 * B=7, C=1.4, D=1
 * 
 * Thesis report by MH, Section 5.1, p. 57
 * Gokart Front Tire: B=15, C=1.1, D=0.96
 * Gokart Rear Tire: B=5.2, C=1.4, D=1.06
 * 
 * Important: {@link PacejkaMagic} is not continuous for several input
 * Tensors.vector(0, 1);
 * Tensors.vector(0, 0); */
public class PacejkaMagic implements Serializable {
  private final Scalar B;
  private final Scalar C;
  private final Scalar D;

  public PacejkaMagic(Scalar B, Scalar C, Scalar D) {
    this.B = B;
    this.C = C;
    this.D = D;
  }

  /** @param B
   * @param C
   * @param D */
  public PacejkaMagic(double B, double C, double D) {
    this(RealScalar.of(B), RealScalar.of(C), RealScalar.of(D));
  }

  /** @param B
   * @param C */
  public PacejkaMagic(double B, double C) {
    this(B, C, 1);
  }

  /** the limit case has been established with Mathematica.
   * for reasonable pacejka constants, the "total" variable will evaluate to be positive.
   * due to the negate the final friction "mu" is then pointing against the ground speed {ux, uy} */
  public Scalar limit() {
    return D.multiply(Sin.of(C.multiply(Pi.HALF)));
  }

  public class sin implements ScalarUnaryOperator {
    @Override
    public Scalar apply(Scalar slip) {
      if (!NumberQ.of(slip))
        throw TensorRuntimeException.of(slip);
      return D.multiply(Sin.of(C.multiply(ArcTan.of(B.multiply(slip)))));
    }
  }

  public class cos implements ScalarUnaryOperator {
    @Override
    public Scalar apply(Scalar slip) {
      if (!NumberQ.of(slip))
        throw TensorRuntimeException.of(slip);
      return D.multiply(Cos.of(C.multiply(ArcTan.of(B.multiply(slip)))));
    }
  }
}
