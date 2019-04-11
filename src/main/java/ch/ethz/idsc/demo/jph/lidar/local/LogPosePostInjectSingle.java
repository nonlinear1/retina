// code by jph
package ch.ethz.idsc.demo.jph.lidar.local;

import java.io.File;

import ch.ethz.idsc.gokart.core.pos.LocalizationConfig;
import ch.ethz.idsc.gokart.offline.api.GokartLogAdapter;
import ch.ethz.idsc.gokart.offline.api.GokartLogInterface;
import ch.ethz.idsc.gokart.offline.pose.LogPosePostInject;
import ch.ethz.idsc.gokart.offline.slam.LidarGyroOfflineLocalize;
import ch.ethz.idsc.gokart.offline.slam.OfflineLocalizeWrap;
import ch.ethz.idsc.gokart.offline.slam.VoidScatterImage;
import ch.ethz.idsc.tensor.Tensor;

/* package */ enum LogPosePostInjectSingle {
  ;
  public static OfflineLocalizeWrap of(Tensor pose) {
    return new OfflineLocalizeWrap(new LidarGyroOfflineLocalize( //
        LocalizationConfig.getPredefinedMap().getImageExtruded(), //
        pose, //
        LocalizationConfig.offlineSe2MultiresGrids(4), //
        VoidScatterImage.INSTANCE));
  }

  public static void in(File folder) throws Exception {
    GokartLogInterface gokartLogInterface = GokartLogAdapter.of(folder);
    final File post_lcm = new File(folder, StaticHelper.POST_LCM);
    if (post_lcm.isFile()) {
      // System.err.println("delete " + target);
      // target.delete();
      // System.out.println("skip " + folder);
    } else {
      OfflineLocalizeWrap lidarGyroPoseEstimator = of(gokartLogInterface.pose());
      LogPosePostInject logPosePostInject = new LogPosePostInject();
      // FIXME JPH
      // lidarGyroPoseEstimator.offlineLocalize.addListener(logPosePostInject);
      logPosePostInject.process(gokartLogInterface.file(), post_lcm, lidarGyroPoseEstimator);
    }
    if (post_lcm.isFile()) {
      final File source = new File(folder, StaticHelper.LOG_LCM);
      if (source.isFile() && source.length() <= post_lcm.length()) {
        source.delete();
        System.out.println("remove " + source);
      }
    }
  }
}