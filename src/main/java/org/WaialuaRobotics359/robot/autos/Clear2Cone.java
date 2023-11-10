package org.WaialuaRobotics359.robot.autos;

import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Clear2Cone extends SequentialCommandGroup {

    public Clear2Cone (SwerveAutoBuilder autoBuilder, PoseEstimator s_poseEstimator) {

        PathPlannerTrajectory AutoPath = PathPlanner.loadPath("Clear2ConeP1", new PathConstraints(2, 1)); 
        PathPlannerTrajectory AutoPath2 = PathPlanner.loadPath("Clear2ConeP2", new PathConstraints(2, 1));

        Pose2d startpose = AutoPath.getInitialHolonomicPose();

        addCommands(new SequentialCommandGroup(
            new InstantCommand(()-> s_poseEstimator.resetPose(startpose)),
            autoBuilder.fullAuto(AutoPath),
            autoBuilder.fullAuto(AutoPath2)
        ));
    }
}