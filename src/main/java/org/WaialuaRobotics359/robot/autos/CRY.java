package org.WaialuaRobotics359.robot.autos;

import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CRY extends SequentialCommandGroup {

    public CRY (SwerveAutoBuilder autoBuilder, PoseEstimator s_poseEstimator) {

        PathPlannerTrajectory AutoPath = PathPlanner.loadPath("CRYP1", new PathConstraints(4, 3)); 
        PathPlannerTrajectory AutoPath2 = PathPlanner.loadPath("CRYP2", new PathConstraints(4, 3));
        PathPlannerTrajectory AutoPath3 = PathPlanner.loadPath("CRYP3", new PathConstraints(4, 3));
        PathPlannerTrajectory AutoPath4 = PathPlanner.loadPath("CRYP4", new PathConstraints(4, 3));
        PathPlannerTrajectory AutoPath5 = PathPlanner.loadPath("CRYP5", new PathConstraints(4, 4));

        Pose2d startpose = AutoPath.getInitialHolonomicPose();

        addCommands(new SequentialCommandGroup(
            new InstantCommand(()-> s_poseEstimator.resetPose(startpose)),
            autoBuilder.fullAuto(AutoPath),
            autoBuilder.fullAuto(AutoPath2),
            autoBuilder.fullAuto(AutoPath3),
            autoBuilder.fullAuto(AutoPath4),
            autoBuilder.fullAuto(AutoPath5)
        ));
    }
}