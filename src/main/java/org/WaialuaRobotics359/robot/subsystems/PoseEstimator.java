package org.WaialuaRobotics359.robot.subsystems;

import java.util.Optional;

import org.WaialuaRobotics359.robot.Constants;
import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PoseEstimator extends SubsystemBase {

    public Pose2d CurrentPose;

    private final Swerve s_Swerve;
    private final PhotonVision s_PhotonVision;

    // Kalman Filter Configuration. These can be "tuned-to-taste" based on how much
    // you trust your various sensors. Smaller numbers will cause the filter to
    // "trust" the estimate from that particular component more than the others. 
    // This in turn means the particualr component will have a stronger influence
    // on the final pose estimate.
    private static final Matrix<N3, N1> stateStdDevs = VecBuilder.fill(.5, .5, Units.degreesToRadians(.01)); //.1
    private static final Matrix<N3, N1> visionMeasurementStdDevs = VecBuilder.fill(.5, .5, Units.degreesToRadians(180));
    private final SwerveDrivePoseEstimator SwerveposeEstimator;

    private final Field2d field2d = new Field2d();

    public PoseEstimator(PhotonVision s_PhotonVision, Swerve s_Swerve) {
        this.s_Swerve = s_Swerve;
        this.s_PhotonVision = s_PhotonVision;

        ShuffleboardTab tab = Shuffleboard.getTab("Photon");
        tab.add(field2d);

        SwerveposeEstimator = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics,
            s_Swerve.getYaw(),
            s_Swerve.getModulePositions(),
            new Pose2d(),
            stateStdDevs,
            visionMeasurementStdDevs);
    }

    public void VisionMessure(Pose2d robotPose, double timestampSeconds, boolean soft){
        if (robotPose == null) {
            return;
        }
        if (soft){
            SwerveposeEstimator.addVisionMeasurement
            (robotPose, timestampSeconds /*- (LimelightHelpers.getLatency_Pipeline("limelight")+ LimelightHelpers.getLatency_Capture("limelight"))*/);
          } else{
            SwerveposeEstimator.resetPosition(robotPose.getRotation(), s_Swerve.getModulePositions(), robotPose);
          }
      }

    
    public void periodic(){
        //Camera1
        Optional<EstimatedRobotPose> result1 = s_PhotonVision.getEstimatedPose(0);
        EstimatedRobotPose cam1Pose = result1.get();
        field2d.getObject("Cam1 Est Pos").setPose(cam1Pose.estimatedPose.toPose2d());

        //Camera2
        Optional<EstimatedRobotPose> result2 = s_PhotonVision.getEstimatedPose(1);
        EstimatedRobotPose cam2Pose = result2.get();
        field2d.getObject("Cam2 Est Pos").setPose(cam2Pose.estimatedPose.toPose2d());

        SwerveposeEstimator.updateWithTime(Timer.getFPGATimestamp(),s_Swerve.getYawflip(), s_Swerve.getModulePositions());
        VisionMessure(cam1Pose.estimatedPose.toPose2d(), Timer.getFPGATimestamp(), true); //true
        VisionMessure(cam2Pose.estimatedPose.toPose2d(), Timer.getFPGATimestamp(), true); //true
        CurrentPose = SwerveposeEstimator.getEstimatedPosition();
        field2d.setRobotPose(CurrentPose);
      }
}
