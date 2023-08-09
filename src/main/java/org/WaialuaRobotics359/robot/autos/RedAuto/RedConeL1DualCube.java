package org.WaialuaRobotics359.robot.autos.RedAuto;


import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class RedConeL1DualCube extends SequentialCommandGroup {

    public RedConeL1DualCube (SwerveAutoBuilder autoBuilder, PoseEstimator s_poseEstimator) {

        PathPlannerTrajectory RedConeL1Dual= PathPlannerTrajectory.transformTrajectoryForAlliance((PathPlanner.loadPath("ConeL1DualLink", new PathConstraints(4.5, 3.5))), Alliance.Red);
        PathPlannerTrajectory RedConeL1DualLinkOut= PathPlannerTrajectory.transformTrajectoryForAlliance((PathPlanner.loadPath("ConeL1DualLinkOut", new PathConstraints(4.5, 4))), Alliance.Red);
        Pose2d startpose = RedConeL1Dual.getInitialHolonomicPose();

        addCommands(new SequentialCommandGroup(
            new InstantCommand(()-> s_poseEstimator.resetPose(startpose)),
            autoBuilder.fullAuto(RedConeL1Dual),
            autoBuilder.fullAuto(RedConeL1DualLinkOut)
        ));
    }
}