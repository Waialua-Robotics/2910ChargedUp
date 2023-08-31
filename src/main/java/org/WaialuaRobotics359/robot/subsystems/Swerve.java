package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    //public SwerveDriveOdometry swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;

    public double desiredAngle;
    public double PreviousPitch;

    public Boolean slowMode = false;

    public Swerve() {
        gyro = new Pigeon2(Constants.Swerve.pigeonID, "Drivetrain");
        gyro.configFactoryDefault();

        zeroGyro();

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        Timer.delay(1.0);
        resetModulesToAbsolute();

        //swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());

        for(SwerveModule mod : mSwerveMods){
            System.out.println("CANcoder on Module " + mod.moduleNumber + " took " + mod.CANcoderInitTime + " ms to be ready.");
        }
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getYaw()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(),
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }    

    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredStateAuto(desiredStates[mod.moduleNumber], true); //false #FIXME invert for closed loop finally fixed
        }
    }    

    public ChassisSpeeds getChassisSpeed(){
        return Constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates());
    }

    public void stop(){
        drive(new Translation2d(0,0), .001, true, false);
    }

    public void setCurrentAngle(){
        desiredAngle = getYaw().getDegrees();
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public SwerveModulePosition[] getModulePositionsFlip(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPositionFlip();
        }
        return positions;
    }

    public void zeroGyro(){
        desiredAngle = 0;
        gyro.setYaw(0);
    }

    public void setGyroYaw(double yaw){
        gyro.setYaw(yaw);
    }

    public double GetGyroPitch(){
        return gyro.getRoll();
    }

    public boolean gyroPitchHasChanged(){
        int pitch = (int) GetGyroPitch();
        boolean Compare = pitch != PreviousPitch;
        PreviousPitch = pitch;
        return Compare;
    }

    //Boolean function if pitch value has changed since last call

    public Rotation2d getYaw() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public Rotation2d getYawflip(){
        return Rotation2d.fromDegrees((getYaw360() + 180) % 360);
    }

    public double getYaw360() {
        return ( (getYaw().getDegrees() % 360) + 360 ) % 360;
    }

    public void setDesired( double desired ) {
        desiredAngle = desired;
    }

    public void snapRightAngle() {
        //set desired to nearest 90 degrees
        setDesired( Math.round(getYaw360()/180)*180);//90 for 90 degrees
    }

    public void resetModulesToAbsoluteInit(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsoluteInit();
        }
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }

    public void setFortyFive(){
        for(SwerveModule mod: mSwerveMods){
            if(mod.moduleNumber == 0){
                mod.ForceAngle(-45);
            }else if(mod.moduleNumber == 3){
                mod.ForceAngle(-45);
            }else{
                mod.ForceAngle(45);
            }
        }
    }

    @Override
    public void periodic(){

        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);
        }
    }
}