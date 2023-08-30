package org.WaialuaRobotics359.robot.autos;

import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ConeL1DualCube extends SequentialCommandGroup {

    public ConeL1DualCube (SwerveAutoBuilder autoBuilder, PoseEstimator s_poseEstimator) {

        PathPlannerTrajectory ConeL1Dual = PathPlanner.loadPath("ConeL1DualLink", new PathConstraints(4.5, 3.5)); 
        PathPlannerTrajectory ConeL1DualLinkOut = PathPlanner.loadPath("ConeL1DualLinkOut", new PathConstraints(4.5, 4));
        Pose2d startpose = ConeL1Dual.getInitialHolonomicPose();

        addCommands(new SequentialCommandGroup(
            new InstantCommand(()-> s_poseEstimator.resetPose(startpose)),
            autoBuilder.fullAuto(ConeL1Dual),
            autoBuilder.fullAuto(ConeL1DualLinkOut)
        ));
    }
}