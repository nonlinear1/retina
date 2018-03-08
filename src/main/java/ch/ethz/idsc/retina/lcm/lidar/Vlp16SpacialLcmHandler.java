// code by jph
package ch.ethz.idsc.retina.lcm.lidar;

import ch.ethz.idsc.retina.dev.lidar.LidarSpacialProvider;
import ch.ethz.idsc.retina.dev.lidar.VelodyneDecoder;
import ch.ethz.idsc.retina.dev.lidar.VelodyneModel;
import ch.ethz.idsc.retina.dev.lidar.vlp16.Vlp16Decoder;
import ch.ethz.idsc.retina.dev.lidar.vlp16.Vlp16SpacialProvider;
import ch.ethz.idsc.retina.lcm.LcmClientInterface;

public class Vlp16SpacialLcmHandler implements LcmClientInterface {
  private final VelodyneLcmClient velodyneLcmClient;
  public final LidarSpacialProvider lidarSpacialProvider = new Vlp16SpacialProvider(-1.61); // FIXME

  public Vlp16SpacialLcmHandler(String lidarId) {
    VelodyneModel velodyneModel = VelodyneModel.VLP16;
    VelodyneDecoder velodyneDecoder = new Vlp16Decoder();
    velodyneLcmClient = new VelodyneLcmClient(velodyneModel, velodyneDecoder, lidarId);
    velodyneDecoder.addRayListener(lidarSpacialProvider);
  }

  @Override
  public void startSubscriptions() {
    velodyneLcmClient.startSubscriptions();
  }

  @Override
  public void stopSubscriptions() {
    velodyneLcmClient.stopSubscriptions();
  }
}
