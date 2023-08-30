
package org.WaialuaRobotics359.robot.commands.Swerve;

import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;
import org.WaialuaRobotics359.robot.subsystems.Swerve;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoAlignPath extends CommandBase {

    private PoseEstimator s_PoseEstimator;
    private Swerve s_swerve;
    private SwerveAutoBuilder autoBuilder;

    private Timer  Timer;

    private PathPoint currentPose;
    private PathPoint desiredPose;
    private Pose2d error;

    public AutoAlignPath(PoseEstimator s_poseEstimator, Swerve s_swerve, SwerveAutoBuilder autoBuilder) {
        this.s_PoseEstimator = s_poseEstimator;
        this.s_swerve = s_swerve;
        this.autoBuilder = autoBuilder;
        Timer = new Timer();
    }

    private void fetchValues() {
        currentPose = PathPoint.fromCurrentHolonomicState(s_PoseEstimator.getPose(), s_swerve.getChassisSpeed());
        desiredPose = new PathPoint(new Translation2d(s_PoseEstimator.ClosestSelectedNode().getX(), s_PoseEstimator.ClosestSelectedNode().getY()), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0));
        error = s_PoseEstimator.getPose().relativeTo(s_PoseEstimator.ClosestSelectedNode()) ;
    }

    @Override
    public void initialize() {
        fetchValues();
        Timer.reset();
        Timer.start();

    }

    @Override
    public void execute() {
       fetchValues();
       
       PathPlannerTrajectory trajectory = PathPlanner.generatePath(
    new PathConstraints(1, .5), 
    currentPose, 
    desiredPose
    ); 

    autoBuilder.followPath(trajectory);

    }
    
    @Override
    public boolean isFinished(){        
        return ( error.getX()<.5 && error.getY() <.5 && error.getRotation().getDegrees() <10 || Timer.hasElapsed(2));
    }

    @Override 
    public void end(boolean interupted) {
        s_swerve.stop();
    }
}