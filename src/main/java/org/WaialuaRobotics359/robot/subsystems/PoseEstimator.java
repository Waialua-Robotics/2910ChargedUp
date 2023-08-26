package org.WaialuaRobotics359.robot.subsystems;


import java.util.Optional;

import org.WaialuaRobotics359.lib.math.Conversions;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PoseEstimator extends SubsystemBase {

  public Pose2d CurrentPose;

  private final Swerve s_Swerve;
  private final PhotonVision s_PhotonVision;

  EstimatedRobotPose cam1Pose;
  EstimatedRobotPose cam2Pose;

  // Kalman Filter Configuration. These can be "tuned-to-taste" based on how much
  // you trust your various sensors. Smaller numbers will cause the filter to
  // "trust" the estimate from that particular component more than the others. 
  // This in turn means the particualr component will have a stronger influence
  // on the final pose estimate.
  private static final Matrix<N3, N1> stateStdDevs = VecBuilder.fill(.01, .01, Units.degreesToRadians(.01)); //.1,.1 .01
  private static final Matrix<N3, N1> visionMeasurementStdDevs = VecBuilder.fill(.1, .1, Units.degreesToRadians(1));
  private final SwerveDrivePoseEstimator SwerveposeEstimator;

  private final Field2d field2d = new Field2d();

  /* Logging */


  public PoseEstimator(Swerve s_Swerve, PhotonVision s_PhotonVision) {
    this.s_Swerve = s_Swerve;
    this.s_PhotonVision = s_PhotonVision;

    ShuffleboardTab tab = Shuffleboard.getTab("Field");
    tab.add(field2d);

    SwerveposeEstimator = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics,
      s_Swerve.getYaw(),
      s_Swerve.getModulePositions(),
      new Pose2d(),
      stateStdDevs,
      visionMeasurementStdDevs);

  }

  /* PoseEst */
  public void VisionMeasure(Pose2d robotPose, double timestampSeconds, boolean soft) {
    if (robotPose == null) {
      return;
    }
    if (soft) {
      SwerveposeEstimator.addVisionMeasurement(robotPose, timestampSeconds);
    } else {
      SwerveposeEstimator.resetPosition(robotPose.getRotation(), s_Swerve.getModulePositions(), robotPose);
    }
  }

  public void simpleVisionMeasure(Pose2d robotPose, double timestampSeconds, int cam) {
    if (s_PhotonVision.getDistance(cam) < 4 && s_PhotonVision.getDistance(cam) > 0) {
      if (s_PhotonVision.getPoseAmbiguity(cam) < .2 && s_PhotonVision.getPoseAmbiguity(cam)>0 ) {
        SwerveposeEstimator.addVisionMeasurement(robotPose, timestampSeconds);
      }
    }
  }

  public void resetPoseToZero(){
    Pose2d pose = new Pose2d(0,0, new Rotation2d(0));
    SwerveposeEstimator.resetPosition(pose.getRotation(), s_Swerve.getModulePositions(), pose);
  }

  public void resetPose(Pose2d pose){
    SwerveposeEstimator.resetPosition(pose.getRotation(), s_Swerve.getModulePositions(), pose);
  }

  public Pose2d getPose(){
    return SwerveposeEstimator.getEstimatedPosition();
  }

  public Pose2d ClosestSelectedNode(){
    if(DriverStation.getAlliance() == Alliance.Red){
     return RobotContainer.isCube ? getPose().nearest(Constants.ScoringPoses.Cube.RedPoses) : getPose().nearest(Constants.ScoringPoses.Cone.RedPoses);
    }else{
      return RobotContainer.isCube ? getPose().nearest(Constants.ScoringPoses.Cube.BluePoses) : getPose().nearest(Constants.ScoringPoses.Cone.BluePoses);
    }
  }

  public double getXtoClosestSelectedNode(){
    return ClosestSelectedNode().getY() - getPose().getY();
  }

  public double getYtoClosestSelectedNode(){
    return ClosestSelectedNode().getX() - getPose().getX();
  }

  public boolean isFrontScore(){
    double angle = getPose().getRotation().getCos();
    return angle > 0 ? false : true;
  }


  public void periodic(){

    //CameraLeft
    Optional<EstimatedRobotPose> result1 = s_PhotonVision.getEstimatedPose(0);
    if(result1.isPresent()){ //result1.isPresent()
      cam1Pose = result1.get();
      field2d.getObject("CamLeft Est Pos").setPose(cam1Pose.estimatedPose.toPose2d());
      SmartDashboard.putNumber("cam1Result3dRot", Units.radiansToDegrees(cam1Pose.estimatedPose.getRotation().getAngle()));
      SmartDashboard.putNumber("cam1Result3dZ", cam1Pose.estimatedPose.getZ());
      SmartDashboard.putNumber("cam1Result3dX", cam1Pose.estimatedPose.getX());
      SmartDashboard.putNumber("cam1Result3dY", cam1Pose.estimatedPose.getY());
      SmartDashboard.putNumber("cam1Result2dRot", Units.radiansToDegrees(cam1Pose.estimatedPose.getRotation().getAngle()));
    }else{
      field2d.getObject("CamLeft Est Pos").setPose(new Pose2d(-100, -100, new Rotation2d()));
    }
    
    //CameraRight
    Optional<EstimatedRobotPose> result2 = s_PhotonVision.getEstimatedPose(1);
    if(result2.isPresent()){ //result2.isPresent()
      cam2Pose = result2.get();
      field2d.getObject("CamRight Est Pos").setPose(cam2Pose.estimatedPose.toPose2d());
      SmartDashboard.putNumber("cam2Result3dRot", Units.radiansToDegrees(cam2Pose.estimatedPose.getRotation().getAngle()));
      SmartDashboard.putNumber("cam2Result3dZ", cam2Pose.estimatedPose.getZ());
      SmartDashboard.putNumber("cam2Result3dX", cam2Pose.estimatedPose.getX());
      SmartDashboard.putNumber("cam2Result3dY", cam2Pose.estimatedPose.getY());
      SmartDashboard.putNumber("cam2Result2dRot", Units.radiansToDegrees(cam2Pose.estimatedPose.getRotation().getAngle()));
    }else{
      field2d.getObject("CamRight Est Pos").setPose(new Pose2d(-100, -100, new Rotation2d()));
    }

    //Update Pose
    SwerveposeEstimator.updateWithTime(Timer.getFPGATimestamp(),s_Swerve.getYaw(), s_Swerve.getModulePositionsFlip()); //flipped pose?? or yaw?
    if(result1.isPresent()){
      simpleVisionMeasure(cam1Pose.estimatedPose.toPose2d(), cam1Pose.timestampSeconds, 0); //Timer.getFPGATimestamp()-s_PhotonVision.getLatency(0)
    }

    if(result2.isPresent()){
      simpleVisionMeasure(cam2Pose.estimatedPose.toPose2d(), cam2Pose.timestampSeconds, 1);
    }

    CurrentPose = SwerveposeEstimator.getEstimatedPosition();
    field2d.setRobotPose(CurrentPose);
    field2d.getObject("TargetNode").setPose(ClosestSelectedNode());

    SmartDashboard.putNumber("Xshuffle", CurrentPose.getX());
    SmartDashboard.putNumber("Yshuffle", CurrentPose.getY());
    SmartDashboard.putNumber("ROTshuffle", CurrentPose.getRotation().getDegrees());
    /*SmartDashboard.putNumber("cam 0 dist", s_PhotonVision.getDistance(0));
    SmartDashboard.putNumber("cam 1 dist", s_PhotonVision.getDistance(1));
    SmartDashboard.putNumber("cam 0 amb", s_PhotonVision.getPoseAmbiguity(0));
    SmartDashboard.putNumber("cam 1 amb", s_PhotonVision.getPoseAmbiguity(1));
    SmartDashboard.putNumber("cam 0 lat", s_PhotonVision.getLatencySec(0));
    SmartDashboard.putNumber("cam 1 lat", s_PhotonVision.getLatencySec(1));*/
    SmartDashboard.putBoolean("isFront", isFrontScore());

    //logData();
    //SmartDashboard.putNumber("X to Closest Node", getXtoClosestSelectedNode());
    //SmartDashboard.putNumber ("latency", ((LimelightHelpers.getLatency_Pipeline("LimeLight")) + LimelightHelpers.getLatency_Capture("limelight")));
  }
    
}
