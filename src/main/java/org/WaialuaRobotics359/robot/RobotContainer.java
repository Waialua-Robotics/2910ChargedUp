package org.WaialuaRobotics359.robot;

import edu.wpi.first.hal.simulation.DIODataJNI;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.HashMap;

import org.WaialuaRobotics359.robot.autos.*;
import org.WaialuaRobotics359.robot.commands.*;
import org.WaialuaRobotics359.robot.commands.Manual.*;
import org.WaialuaRobotics359.robot.commands.SetPoints.*;
import org.WaialuaRobotics359.robot.commands.SetPoints.Scoring.*;
import org.WaialuaRobotics359.robot.commands.SetPoints.Pickup.*;
import org.WaialuaRobotics359.robot.commands.autonomous.*;
import org.WaialuaRobotics359.robot.subsystems.*;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    public static boolean isCube = true;
    public static boolean allowScore = true;
    public static boolean retractOnScore = false;
    public boolean toggleMode = true;
    public boolean brakeMode = false;
    public boolean zeroMode = true;
    


    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final Joystick operator = new Joystick(1);

    public final DigitalInput brakeToggle = new DigitalInput(0);
    public final DigitalInput zero = new DigitalInput(1);



    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    private final Trigger snapRightAngle = new Trigger(() -> driver.getRawAxis(XboxController.Axis.kLeftTrigger.value) > 0.5);

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kStart.value);
    private final JoystickButton ResetMods = new JoystickButton(driver, XboxController.Button.kBack.value); 
    private final JoystickButton Angle0 = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton Angle180 = new JoystickButton(driver, XboxController.Button.kA.value);
    private final JoystickButton setDriveSlowMode = new JoystickButton(driver, XboxController.Button.kRightBumper.value); 
    private final JoystickButton Angle90 = new JoystickButton(driver, XboxController.Button.kX.value);
    private final JoystickButton Angle270 = new JoystickButton(driver, XboxController.Button.kB.value);
    private final JoystickButton setCurrentAngle = new JoystickButton(driver, XboxController.Button.kRightStick.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    

    /* Operator Controls */
    private final int wristAxis = (Constants.OI.wristAxis);
    private final int revWristAxis = (Constants.OI.revWristAxis);
    private final int pivot = (Constants.OI.pivot);
    private final int arm = (Constants.OI.arm);

    /* Operator Buttons */

    private final POVButton intake = new POVButton(operator, Constants.OI.intake);
    private final POVButton outake = new POVButton(operator, Constants.OI.outake);

    private final JoystickButton lowPickup = new JoystickButton(operator, Constants.OI.lowPickup);
    private final JoystickButton midPickup = new JoystickButton(operator, Constants.OI.midPckup);

    private final JoystickButton setCube = new JoystickButton(operator, Constants.OI.isCube);
    private final JoystickButton setCone = new JoystickButton(operator, Constants.OI.isCone);

    private final JoystickButton lowPos = new JoystickButton(operator, Constants.OI.lowPos);
    private final JoystickButton midPos = new JoystickButton(operator, Constants.OI.midPos);
    private final JoystickButton highPos = new JoystickButton(operator, Constants.OI.highPos);
    private final JoystickButton feedPos = new JoystickButton(operator, Constants.OI.feedPos);

    private final Trigger kill = new Trigger(() -> operator.getRawAxis(XboxController.Axis.kRightTrigger.value) > 0.5);

    private final JoystickButton stow = new JoystickButton(operator, Constants.OI.stow);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Intake s_Intake = new Intake();
    private final Wrist s_Wrist = new Wrist();
    private final Arm s_Arm = new Arm();
    private final Flight s_Flight = new Flight();
    private final Pivot s_Pivot = new Pivot();
    private final Leds s_Leds = new Leds();
    private final PoseEstimator s_PoseEstimator = new PoseEstimator(s_Swerve);

    /* Auto Builder */
    private SwerveAutoBuilder autoBuilder;

    /*chooser for autonomous commands*/
    SendableChooser<Command> m_chooser = new SendableChooser<>();


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
         
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
            s_Swerve, 
            () -> driver.getRawAxis(translationAxis), 
            () -> driver.getRawAxis(strafeAxis), 
            () -> driver.getRawAxis(rotationAxis), 
            () -> robotCentric.getAsBoolean()
            )
        );  
/* 
        s_Wrist.setDefaultCommand(
            new ManualWrist(
                s_Wrist,
                () -> operator.getRawAxis(wristAxis),
                () -> -operator.getRawAxis(revWristAxis)
            )
        );
        */

        s_Intake.setDefaultCommand(
            new ManualIntake(
                s_Intake, s_Leds,
                () -> intake.getAsBoolean(),
                () -> outake.getAsBoolean()
            )
        );
/* 
        s_Pivot.setDefaultCommand(
            new ManualPivot(
                s_Pivot,
                () -> -operator.getRawAxis(pivot)
            )
        );

        s_Arm.setDefaultCommand(
            new ManualArm(
                s_Arm,
                () -> -operator.getRawAxis(arm)
            )
        );*/


        // Configure the button bindings
        configureButtonBindings();

        //cingifure event map and autoBuilder
        configAuto();

        configRoutine();


    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Driver Buttons */
            /* Reset Gyro */
            zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
            /* Reset Swerve Modules */
            ResetMods.onTrue(new InstantCommand(() -> s_Swerve.resetModulesToAbsolute()));
            /* Snap-to Swerve Angle */
            snapRightAngle.onTrue(new InstantCommand(() -> s_Swerve.snapRightAngle()));
            setCurrentAngle.onTrue(new InstantCommand(() -> s_Swerve.setCurrentAngle()));
            Angle0.onTrue(new InstantCommand(() -> s_Swerve.setDesired(0)));
            Angle90.onTrue(new InstantCommand(() -> s_Swerve.setDesired(90)));
            Angle180.onTrue(new InstantCommand(() -> s_Swerve.setDesired(180)));
            Angle270.onTrue(new InstantCommand(() -> s_Swerve.setDesired(270)));

            /* Toggle Swerve Slow Mode */
            setDriveSlowMode.onTrue(new InstantCommand(()-> s_Swerve.slowMode =true ));
            setDriveSlowMode.onFalse(new InstantCommand(() -> s_Swerve.slowMode = false));

        /* Operator Buttons */
        setCone.whileTrue(
            new ParallelCommandGroup( new InstantCommand(() -> isCube = false)));
        setCube.onFalse(
            new ParallelCommandGroup( new InstantCommand(() -> isCube = true)));

            kill.whileTrue(
                new ParallelCommandGroup( new InstantCommand(() -> allowScore = false)));
            kill.onFalse(
                new ParallelCommandGroup( new InstantCommand(() -> allowScore = true)));

            lowPickup.whileTrue(new PickupPosition(s_Arm, s_Intake, s_Flight, s_Wrist, s_Leds, s_Pivot));
            lowPickup.onFalse(new StowPosition(s_Intake, s_Arm, s_Leds, s_Flight, s_Wrist, s_Pivot));
            
            midPickup.whileTrue(new MidPickupPosition(s_Arm, s_Intake, s_Flight, s_Wrist, s_Leds, s_Pivot));
            midPickup.onFalse(new StowPosition(s_Intake, s_Arm, s_Leds, s_Flight, s_Wrist, s_Pivot));

            feedPos.whileTrue(new FeederPosition(s_Arm, s_Intake, s_Flight, s_Wrist, s_Leds, s_Pivot));
            feedPos.onFalse(new StowPosition(s_Intake, s_Arm, s_Leds, s_Flight, s_Wrist,s_Pivot));

            lowPos.whileTrue(new LowPosition(s_Arm, s_Wrist, s_Pivot, s_Leds, s_PoseEstimator));
            lowPos.onFalse(new Score(s_Arm, s_Intake, s_Pivot, s_Wrist, s_Leds));

            midPos.whileTrue(new MidPosition(s_Intake, s_Arm, s_Wrist, s_Pivot, s_Leds));
            midPos.onFalse(new Score(s_Arm, s_Intake, s_Pivot, s_Wrist, s_Leds));

            highPos.whileTrue(new HighPosition(s_Arm, s_Wrist, s_Pivot, s_Leds));
            highPos.onFalse(new Score(s_Arm, s_Intake, s_Pivot, s_Wrist, s_Leds));

            stow.onTrue(new StowPosition(s_Intake, s_Arm, s_Leds, s_Flight, s_Wrist, s_Pivot));
    }   

    public Leds getLeds(){
        return s_Leds;
    }

    public Pivot getPivot(){
        return s_Pivot;
    }

    public Wrist getWrist(){
        return s_Wrist;
    }

    public Arm getArm(){
        return s_Arm;
    }

    public Swerve getSwerve(){
        return s_Swerve;
    }


    public void configAuto() {

        /* Populate Event Map */
       HashMap<String, Command> eventMap = new HashMap<String, Command>();
       //eventMap.put("SetCube", new InstantCommand(() -> isCube = true));
       //eventMap.put("SetCone", new InstantCommand(()-> isCube = false));


        /*Wait Times */
        eventMap.put("Wait5", new AutoWait(5));
        eventMap.put("Wait1", new AutoWait(1));
        eventMap.put("Wait1.5",new AutoWait(1.5));

        /* End Event Map */


        /* Auto Builder */
        autoBuilder = new SwerveAutoBuilder(
            s_PoseEstimator::getPose,
            s_PoseEstimator::resetPose,
            Constants.Swerve.swerveKinematics,
            new PIDConstants(Constants.AutoConstants.translationPID.kP, Constants.AutoConstants.translationPID.kI,
                Constants.AutoConstants.translationPID.kD),
            new PIDConstants(Constants.AutoConstants.rotationPID.kP, Constants.AutoConstants.rotationPID.kI,
                Constants.AutoConstants.rotationPID.kD),
            s_Swerve::setModuleStates,
            eventMap,
            s_Swerve
        );
      }

      public void configRoutine() {

          /* AutoChosser */
          m_chooser.setDefaultOption("PurpleYellow", new PurpleYellow(autoBuilder, s_PoseEstimator));
          m_chooser.addOption("LineAuto", new LINEAuto(autoBuilder, s_PoseEstimator));
          m_chooser.addOption("None", null);

          Shuffleboard.getTab("Autonmous").add(m_chooser).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 0)
                  .withSize(2, 1);
      }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // selected auto will run
        return m_chooser.getSelected();
        //return a_PurpleYellow;
    }
}
