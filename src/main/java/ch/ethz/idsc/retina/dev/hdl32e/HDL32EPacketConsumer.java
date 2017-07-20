// code by jph
package ch.ethz.idsc.retina.dev.hdl32e;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ch.ethz.idsc.retina.util.io.PacketConsumer;
import ch.ethz.idsc.retina.util.io.PcapParse;

public class HDL32EPacketConsumer implements PacketConsumer {
  public static final int LASER_SIZE1 = 1248;
  public static final int LASER_SIZE2 = 1206;
  // ---
  private final HDL32EFiringPacketConsumer firingPacketConsumer;

  public HDL32EPacketConsumer(HDL32EFiringPacketConsumer firingPacketConsumer) {
    this.firingPacketConsumer = firingPacketConsumer;
  }

  @Override
  public void parse(byte[] packet_data, int length) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(packet_data);
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    if (length == LASER_SIZE1) {
      byteBuffer.position(42);
      // System.out.println("ok");
      firingPacketConsumer.lasers(byteBuffer);
    } else //
    if (length == LASER_SIZE2) {
      // byteBuffer.position(42);
      // System.out.println("ok");
      firingPacketConsumer.lasers(byteBuffer);
    } else {
      System.out.println("unhandled " + length);
      // 554 for GPS (?)
      // System.out.println(length);
    }
  }

  public static void main(String[] args) throws Exception {
    LaserPositionConsumer laserPositionConsumer = new LaserPositionConsumer() {
      @Override
      public void digest(float[] position_data, int length) {
        // System.out.println("here");
      }
    };
    PacketConsumer packetConsumer = new HDL32EPacketConsumer( //
        new HDL32EFiringCollector(laserPositionConsumer));
    new PcapParse(
        new File( //
            "/media/datahaki/media/ethz/sensors/velodyne01/usb/Velodyne/HDL-32E Sample Data", //
            "HDL32-V2_Tunnel.pcap"), //
        packetConsumer);
  }
}