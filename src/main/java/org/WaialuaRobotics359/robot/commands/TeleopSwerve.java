package org.WaialuaRobotics359.robot.commands;

import org.WaialuaRobotics359.lib.math.Conversions;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;
import org.WaialuaRobotics359.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class TeleopSwerve extends CommandBase {    
    private Swerve s_Swerve;    
    private PoseEstimator s_PoseEstimator;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;

    private double rotationalIncrement = 5;
    private double ControllerGain = 1;
    private Boolean feedbackNode = false;

    private Boolean usePoseEstimator = false; 

    public TeleopSwerve(Swerve s_Swerve, PoseEstimator s_PoseEstimator, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, BooleanSupplier robotCentricSup) {
        this.s_Swerve = s_Swerve;
        this.s_PoseEstimator = s_PoseEstimator;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
    }

    @Override
    public void execute() {

        if (!RobotState.isAutonomous()){
            /* if slow drive 0.5 : else 1 */
            ControllerGain = s_Swerve.slowMode ? 0.5 : 1;

            /* Get Values, Deadband*/
            double translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), .05);
            double strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), .05);
            double omega = -MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.OI.deadband);

            /* Square Values */
            translationVal = translationVal * translationVal * Math.signum(translationVal);
            strafeVal = strafeVal * strafeVal * Math.signum(strafeVal);
            omega = omega * omega * Math.signum(omega);

            /* Enable Slow Mode */
            translationVal *= ControllerGain;
            strafeVal *= ControllerGain;
            omega *= ControllerGain;

            if (omega != 0 && !feedbackNode) {
                s_Swerve.desiredAngle = s_Swerve.getYaw360();
                feedbackNode = true;
            } else if (omega == 0) {
                feedbackNode = false;
            }

            s_Swerve.desiredAngle += omega * rotationalIncrement;
            s_Swerve.desiredAngle = (s_Swerve.desiredAngle + 360) % 360;

            //SmartDashboard.putNumber("desired", s_Swerve.desiredAngle);
            //SmartDashboard.putNumber("current", s_Swerve.getYaw360());

            double angleToDesired = Conversions.wrap(s_Swerve.getYaw360(), s_Swerve.desiredAngle);
            double rotationVal = angleToDesired / 90;
            if (rotationVal > 1) rotationVal = 1;
            if (rotationVal < -1) rotationVal = -1;

            //SmartDashboard.putNumber("rotationval", rotationVal);

            /* Drive */
            s_Swerve.drive(
                new Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed), 
                rotationVal * Constants.Swerve.maxAngularVelocity, 
                !robotCentricSup.getAsBoolean(), 
                true, 
                usePoseEstimator ? s_PoseEstimator.getYaw() : s_Swerve.getYaw() //what gyro to use
            );
        }
    }
}