// code by jph
package ch.ethz.idsc.gokart.gui;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.gokart.calib.ChassisGeometry;
import ch.ethz.idsc.gokart.calib.SensorsConfig;
import ch.ethz.idsc.gokart.calib.brake.BrakeFunctionConfig;
import ch.ethz.idsc.gokart.calib.steer.HighPowerSteerPid;
import ch.ethz.idsc.gokart.core.adas.AntilockConfig;
import ch.ethz.idsc.gokart.core.adas.HapticSteerConfig;
import ch.ethz.idsc.gokart.core.fuse.SafetyConfig;
import ch.ethz.idsc.gokart.core.man.ManualConfig;
import ch.ethz.idsc.gokart.core.map.MappingConfig;
import ch.ethz.idsc.gokart.core.map.OccupancyConfig;
import ch.ethz.idsc.gokart.core.mpc.MPCLudicConfig;
import ch.ethz.idsc.gokart.core.mpc.MPCOptimizationConfig;
import ch.ethz.idsc.gokart.core.plan.TrajectoryConfig;
import ch.ethz.idsc.gokart.core.pure.ClothoidPursuitConfig;
import ch.ethz.idsc.gokart.core.pure.PurePursuitConfig;
import ch.ethz.idsc.gokart.core.slam.LocalizationConfig;
import ch.ethz.idsc.gokart.core.tvec.TorqueVectoringConfig;
import ch.ethz.idsc.gokart.dev.linmot.LinmotConfig;
import ch.ethz.idsc.gokart.dev.rimo.RimoConfig;
import ch.ethz.idsc.gokart.dev.steer.SteerConfig;
import ch.ethz.idsc.gokart.dev.steer.SteerPid;

/* package */ enum ParametersHelper {
  ;
  public static final List<Object> OBJECTS = Arrays.asList(//
      ChassisGeometry.GLOBAL, //
      SensorsConfig.GLOBAL, //
      LinmotConfig.GLOBAL, //
      SteerConfig.GLOBAL, //
      SteerPid.GLOBAL, //
      HapticSteerConfig.GLOBAL, //
      AntilockConfig.GLOBAL, //
      HighPowerSteerPid.GLOBAL, //
      RimoConfig.GLOBAL, //
      SafetyConfig.GLOBAL, //
      LocalizationConfig.GLOBAL, //
      ManualConfig.GLOBAL, //
      ClothoidPursuitConfig.GLOBAL, //
      PurePursuitConfig.GLOBAL, //
      TorqueVectoringConfig.GLOBAL, //
      MPCOptimizationConfig.GLOBAL, //
      MPCLudicConfig.GLOBAL, //
      // ClusterConfig.GLOBAL, //
      TrajectoryConfig.GLOBAL, //
      // PlanSRConfig.GLOBAL, //
      MappingConfig.GLOBAL, //
      OccupancyConfig.GLOBAL, //
      // DavisSlamPrcConfig.GLOBAL, //
      // DavisSlamCoreConfig.GLOBAL, //
      // PIDTuningParams.GLOBAL, //
      // MPCActiveCompensationLearningConfig.GLOBAL, //
      BrakeFunctionConfig.GLOBAL);
}
