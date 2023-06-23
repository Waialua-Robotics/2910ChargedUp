package org.WaialuaRobotics359.robot.autos;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Swerve;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class swerveBuilderAuto extends SequentialCommandGroup {

    public swerveBuilderAuto (Swerve s_Swerve) {

        PathPlannerTrajectory ComplexAuto = PathPlanner.loadPath("ComplexAuto", new PathConstraints(Constants.AutoConstants.kMaxSpeedMetersPerSecond, Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared));

        /* Auto Builder */
  // Using the PathPlanner swerve autobuilder constructor to create an AutoBuilder
        SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(
        s_Swerve::getPose,
        s_Swerve::resetOdometry,
        Constants.Swerve.swerveKinematics,
        new PIDConstants(Constants.AutoConstants.translationPID.kP, Constants.AutoConstants.translationPID.kI,
            Constants.AutoConstants.translationPID.kD),
        new PIDConstants(Constants.AutoConstants.rotationPID.kP, Constants.AutoConstants.rotationPID.kI,
            Constants.AutoConstants.rotationPID.kD),
        s_Swerve::setModuleStates,
        Constants.eventMap,
        s_Swerve
        );

        addCommands(new SequentialCommandGroup(
        autoBuilder.fullAuto(ComplexAuto)
        ));
    }
}