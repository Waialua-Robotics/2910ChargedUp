package org.WaialuaRobotics359.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.pathplanner.lib.auto.PIDConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.WaialuaRobotics359.lib.util.COTSFalconSwerveConstants;
import org.WaialuaRobotics359.lib.util.SwerveModuleConstants;

public final class Constants {

//conepickup - a = 5400 p = 790 w = 32620
//cubepickup - a = 5400 p = 2275 w = 21500
//backLowCone - a = 100 p = 16300 w = 43150
//low cone - a = 0 p = 21300 w = 2325
//mid cone - a = 13225 p = 83290 w = 6730
//high cone - a = 26525 p = 8680 w = 13000
//backlowcube - a = 0 p = 62200 w = 155
//backmidcube - a = 2790 p = 85630 w = 5110
//backhigh cube - a = 25020 p = 88410 w = 6880
//lowcube - a = 0 p = 0 w = 0
//midCube - a - 1665 p = 20630 w = 9200
//highcube - a = 26470 p = 22000 w = 2580
//feederCone - a = 20350 p = 71150 w = 0
//downfeederCone - a = 0 p = 69500 w = 14885
//feederCube - a = 18120 p = 39050 w = 37440

    public static final double stickDeadband = 0.1;
    public static HashMap<String, Command> eventMap = new HashMap<String, Command>();

    public static final class LEDs {
        public static final int CANdleID = 8; //TODO: This must be tuned to robot
    }

    public static final class Intake {
        public static final int IntakeID = 2;

        public static final double intakeSpeed = 1;
        public static final double outakeSpeed = -1;
    }

    public static final class Wrist {
        public static final int softMax = 0;
        public static final int softMin = 0;
        public static final int WristID = 1;
        public static final int stowPosition = 0;

        public static final class Cone {
            public static final int groundPosition = 32620;
            public static final int lowPosition = 2325;
            public static final int midPosition = 6730;
            public static final int highPosition = 13000;
            public static final int feederPosition = 0;
        }
        
        public static final class Cube {
            public static final int groundPosition = 21500;
            public static final int lowPosition = 0;
            public static final int midPosition = 9200;
            public static final int highPosition = 2580;
            public static final int feederPosition = 37440;
        }
    }

    public static final class Arm {
        public static final int softMax = 0;
        public static final int softMin = 0;
        public static final int lArmID = 5;
        public static final int rArmID = 6;
        public static final int stowPosition = 0;

        public static final class Cone {
            public static final int groundPosition = 5400;
            public static final int lowPosition = 0;
            public static final int midPosition = 13225;
            public static final int highPosition = 26525;
            public static final int feederPosition = 20350;
        }
        
        public static final class Cube {
            public static final int groundPosition = 5400;
            public static final int lowPosition = 0;
            public static final int midPosition = 1665;
            public static final int highPosition = 26470;
            public static final int feederPosition = 18120;
        }
    }

    public static final class Pivot {
        public static final int softMax = 0;
        public static final int softMin = 0;
        public static final int Fl_PivotID = 3;
        public static final int Bl_PivotID = 4;
        public static final int Fr_PivotID = 7;
        public static final int Br_PivotID = 8;
        public static final int lowStart = 10000;
        
        public static final int stowPosition = 1500;

        public static final double kTicksPerDegree = 636.4;


        public static final class Cone {

            public static final int groundPosition = 790;
            public static final int lowPosition = 21300;
            public static final int midPosition = 83290;
            public static final int highPosition = 86800;
            public static final int feederPosition = 71150;

        }
        
        public static final class Cube {
            public static final int groundPosition = 2275;
            public static final int lowPosition = 0;
            public static final int midPosition = 20630;
            public static final int highPosition = 22000;
            public static final int feederPosition = 39050;
        }
    }

    public static final class Leds {
        public static final int CandleID = 26;
        public static final int LEDCount = 68;
    }

    public static final class OI {

        public static final int wristAxis = XboxController.Axis.kRightTrigger.value;
        public static final int revWristAxis = XboxController.Axis.kLeftTrigger.value;

        public static final int isCube = XboxController.Button.kLeftStick.value;
        public static final int isCone = XboxController.Button.kRightStick.value;

        public static final int intake = XboxController.Button.kRightBumper.value;
        public static final int outake = XboxController.Button.kLeftBumper.value;

        public static final int pivot = XboxController.Axis.kLeftY.value;

        public static final int arm = XboxController.Axis.kRightY.value;



        public static final int lowPickup = XboxController.Button.kLeftBumper.value;
        public static final int midPckup = XboxController.Button.kRightBumper.value;

        public static final int lowPos = XboxController.Button.kA.value;
        public static final int midPos = XboxController.Button.kB.value;
        public static final int highPos = XboxController.Button.kY.value;
        public static final int feedPos = XboxController.Button.kX.value;

        public static final int stow = XboxController.Button.kStart.value;

        public static final double deadband = 0;
    }

    public static final class Swerve {
        public static final int pigeonID = 6;
        public static final boolean invertGyro = false; // TODO: Always ensure Gyro is CCW+ CW-

        public static final COTSFalconSwerveConstants chosenModule = 
            COTSFalconSwerveConstants.SDSMK4i(COTSFalconSwerveConstants.driveGearRatios.SDSMK4i_L2);

        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(19.75); //TODO: This must be tuned to robot
        public static final double wheelBase = Units.inchesToMeters(19.75); //TODO: This must be tuned to robot
        public static final double wheelCircumference = chosenModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenModule.driveGearRatio;
        public static final double angleGearRatio = chosenModule.angleGearRatio;

        /* Motor Inverts */
        public static final boolean angleMotorInvert = chosenModule.angleMotorInvert;
        public static final boolean driveMotorInvert = chosenModule.driveMotorInvert;

        /* Angle Encoder Invert */
        public static final boolean canCoderInvert = chosenModule.canCoderInvert;

        /* Swerve Current Limiting */
        public static final int angleContinuousCurrentLimit = 25;
        public static final int anglePeakCurrentLimit = 40;
        public static final double anglePeakCurrentDuration = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveContinuousCurrentLimit = 35;
        public static final int drivePeakCurrentLimit = 60;
        public static final double drivePeakCurrentDuration = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = chosenModule.angleKP;
        public static final double angleKI = chosenModule.angleKI;
        public static final double angleKD = chosenModule.angleKD;
        public static final double angleKF = chosenModule.angleKF;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.1; //TODO: This must be tuned to robot
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values 
         * Divide SYSID values by 12 to convert from volts to percent output for CTRE */
        public static final double driveKS = (0.32 / 12); //FIXME: This must be tuned to robot
        public static final double driveKV = (1.51 / 12);
        public static final double driveKA = (0.27 / 12);

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 4.5; //TODO: This must be tuned to robot
        /** Radians per Second */
        public static final double maxAngularVelocity = 10; //TODO: This must be tuned to robot

        /* Neutral Modes */
        public static final NeutralMode angleNeutralMode = NeutralMode.Coast;
        public static final NeutralMode driveNeutralMode = NeutralMode.Brake;

        /* Module Specific Constants */

         /*     Swerve Order
         *         0  1
         *         2  3
         */
        
        /* Front Left Module - Module 0 */
        public static final class Mod0 { //TODO: This must be tuned to robot
            public static final int driveMotorID = 0;
            public static final int angleMotorID = 10;
            public static final int canCoderID = 20;
            public static final int driveMotor2ID = 30;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(357.45);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Front Right Module - Module 1 */
        public static final class Mod1 { //TODO: This must be tuned to robot
            public static final int driveMotorID = 1;
            public static final int angleMotorID = 11;
            public static final int canCoderID = 21;
            public static final int driveMotor2ID = 31;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(82.53);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
        
        /* Back Left Module - Module 2 */
        public static final class Mod2 { //TODO: This must be tuned to robot
            public static final int driveMotorID = 2;
            public static final int angleMotorID = 12;
            public static final int canCoderID = 22;
            public static final int driveMotor2ID = 32;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(55.55);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Right Module - Module 3 */
        public static final class Mod3 { //TODO: This must be tuned to robot
            public static final int driveMotorID = 3;
            public static final int angleMotorID = 13;
            public static final int canCoderID = 23;
            public static final int driveMotor2ID = 33;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(268.24);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
    }

    public static final class ScoringPoses{
        public static final class Cone{
            public static final List<Pose2d> BluePoses =
            new ArrayList<>() {
                {
                add(new Pose2d(1.85, Units.inchesToMeters(196.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(152.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(130.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(86.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(64.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(20.19), new Rotation2d(0)));
                }
            };
            
            public static final List<Pose2d> RedPoses =
            new ArrayList<>() {
                {
                add(new Pose2d(1.85, Units.inchesToMeters(295.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(251.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(229.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(185.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(163.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(119.56), new Rotation2d(0)));
                }
            };
        }

        public static final class Cube{
            public static final List<Pose2d> BluePoses =
            new ArrayList<>() {
                {
                add(new Pose2d(1.85, Units.inchesToMeters(174.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(108.19), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(42.19), new Rotation2d(0)));
                }
            };
            
            public static final List<Pose2d> RedPoses =
            new ArrayList<>() {
                {
                add(new Pose2d(1.85, Units.inchesToMeters(273.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(207.56), new Rotation2d(0)));
                add(new Pose2d(1.85, Units.inchesToMeters(141.56), new Rotation2d(0)));
                }
            };
        }
    }

    public static final class AutoConstants { //TODO: The below constants are used in the example auto, and must be tuned to specific robot
        public static final double kMaxSpeedMetersPerSecond = 3;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = 1;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;

        /*Auto Builder Const */
        public static final PIDConstants translationPID = new PIDConstants(0.5, 0, 0);
        public static final PIDConstants rotationPID = new PIDConstants(15, 0, 0); //d.05
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }
}
