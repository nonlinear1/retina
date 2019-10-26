// code by jph
package ch.ethz.idsc.demo.jph.lidar.local;

import java.io.File;

import ch.ethz.idsc.gokart.core.slam.LocalizationConfig;
import ch.ethz.idsc.gokart.offline.api.GokartLogInterface;
import ch.ethz.idsc.gokart.offline.pose.LidarLocalizationOffline;
import ch.ethz.idsc.gokart.offline.pose.LogPosePostInject;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/* package */ enum NewPosePostInjectSingle {
  ;
  public static void in(GokartLogInterface gokartLogInterface) throws Exception {
    File file = gokartLogInterface.file();
    in(file, gokartLogInterface.pose(), new File(file.getParentFile(), "post.lcm"));
  }

  public static void in(File origin, Tensor pose, File target) throws Exception {
    // final File post_lcm = HomeDirectory.file(StaticHelper.POST_LCM);
    LocalizationConfig localizationConfig = new LocalizationConfig();
    localizationConfig.qualityMin = RealScalar.ZERO;
    LidarLocalizationOffline lidarLocalizationOffline = //
        new LidarLocalizationOffline(localizationConfig, pose);
    LogPosePostInject logPosePostInject = new LogPosePostInject();
    lidarLocalizationOffline.gokartPoseListeners.add(logPosePostInject);
    logPosePostInject.process(origin, target, lidarLocalizationOffline);
  }

  public static void main(String[] args) throws Exception {
    // SensorsConfig.GLOBAL.planarVmu931Type = PlanarVmu931Type.FLIPPED.name();
    // GokartLogInterface gokartLogInterface = GokartLogAdapter.of(new File("/media/datahaki/data/gokart/cuts/20190328/20190328T164433_01"));
    File source = new File("/media/datahaki/data/gokart/localize/20191022T120450_00", "log.lcm");
    // new File("/media/datahaki/data/gokart/tokio/20190310/20190310T220933_00", "log.lcm");
    // Optional<ByteBuffer> optional = FirstLogMessage.of(source, GokartPoseChannel.INSTANCE.channel());
    in( //
        source, //
        // DatahakiLogFileLocator.file(GokartLogFile._20190418T145229_b6a70baf), //
        Tensors.fromString("{52.87219[m], 58.71351[m], -2.48135}"), //
        // GokartPoseEvent.of(optional.get()).getPose(), //
        new File(source.getParentFile(), "post.lcm"));
  }
}
