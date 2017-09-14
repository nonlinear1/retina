// code by jph
package ch.ethz.idsc.retina.lcm.davis;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ch.ethz.idsc.retina.dev.davis._240c.Davis240c;
import ch.ethz.idsc.retina.dev.davis._240c.DavisEventStatistics;
import ch.ethz.idsc.retina.dev.davis.app.AccumulatedOverlay;
import ch.ethz.idsc.retina.dev.davis.app.DavisImageBuffer;
import ch.ethz.idsc.retina.dev.davis.app.FirstImageTriggerExportControl;
import ch.ethz.idsc.retina.dev.davis.app.SignalResetDifference;
import ch.ethz.idsc.retina.dev.davis.io.DavisGifImageWriter;
import idsc.BinaryBlob;
import idsc.DavisImu;
import lcm.logging.Log;
import lcm.logging.Log.Event;

public class DavisLcmLogGifConvert {
  public static void of(final File file, final File target) {
    DavisLcmClient davisLcmClient = new DavisLcmClient(null);
    FirstImageTriggerExportControl fitec = new FirstImageTriggerExportControl();
    long count = 0;
    try {
      DavisEventStatistics davisEventStatistics = new DavisEventStatistics();
      davisLcmClient.davisDvsDatagramDecoder.addDvsListener(davisEventStatistics);
      // ---
      AccumulatedOverlay accumulatedOverlay = new AccumulatedOverlay(Davis240c.INSTANCE, 2000);
      // ---
      DavisImageBuffer davisImageBuffer = new DavisImageBuffer();
      davisLcmClient.davisRstDatagramDecoder.addListener(davisImageBuffer);
      davisLcmClient.davisRstDatagramDecoder.addListener(accumulatedOverlay.rst);
      // ---
      DavisGifImageWriter davisGifImageWriter = //
          new DavisGifImageWriter(new File(target, file.getName() + ".gif"), 100, fitec);
      SignalResetDifference signalResetDifference = new SignalResetDifference(davisImageBuffer);
      davisLcmClient.davisSigDatagramDecoder.addListener(signalResetDifference);
      davisLcmClient.davisSigDatagramDecoder.addListener(fitec);
      davisLcmClient.davisSigDatagramDecoder.addListener(accumulatedOverlay.sig);
      // ---
      davisLcmClient.davisDvsDatagramDecoder.addDvsListener(accumulatedOverlay);
      signalResetDifference.addListener(accumulatedOverlay.differenceListener);
      accumulatedOverlay.addListener(davisGifImageWriter);
      // ---
      Log log = new Log(file.toString(), "r");
      Set<String> set = new HashSet<>();
      try {
        while (true) {
          Event event = log.readNext();
          ++count;
          if (set.add(event.channel))
            System.out.println(event.channel);
          // TODO magic const extensions
          if (event.channel.endsWith(".imu")) // imu
            davisLcmClient.digestImu(new DavisImu(event.data));
          if (event.channel.endsWith(".sig")) // signal aps
            davisLcmClient.digestSig(new BinaryBlob(event.data));
          if (event.channel.endsWith(".rst")) // reset read aps
            davisLcmClient.digestRst(new BinaryBlob(event.data));
          if (event.channel.endsWith(".dvs")) // events
            davisLcmClient.digestDvs(new BinaryBlob(event.data));
        }
      } catch (IOException exception) {
        // ---
      }
      // eventsTextWriter.close();
      davisGifImageWriter.close();
      davisEventStatistics.print();
      System.out.println("total_frames" + davisGifImageWriter.total_frames());
    } catch (IOException exception) {
      // ---
    }
    System.out.println("entries: " + count);
  }
}