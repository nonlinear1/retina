// code by jph
package ch.ethz.idsc.retina.imu.vmu931;

/* package */ enum Vmu931Statics {
  ;
  /** factor from g to m*s^-2 */
  static final double G_TO_M_S2 = 9.81;
  /** accelerometer */
  static final byte ID_ACCELEROMETER = 'a';
  /** gyroscope */
  static final byte ID_GYROSCOPE = 'g';
  /** magnetometer */
  static final byte ID_MAGNETOMETER = 'c';
  /** quaternion */
  static final byte ID_QUATERNION = 'q';
  /** euler angle */
  static final byte ID_EULER_ANGLES = 'e';
  /** heading */
  static final byte ID_HEADING = 'h';
  /** self test */
  static final byte ID_SELFTEST = 't';
  /**
   * 
   */
  static final byte ID_CALIBRATION = 'l';
  /** status of sensor
   * results in a reply of size == 11 */
  static final byte ID_STATUS = 's';

  public static byte[] requestStatus() {
    return command(ID_STATUS);
  }

  public static final String SELFTEST_STARTED = "Self-test started.";
  public static final String CALIBRATION_STARTED = "Calibration started.";

  public static byte[] requestSelftest() {
    return command(ID_SELFTEST);
  }

  public static byte[] requestCalibration() {
    return command(ID_CALIBRATION);
  }

  static byte[] command(byte type) {
    return new byte[] { 'v', 'a', 'r', type };
  }
}
