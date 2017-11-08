// code by jph
package ch.ethz.idsc.retina.demo.az;

import java.io.IOException;

import ch.ethz.idsc.tensor.RationalScalar;
import lcm.logging.LogPlayer;
import lcm.logging.LogPlayerConfig;

enum DavisLcmLogViewer {
  ;
  public static void main(String[] args) throws IOException {
    LogPlayerConfig cfg = new LogPlayerConfig();
    cfg.logFile = "/home/ale/datasets/zuriscapes/rec_TRAINlong_lcm/lcmlog-2017-11-08.09";
    cfg.speed = RationalScalar.of(1, 4);
    LogPlayer.create(cfg);
  }
}
