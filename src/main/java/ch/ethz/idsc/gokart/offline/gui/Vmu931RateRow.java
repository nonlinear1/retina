// code by jph
package ch.ethz.idsc.gokart.offline.gui;

import ch.ethz.idsc.retina.imu.vmu931.Vmu931ImuFrame;
import ch.ethz.idsc.retina.imu.vmu931.Vmu931ImuFrameListener;
import ch.ethz.idsc.retina.util.math.SI;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.img.ColorDataGradient;
import ch.ethz.idsc.tensor.img.ColorDataGradients;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Clips;

/* package */ class Vmu931RateRow extends ClipLogImageRow implements Vmu931ImuFrameListener {
  private static final Clip CLIP = Clips.positive(Quantity.of(1_000, SI.PER_SECOND));
  // ---
  private Scalar scalar = RealScalar.ZERO;

  @Override // from Vmu931ImuFrameListener
  public void vmu931ImuFrame(Vmu931ImuFrame vmu931ImuFrame) {
    scalar = scalar.add(RealScalar.ONE);
  }

  @Override // from GokartLogImageRow
  public Scalar getScalar() {
    Scalar value = scalar;
    scalar = RealScalar.ZERO;
    return CLIP.rescale(value.divide(GokartLogFileIndexer.RESOLUTION));
  }

  @Override // from GokartLogImageRow
  public ColorDataGradient getColorDataGradient() {
    return ColorDataGradients.STARRYNIGHT;
  }

  @Override // from GokartLogImageRow
  public String getName() {
    return "vmu931 rate";
  }

  @Override
  public Clip clip() {
    return CLIP;
  }
}
