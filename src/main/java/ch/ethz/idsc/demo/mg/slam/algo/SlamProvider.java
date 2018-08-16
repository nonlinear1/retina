// code by mg
package ch.ethz.idsc.demo.mg.slam.algo;

import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;

import ch.ethz.idsc.demo.mg.pipeline.BackgroundActivityFilter;
import ch.ethz.idsc.demo.mg.pipeline.FilteringPipeline;
import ch.ethz.idsc.demo.mg.slam.GokartPoseOdometryDemo;
import ch.ethz.idsc.demo.mg.slam.MapProvider;
import ch.ethz.idsc.demo.mg.slam.SlamConfig;
import ch.ethz.idsc.demo.mg.slam.SlamParticle;
import ch.ethz.idsc.demo.mg.slam.WayPoint;
import ch.ethz.idsc.demo.mg.util.calibration.ImageToGokartInterface;
import ch.ethz.idsc.gokart.core.pos.GokartPoseInterface;
import ch.ethz.idsc.gokart.core.pos.GokartPoseLocal;
import ch.ethz.idsc.retina.dev.davis.DavisDvsListener;
import ch.ethz.idsc.retina.dev.davis._240c.DavisDvsEvent;
import ch.ethz.idsc.tensor.Tensor;

/** implementation of the SLAM algorithm
 * "simultaneous localization and mapping for event-based vision systems"
 * by David Weikersdorfer, Raoul Hoffmann, and Joerg Conradt
 * https://mediatum.ub.tum.de/doc/1191908/1191908.pdf */
public class SlamProvider implements DavisDvsListener {
  private final ImageToGokartInterface imageToGokartInterface;
  private final GokartPoseInterface gokartLidarPose;
  private final GokartPoseOdometryDemo gokartPoseOdometry;
  // ---
  private final FilteringPipeline filteringPipeline;
  private final SlamLocalizationStep slamLocalizationStep;
  private final SlamMappingStep slamMappingStep;
  private final SlamMapProcessing slamWayPoints;
  private final SlamTrajectoryPlanning slamTrajectoryPlanning;
  private final boolean lidarMappingMode;
  private final SlamParticle[] slamParticles;
  // ---
  private boolean isInitialized;
  // --

  public SlamProvider(SlamConfig slamConfig, GokartPoseOdometryDemo gokartPoseOdometry, GokartPoseInterface gokartLidarPose) {
    imageToGokartInterface = slamConfig.davisConfig.createImageToGokartUtilLookup();
    this.gokartLidarPose = gokartLidarPose;
    this.gokartPoseOdometry = gokartPoseOdometry;
    // ---
    filteringPipeline = new BackgroundActivityFilter(slamConfig.davisConfig);
    slamLocalizationStep = new SlamLocalizationStep(slamConfig);
    slamMappingStep = new SlamMappingStep(slamConfig);
    slamWayPoints = new SlamMapProcessing(slamConfig);
    slamTrajectoryPlanning = new SlamTrajectoryPlanning(slamConfig, slamLocalizationStep.getSlamEstimatedPose());
    lidarMappingMode = slamConfig.lidarMappingMode;
    // ---
    int numOfPart = slamConfig.numberOfParticles.number().intValue();
    slamParticles = new SlamParticle[numOfPart];
    for (int index = 0; index < numOfPart; ++index)
      slamParticles[index] = new SlamParticle();
  }

  public void initialize(Tensor pose, double timeStamp) {
    gokartPoseOdometry.setPose(pose);
    slamLocalizationStep.initialize(slamParticles, pose, timeStamp);
    slamMappingStep.initialize(timeStamp);
    slamWayPoints.initialize(timeStamp);
    slamTrajectoryPlanning.initialize(timeStamp); // TODO need to call stop function
    isInitialized = true;
  }

  @Override
  public void davisDvs(DavisDvsEvent davisDvsEvent) {
    if (!isInitialized) {
      // TODO JPH find other way to trigger initialize
      if (gokartLidarPose.getPose() != GokartPoseLocal.INSTANCE.getPose())
        initialize(gokartLidarPose.getPose(), davisDvsEvent.time * 1E-6);
    } else {
      if (filteringPipeline.filterPipeline(davisDvsEvent)) {
        double currentTimeStamp = davisDvsEvent.time * 1E-6;
        double[] eventGokartFrame = imageToGokartInterface.imageToGokart(davisDvsEvent.x, davisDvsEvent.y);
        if (lidarMappingMode) {
          slamLocalizationStep.setPose(gokartLidarPose.getPose());
          slamMappingStep.mappingStepWithLidar(slamLocalizationStep.getSlamEstimatedPose().getPoseUnitless(), eventGokartFrame, currentTimeStamp);
        } else {
          slamLocalizationStep.localizationStep(slamParticles, slamMappingStep.getMap(0), gokartPoseOdometry.getVelocity(), eventGokartFrame, currentTimeStamp);
          slamMappingStep.mappingStep(slamParticles, slamLocalizationStep.getSlamEstimatedPose().getPoseUnitless(), eventGokartFrame, currentTimeStamp);
        }
        slamWayPoints.mapPostProcessing(slamMappingStep.getMap(0), currentTimeStamp);
        slamTrajectoryPlanning.computeTrajectory(slamWayPoints.getWorldWayPoints(), currentTimeStamp);
      }
    }
  }

  public GokartPoseInterface getGokartPoseInterface() {
    return slamLocalizationStep.getSlamEstimatedPose();
  }

  public SlamParticle[] getParticles() {
    return slamParticles;
  }

  public Mat getProcessedMat() {
    return slamWayPoints.getProcessedMat();
  }

  public List<WayPoint> getWayPoints() {
    return slamTrajectoryPlanning.getWayPoints();
  }

  // mapID: 0 == occurrence map, 1 == normalization map, 2 == likelihood map
  public MapProvider getMap(int mapID) {
    return slamMappingStep.getMap(mapID);
  }

  public boolean getIsInitialized() {
    return isInitialized;
  }
}