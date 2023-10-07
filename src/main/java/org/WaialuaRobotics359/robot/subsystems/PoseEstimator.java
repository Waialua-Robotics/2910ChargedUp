package org.WaialuaRobotics359.robot.subsystems;


import java.sql.Driver;
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
import edu.wpi.first.wpilibj.counter.UpDownCounter;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PoseEstimator extends SubsystemBase {

  public Pose2d CurrentPose;

  private final Swerve s_Swerve;
  private final PhotonVision s_PhotonVision;
  private final Flight s_Flight;

  // Kalman Filter Configuration. These can be "tuned-to-taste" based on how much
  // you trust your various sensors. Smaller numbers will cause the filter to
  // "trust" the estimate from that particular component more than the others. 
  // This in turn means the particualr component will have a stronger influence
  // on the final pose estimate.
  private static final Matrix<N3, N1> stateStdDevs = VecBuilder.fill(.01, .01, Units.degreesToRadians(.1)); //.1,.1 .01
  private static final Matrix<N3, N1> visionMeasurementStdDevs = VecBuilder.fill(.05, .05, Units.degreesToRadians(25));
  private final SwerveDrivePoseEstimator SwerveposeEstimator;

  private final Field2d field2d = new Field2d();

    /**
    * Apriltag positions
    * / --------------------------------------------- \
    * 5                      |                        4
    * |                      |                        |
    * |                      |                        |
    * 6                      |                        3
    * |                      |                        |
    * 7                      |                        2
    * |                      |                        |
    * 8                      |                        1
    * \ --------------------------------------------- /
    */

    public PoseEstimator(Swerve s_Swerve, PhotonVision s_PhotonVision, Flight s_Flight) {
      this.s_Flight = s_Flight;
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
    public void calibratePose(boolean soft) {
      // Create an "Optional" object that contains the estimated pose of the robot
      // This can be present (see's tag) or not present (does not see tag)
      Optional<EstimatedRobotPose> result = s_PhotonVision.getEstimatedRobotPose();

      // If the result of the estimatedRobotPose exists
      if (result.isPresent()) {
        EstimatedRobotPose camEstimatedPose = result.get();
        //double timestampSeconds = camEstimatedPose.timestampSeconds;
        if (soft) {
          // Add the vision measurement to the pose estimator to update the odometry
          SwerveposeEstimator.addVisionMeasurement(
              camEstimatedPose.estimatedPose.toPose2d(),
              Timer.getFPGATimestamp() - Constants.PhotonConstants.Latency);
        } else {
          resetPose(camEstimatedPose.estimatedPose.toPose2d());
        }
      }
    }
  

  public void resetPoseToZero(){
    Pose2d pose = new Pose2d(0,0, new Rotation2d(0));
    SwerveposeEstimator.resetPosition(pose.getRotation(), s_Swerve.getModulePositionsFlip(), pose);
  }

  public void resetPose(Pose2d pose){
    SwerveposeEstimator.resetPosition(pose.getRotation(), s_Swerve.getModulePositionsFlip(), pose);
  }

  public Pose2d getPose(){
    return SwerveposeEstimator.getEstimatedPosition();
  }

  public Rotation2d getYaw(){
    return getPose().getRotation();
  }

  public Pose2d ClosestSelectedNode(){
    if(DriverStation.getAlliance() == Alliance.Red){
     return RobotContainer.isCube ? getPose().nearest(Constants.ScoringPoses.Cube.RedPoses) : getPose().nearest(Constants.ScoringPoses.Cone.RedPoses);
    }else{
      return RobotContainer.isCube ? getPose().nearest(Constants.ScoringPoses.Cube.BluePoses) : getPose().nearest(Constants.ScoringPoses.Cone.BluePoses);
    }
  }

  public double getXtoClosestSelectedNode(Pose2d desiredNode){
    return desiredNode.getY() - getPose().getY();
  }

  public double getYtoClosestSelectedNode(Pose2d desiredNode){
    return desiredNode.getX() - getPose().getX();
  }

  public boolean isFrontScore(){
    double angle = getPose().getRotation().getCos();
    return angle > 0 ? false : true;
  }

  public boolean inScoringPose(){
    double allowableError = Constants.AutoConstants.inPosisionError;

    if(Math.abs(getXtoClosestSelectedNode(ClosestSelectedNode()) + s_Flight.offsetFromCenterIn())<allowableError && Math.abs(getYtoClosestSelectedNode(ClosestSelectedNode()))< allowableError){
      return true;
    }else{
      return false;
    }
  }


  public void periodic(){

    RobotContainer.inScoringPose = inScoringPose();

    // Update Pose
    SwerveposeEstimator.updateWithTime(Timer.getFPGATimestamp(), s_Swerve.getYaw(), s_Swerve.getModulePositionsFlip());

    // If we are in teleop or vision is enabled in auto, update the pose estimator
    if (Constants.AutoConstants.VisionInAuto ? true : DriverStation.isTeleopEnabled()) {
      calibratePose(true);
    }

    CurrentPose = SwerveposeEstimator.getEstimatedPosition();
    field2d.setRobotPose(CurrentPose);
    field2d.getObject("TargetNode").setPose(ClosestSelectedNode());

    //SmartDashboard.putNumber("Xshuffle", CurrentPose.getX());
    //SmartDashboard.putNumber("Yshuffle", CurrentPose.getY());
    //SmartDashboard.putNumber("ROTshuffle", CurrentPose.getRotation().getDegrees());
    /*SmartDashboard.putNumber("cam 0 dist", s_PhotonVision.getDistance(0));
    SmartDashboard.putNumber("cam 1 dist", s_PhotonVision.getDistance(1));
    SmartDashboard.putNumber("cam 0 amb", s_PhotonVision.getPoseAmbiguity(0));
    SmartDashboard.putNumber("cam 1 amb", s_PhotonVision.getPoseAmbiguity(1));
    SmartDashboard.putNumber("cam 0 lat", s_PhotonVision.getLatencySec(0));
    SmartDashboard.putNumber("cam 1 lat", s_PhotonVision.getLatencySec(1));*/
    //SmartDashboard.putBoolean("isFront", isFrontScore());

    //logData();
    //SmartDashboard.putNumber("X to Closest Node", getXtoClosestSelectedNode());
  }
    
}
