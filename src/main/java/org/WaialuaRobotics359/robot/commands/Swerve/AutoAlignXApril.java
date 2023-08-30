
package org.WaialuaRobotics359.robot.commands.Swerve;

import java.util.function.BooleanSupplier;

import org.WaialuaRobotics359.lib.math.Conversions;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.Flight;
import org.WaialuaRobotics359.robot.subsystems.PoseEstimator;
import org.WaialuaRobotics359.robot.subsystems.Swerve;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoAlignXApril extends CommandBase {

    private PoseEstimator s_PoseEstimator;
    private Swerve s_swerve;
    private Flight s_Flight;
    private BooleanSupplier alignButton;

    // Create a PID controller whose setpoint's change is subject to maximum
    // velocity and acceleration constraints.
    private static double kDt = 0.02;
    private final TrapezoidProfile.Constraints constraintsX;
    private final ProfiledPIDController controllerX;
    private final TrapezoidProfile.Constraints constraintsY;
    private final ProfiledPIDController controllerY;

    private double xDistance;
    private double yDistance;
    private Timer  Timer;

    public AutoAlignXApril(PoseEstimator s_poseEstimator, Swerve s_swerve, Flight s_Flight, BooleanSupplier alignButton ) {
        this.s_PoseEstimator = s_poseEstimator;
        this.s_swerve = s_swerve;
        this.s_Flight = s_Flight;
        this.alignButton = alignButton;
        Timer = new Timer();
        constraintsX = new TrapezoidProfile.Constraints(3, 5);
        controllerX = RobotContainer.isCube ? new ProfiledPIDController(5, 0.5, 0, constraintsX, kDt) : new ProfiledPIDController(5, 1, 0, constraintsX, kDt);
        controllerX.setTolerance(0.0);//.01

        constraintsY = new TrapezoidProfile.Constraints(3, 4);
        controllerY = RobotContainer.isCube ? new ProfiledPIDController(4, 0.5, 0, constraintsY, kDt) : new ProfiledPIDController(4, 1, 0, constraintsY, kDt);
        controllerY.setTolerance(0.0);//.01
    }

    private void fetchValues() {
       xDistance = s_PoseEstimator.getXtoClosestSelectedNode()+ s_Flight.offsetFromCenterIn();
       yDistance = s_PoseEstimator.getYtoClosestSelectedNode();
    }

    @Override
    public void initialize() {
        fetchValues();
        Timer.reset();
        Timer.start();
        controllerX.reset(xDistance);
        controllerY.reset(yDistance);
    }

    @Override
    public void execute() {

        double angleToDesired = Conversions.wrap(s_swerve.getYaw360(), 0);
        double rotationVal = angleToDesired / 90;
        if (rotationVal > 1) rotationVal = 1;
        if (rotationVal < -1) rotationVal = -1;
        s_swerve.setDesired(0);
        
       fetchValues();
       
       Translation2d translation = new Translation2d(controllerY.calculate(yDistance, 0), controllerX.calculate(xDistance, 0)); // only drive y value
       SmartDashboard.putNumber("xDistance", controllerX.calculate(xDistance, 0));
       SmartDashboard.putNumber("xDistance", controllerY.calculate(xDistance, 0));
       
        s_swerve.drive(
            translation, rotationVal * Constants.Swerve.maxAngularVelocity, true, true //open loop?
        ); 

        //System.out.println(translation.getY());
        //System.out.println(translation.getX());
    }
    
    @Override
    public boolean isFinished(){        
        return (!alignButton.getAsBoolean()); //Timer.hasElapsed(2) || controller.atGoal() && !alignButton.getAsBoolean()
    }

    @Override 
    public void end(boolean interupted) {
        s_swerve.stop();
    }
}