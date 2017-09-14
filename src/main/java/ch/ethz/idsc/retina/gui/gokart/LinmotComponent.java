// code by jph
package ch.ethz.idsc.retina.gui.gokart;

import java.awt.Dimension;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.TimerTask;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import ch.ethz.idsc.retina.dev.linmot.LinmotGetEvent;
import ch.ethz.idsc.retina.dev.linmot.LinmotPutConfiguration;
import ch.ethz.idsc.retina.dev.linmot.LinmotPutEvent;
import ch.ethz.idsc.retina.dev.linmot.LinmotSocket;
import ch.ethz.idsc.retina.util.HexStrings;
import ch.ethz.idsc.retina.util.data.Word;
import ch.ethz.idsc.retina.util.gui.SpinnerLabel;
import ch.ethz.idsc.retina.util.io.ByteArrayConsumer;
import ch.ethz.idsc.retina.util.io.DatagramSocketManager;

public class LinmotComponent extends InterfaceComponent implements ByteArrayConsumer {
  private final DatagramSocketManager datagramSocketManager = //
      DatagramSocketManager.local(new byte[LinmotGetEvent.LENGTH], LinmotSocket.LOCAL_PORT, LinmotSocket.LOCAL_ADDRESS);
  private TimerTask timerTask = null;
  private final SpinnerLabel<Word> spinnerLabelCtrl = new SpinnerLabel<>();
  private final SpinnerLabel<Word> spinnerLabelHdr = new SpinnerLabel<>();
  private final SliderExt sliderExtTPos;
  private final SliderExt sliderExtMVel;
  private final SliderExt sliderExtAcc;
  private final SliderExt sliderExtDec;
  private final JTextField jTextFieldRecv;

  public LinmotComponent() {
    {
      JToolBar jToolBar = createRow("control word");
      spinnerLabelCtrl.setList(LinmotPutConfiguration.COMMANDS);
      spinnerLabelCtrl.setValueSafe(LinmotPutConfiguration.COMMANDS.get(0));
      spinnerLabelCtrl.addToComponent(jToolBar, new Dimension(200, 20), "");
    }
    { // command speed
      JToolBar jToolBar = createRow("motion cmd hdr");
      spinnerLabelHdr.setList(LinmotPutConfiguration.HEADER);
      spinnerLabelHdr.setValueSafe(LinmotPutConfiguration.HEADER.get(0));
      spinnerLabelHdr.addToComponent(jToolBar, new Dimension(200, 20), "");
    }
    { // target pos
      JToolBar jToolBar = createRow("target pos");
      sliderExtTPos = SliderExt.wrap(new JSlider( //
          LinmotPutConfiguration.TARGETPOS_MIN, //
          LinmotPutConfiguration.TARGETPOS_MAX, //
          LinmotPutConfiguration.TARGETPOS_INIT));
      sliderExtTPos.addToComponent(jToolBar);
      // sliderExtF2.setValueShort(init.target_position);
    }
    { // max velocity
      JToolBar jToolBar = createRow("max velocity");
      sliderExtMVel = SliderExt.wrap(new JSlider( //
          LinmotPutConfiguration.MAXVELOCITY_MIN, //
          LinmotPutConfiguration.MAXVELOCITY_MAX, //
          LinmotPutConfiguration.MAXVELOCITY_INIT));
      sliderExtMVel.addToComponent(jToolBar);
    }
    { // acceleration
      JToolBar jToolBar = createRow("acceleration");
      sliderExtAcc = SliderExt.wrap(new JSlider( //
          LinmotPutConfiguration.ACCELERATION_MIN, //
          LinmotPutConfiguration.ACCELERATION_MAX, //
          LinmotPutConfiguration.ACCELERATION_INIT));
      sliderExtAcc.addToComponent(jToolBar);
    }
    { // deceleration
      JToolBar jToolBar = createRow("deceleration");
      sliderExtDec = SliderExt.wrap(new JSlider( //
          LinmotPutConfiguration.DECELERATION_MIN, //
          LinmotPutConfiguration.DECELERATION_MAX, //
          LinmotPutConfiguration.DECELERATION_INIT));
      sliderExtDec.addToComponent(jToolBar);
    }
    { // reception
      jTextFieldRecv = createReading("received");
      datagramSocketManager.addListener(this);
    }
  }

  @Override
  public void connectAction(int period, boolean isSelected) {
    if (isSelected) {
      datagramSocketManager.start();
      timerTask = new TimerTask() {
        @Override
        public void run() {
          LinmotPutEvent linmotPutEvent = new LinmotPutEvent();
          linmotPutEvent.control_word = spinnerLabelCtrl.getValue().getShort();
          linmotPutEvent.motion_cmd_hdr = spinnerLabelHdr.getValue().getShort();
          linmotPutEvent.target_position = (short) sliderExtTPos.jSlider.getValue();
          linmotPutEvent.max_velocity = (short) sliderExtMVel.jSlider.getValue();
          linmotPutEvent.acceleration = (short) sliderExtAcc.jSlider.getValue();
          linmotPutEvent.deceleration = (short) sliderExtDec.jSlider.getValue();
          byte[] data = new byte[LinmotPutEvent.LENGTH];
          ByteBuffer byteBuffer = ByteBuffer.wrap(data);
          byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
          linmotPutEvent.insert(byteBuffer);
          try {
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, //
                InetAddress.getByName(LinmotSocket.REMOTE_ADDRESS), LinmotSocket.REMOTE_PORT);
            datagramSocketManager.send(datagramPacket);
            System.out.println("linmot put=" + HexStrings.from(data));
          } catch (Exception exception) {
            // ---
            System.out.println("LINMOT SEND FAIL");
            exception.printStackTrace();
            System.exit(0); // TODO
          }
        }
      };
      timer.schedule(timerTask, 100, period);
    } else {
      if (Objects.nonNull(timerTask)) {
        timerTask.cancel();
        timerTask = null;
      }
      datagramSocketManager.stop();
    }
  }

  @Override
  public void accept(byte[] data, int length) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    LinmotGetEvent linmotGetEvent = new LinmotGetEvent(byteBuffer);
    jTextFieldRecv.setText(linmotGetEvent.toInfoString());
    // System.out.println(HexStrings.from(data));
    // jTextFieldRecv.setText(HexStrings.from(data));
  }

  @Override
  public String connectionInfoRemote() {
    return String.format("%s:%d", LinmotSocket.REMOTE_ADDRESS, LinmotSocket.REMOTE_PORT);
  }

  @Override
  public String connectionInfoLocal() {
    return String.format("%s:%d", LinmotSocket.LOCAL_ADDRESS, LinmotSocket.LOCAL_PORT);
  }
}