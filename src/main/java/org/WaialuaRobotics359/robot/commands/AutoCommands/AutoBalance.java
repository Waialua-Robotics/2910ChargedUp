package org.WaialuaRobotics359.robot.commands.AutoCommands;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Swerve;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoBalance extends CommandBase {
  private Swerve s_Swerve;    

  private boolean balancing = false;
  private boolean finished = false;

  private double error;
  private double currentAngle;
  private double drivePower;
  private double pitchOffset;
  private int timebalaced = 0;
  
  public AutoBalance(Swerve s_Swerve) {
    this.s_Swerve = s_Swerve;
    addRequirements(s_Swerve);
  }

  public void initialize(){
    balancing = false;
    finished = false;
    pitchOffset = s_Swerve.GetGyroPitch();
  }

  @Override
  public void execute() {

    // Uncomment the line below this to simulate the gyroscope axis with a controller joystick
    // Double currentAngle = -1 * Robot.controller.getRawAxis(Constants.LEFT_VERTICAL_JOYSTICK_AXIS) * 45;
    currentAngle = s_Swerve.GetGyroPitch() - pitchOffset;

    error = Constants.AutoConstants.BalanceGoal - currentAngle;

    // if first time, use second condition. if second time, use first condition
    if (Math.abs(error) < (balancing ? Constants.AutoConstants.BalanceThreshold : 13) ) {

        /* 
         * if the balancing sequence has begun and the pitch is in range,
         * the robot has balanced on the platform. return from the execute
         * and initiate the ending sequence.
         */
        if (balancing && timebalaced > 10) {
          finished = true;
          return;
        } else if (balancing) {
          timebalaced++;
        }

        if(!balancing){
          s_Swerve.setModuleStates(
            new SwerveModuleState[] {
              new SwerveModuleState(-1, Rotation2d.fromDegrees(0)),
              new SwerveModuleState(-1, Rotation2d.fromDegrees(0)),
              new SwerveModuleState(-1, Rotation2d.fromDegrees(0)),
              new SwerveModuleState(-1, Rotation2d.fromDegrees(0))
            }
        );
        }

    } else {

        // indicates that the balancing sequence has begun
        balancing = true;

        timebalaced =0;

        drivePower = .1 * error;

        if(Math.abs(error)<8) drivePower = 0;
        //if (Math.abs(error)< 12) drivePower = 0;

        // Our robot needed an extra push to drive up in reverse, probably due to weight imbalances
        if (drivePower > 0) {
          drivePower *= Constants.AutoConstants.BalanceReverseMulti;
        }

        // Limit the max power
        if (Math.abs(drivePower) > 0.8) {
          drivePower = Math.copySign(0.8, drivePower);
        }

        s_Swerve.setModuleStates(
                  new SwerveModuleState[] {
                    new SwerveModuleState(drivePower, Rotation2d.fromDegrees(0)),
                    new SwerveModuleState(drivePower, Rotation2d.fromDegrees(0)),
                    new SwerveModuleState(drivePower, Rotation2d.fromDegrees(0)),
                    new SwerveModuleState(drivePower, Rotation2d.fromDegrees(0))
                  }
        );

    }

    // Debugging Print Statments
    System.out.println("Current Angle: " + currentAngle);
    System.out.println("Error " + error);
    System.out.println("Drive Power: " + drivePower);
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Swerve.stop();
    s_Swerve.setFortyFive();
  }
 
   // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished; // End the command when we are within the specified threshold of being 'flat' (gyroscope pitch of 0 degrees)
  }
}