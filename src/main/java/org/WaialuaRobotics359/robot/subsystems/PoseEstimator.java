package org.WaialuaRobotics359.robot.subsystems;


import org.WaialuaRobotics359.lib.math.Conversions;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;

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

  // Kalman Filter Configuration. These can be "tuned-to-taste" based on how much
  // you trust your various sensors. Smaller numbers will cause the filter to
  // "trust" the estimate from that particular component more than the others. 
  // This in turn means the particualr component will have a stronger influence
  // on the final pose estimate.
  private static final Matrix<N3, N1> stateStdDevs = VecBuilder.fill(.5, .5, Units.degreesToRadians(.01)); //.1
  private static final Matrix<N3, N1> visionMeasurementStdDevs = VecBuilder.fill(.5, .5, Units.degreesToRadians(180));
  private final SwerveDrivePoseEstimator SwerveposeEstimator;

  private final Field2d field2d = new Field2d();

  /* Logging */


  public PoseEstimator( Swerve s_Swerve) {
    this.s_Swerve = s_Swerve;

    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
    tab.add(field2d);

    SwerveposeEstimator = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics,
      s_Swerve.getYaw(),
      s_Swerve.getModulePositions(),
      new Pose2d(),
      stateStdDevs,
      visionMeasurementStdDevs);

  }

  /*PoseEst */
  public void VisionMessure(Pose2d robotPose, boolean soft){
    if(robotPose != null){
      if (soft){
        //SwerveposeEstimator.addVisionMeasurement
        
      } else{
        SwerveposeEstimator.resetPosition(robotPose.getRotation(), s_Swerve.getModulePositions(), robotPose);
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

  public boolean isFrontScore(){
    double angle = getPose().getRotation().getCos();
    return angle > 0 ? true : false;
  }


  public void periodic(){
    SwerveposeEstimator.updateWithTime(Timer.getFPGATimestamp(),s_Swerve.getYaw(), s_Swerve.getModulePositions());
    //VisionMessure(, true); 
    CurrentPose = SwerveposeEstimator.getEstimatedPosition();
    field2d.setRobotPose(CurrentPose);
    field2d.getObject("TargetNode").setPose(ClosestSelectedNode());
    SmartDashboard.putNumber("X", CurrentPose.getX());
    SmartDashboard.putNumber("y", CurrentPose.getY());
    SmartDashboard.putNumber("Rot", CurrentPose.getRotation().getDegrees());
    SmartDashboard.putBoolean("isFront", isFrontScore());

    //logData();
    //SmartDashboard.putNumber("X to Closest Node", getXtoClosestSelectedNode());
    //SmartDashboard.putNumber ("latency", ((LimelightHelpers.getLatency_Pipeline("LimeLight")) + LimelightHelpers.getLatency_Capture("limelight")));
  }
    
}
