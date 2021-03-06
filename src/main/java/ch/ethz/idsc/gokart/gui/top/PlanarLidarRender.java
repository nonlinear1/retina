// code by jph
package ch.ethz.idsc.gokart.gui.top;

import java.awt.Graphics2D;

import ch.ethz.idsc.owl.gui.win.GeometricLayer;
import ch.ethz.idsc.sophus.lie.se2.Se2Matrix;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** draw polygon */
class PlanarLidarRender extends LidarRender {
  private static final Tensor ORIGIN = Tensors.vectorDouble(0, 0).unmodifiable();

  @Override
  public void protected_render(GeometricLayer geometricLayer, Graphics2D graphics) {
    geometricLayer.pushMatrix(Se2Matrix.of(supplier.get()));
    Tensor points = _points.copy();
    points.append(ORIGIN);
    graphics.setColor(color);
    graphics.fill(geometricLayer.toPath2D(points));
    geometricLayer.popMatrix();
  }
}
