// code by jph
package ch.ethz.idsc.gokart.gui.top;

import java.awt.Color;
import java.awt.Graphics2D;

import ch.ethz.idsc.gokart.calib.ChassisGeometry;
import ch.ethz.idsc.gokart.calib.steer.SteerColumnEvent;
import ch.ethz.idsc.gokart.calib.steer.SteerColumnEvents;
import ch.ethz.idsc.gokart.calib.steer.SteerColumnListener;
import ch.ethz.idsc.gokart.calib.steer.SteerMapping;
import ch.ethz.idsc.gokart.core.pos.GokartPoseEvent;
import ch.ethz.idsc.gokart.core.pos.GokartPoseEvents;
import ch.ethz.idsc.gokart.core.pos.GokartPoseListener;
import ch.ethz.idsc.gokart.dev.steer.SteerConfig;
import ch.ethz.idsc.owl.bot.se2.Se2CarIntegrator;
import ch.ethz.idsc.owl.bot.se2.glc.Se2CarFlows;
import ch.ethz.idsc.owl.gui.RenderInterface;
import ch.ethz.idsc.owl.gui.win.GeometricLayer;
import ch.ethz.idsc.owl.math.flow.Flow;
import ch.ethz.idsc.owl.math.state.FixedStateIntegrator;
import ch.ethz.idsc.owl.math.state.StateIntegrator;
import ch.ethz.idsc.owl.math.state.StateTime;
import ch.ethz.idsc.retina.util.math.Magnitude;
import ch.ethz.idsc.retina.util.pose.PoseHelper;
import ch.ethz.idsc.sophus.lie.se2.Se2Matrix;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Sign;

/** draw blue lines of prediction of traces of gokart extruded footprint */
public class ExtrudedFootprintRender implements RenderInterface {
  private static final StateIntegrator STATE_INTEGRATOR = FixedStateIntegrator.create( //
      Se2CarIntegrator.INSTANCE, RationalScalar.of(1, 4), 4 * 5);
  // ---
  private GokartPoseEvent gokartPoseEvent = GokartPoseEvents.motionlessUninitialized();
  public final GokartPoseListener gokartPoseListener = getEvent -> gokartPoseEvent = getEvent;
  // ---
  private SteerColumnEvent steerColumnEvent = SteerColumnEvents.UNKNOWN;
  public final SteerColumnListener steerColumnListener = getEvent -> steerColumnEvent = getEvent;
  // ---
  private final SteerMapping steerMapping = SteerConfig.GLOBAL.getSteerMapping();
  public Color color = new Color(64 + 32, 64 + 32, 255, 128 - 64);

  @Override // from RenderInterface
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    if (steerColumnEvent.isSteerColumnCalibrated()) {
      geometricLayer.pushMatrix(PoseHelper.toSE2Matrix(gokartPoseEvent.getPose()));
      // ---
      Scalar XAD = ChassisGeometry.GLOBAL.xAxleDistanceMeter(); // axle distance
      Scalar YHW = ChassisGeometry.GLOBAL.yHalfWidthMeter(); // half width
      final Tensor p1;
      final Tensor p2;
      final Scalar ratio = steerMapping.getRatioFromSCE(steerColumnEvent); // <- calibration checked
      if (Sign.isPositive(ratio)) {
        p1 = Tensors.of(RealScalar.ZERO, YHW, RealScalar.ONE);
        p2 = Tensors.of(XAD, YHW.negate(), RealScalar.ONE);
      } else {
        p1 = Tensors.of(XAD, YHW, RealScalar.ONE);
        p2 = Tensors.of(RealScalar.ZERO, YHW.negate(), RealScalar.ONE);
      }
      // center of rear axle
      StateTime CENTER = new StateTime(Tensors.of(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO), RealScalar.ZERO);
      {
        // System.out.println(ratio);
        final Flow flow_forward = Se2CarFlows.singleton(RealScalar.ONE, Magnitude.PER_METER.apply(ratio));
        final Tensor center_forward = //
            Tensor.of(STATE_INTEGRATOR.trajectory(CENTER, flow_forward).stream().map(StateTime::state));
        Tensor w1 = Tensors.empty();
        Tensor w2 = Tensors.empty();
        for (Tensor x : center_forward) {
          // TODO JPH there should be something more efficient available!
          Tensor pose = Se2Matrix.of(x);
          w1.append(pose.dot(p1));
          w2.append(pose.dot(p2));
        }
        graphics.setColor(color);
        graphics.draw(geometricLayer.toPath2D(w1));
        graphics.draw(geometricLayer.toPath2D(w2));
      }
      {
        final Flow flow_reverse = Se2CarFlows.singleton(RealScalar.ONE.negate(), Magnitude.PER_METER.apply(ratio));
        final Tensor center_reverse = //
            Tensor.of(STATE_INTEGRATOR.trajectory(CENTER, flow_reverse).stream().map(StateTime::state));
        Tensor w1 = Tensors.empty();
        Tensor w2 = Tensors.empty();
        for (Tensor x : center_reverse) {
          Tensor pose = Se2Matrix.of(x);
          w1.append(pose.dot(p1));
          w2.append(pose.dot(p2));
        }
        graphics.setColor(color);
        graphics.draw(geometricLayer.toPath2D(w1));
        graphics.draw(geometricLayer.toPath2D(w2));
      }
      geometricLayer.popMatrix();
    }
  }
}
