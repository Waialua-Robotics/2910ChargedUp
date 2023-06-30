package org.WaialuaRobotics359.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import org.WaialuaRobotics359.robot.autos.*;
import org.WaialuaRobotics359.robot.commands.*;
import org.WaialuaRobotics359.robot.commands.Manual.*;
import org.WaialuaRobotics359.robot.commands.SetPoints.*;
import org.WaialuaRobotics359.robot.commands.SetPoints.Pickup.MidPickupPosition;
import org.WaialuaRobotics359.robot.commands.SetPoints.Pickup.PickupPosition;
import org.WaialuaRobotics359.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    public static boolean isCube;
    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final Joystick operator = new Joystick(1);


    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton ResetMods = new JoystickButton(driver, XboxController.Button.kStart.value); 

    /* Operator Controls */

    /* Operator Buttons */
   
    private final int intakeAxis = Constants.OI.intakeAxis;
    private final int outakeAxis = Constants.OI.outakeAxis;

    private final JoystickButton lowPickup = new JoystickButton(operator, Constants.OI.lowPickup);
    private final JoystickButton midPickup = new JoystickButton(operator, Constants.OI.midPckup);

    private final JoystickButton setCube = new JoystickButton(operator, Constants.OI.isCube);
    private final JoystickButton setCone = new JoystickButton(operator, Constants.OI.isCone);

    private final JoystickButton stow = new JoystickButton(operator, Constants.OI.stow);



    /* Subsystems */
    //private final Swerve s_Swerve = new Swerve();
    private final Intake s_Intake = new Intake();
    private final Wrist s_Wrist = new Wrist();
    private final Arm s_Arm = new Arm();
    private final Flight s_Flight = new Flight();
    private final Leds s_Leds = new Leds();
    //private final LEDsSubsystem s_LEDs = new LEDsSubsystem();

    /*The autonomous routines*/

    //private final Command m_twomAuto = new twomAuto(s_Swerve);
    //private final Command m_SwerveBuilderAuto = new swerveBuilderAuto(s_Swerve);

    /*chooser for autonomous commands*/
    SendableChooser<Command> m_chooser = new SendableChooser<>();


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        /* 
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
            s_Swerve, 
            () -> -driver.getRawAxis(translationAxis), 
            () -> -driver.getRawAxis(strafeAxis), 
            () -> -driver.getRawAxis(rotationAxis), 
            () -> robotCentric.getAsBoolean()
            )
        );*/


        s_Intake.setDefaultCommand(
            new ManualIntake(
                s_Intake,
                () -> operator.getRawAxis(intakeAxis),
                () -> operator.getRawAxis(outakeAxis)
            )
        );

      
        

        

        // Configure the button bindings
        configureButtonBindings();

        // Add commands to the autonomous command chooser
        //m_chooser.setDefaultOption("swerveBuilderAuto", m_SwerveBuilderAuto);
        //m_chooser.addOption("twomAuto", m_twomAuto);

        // Put the chooser on the dashboard
        Shuffleboard.getTab("Autonomous").add(m_chooser);

        // Populate the autonomous event map
        setEventMap();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        //zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
        //ResetMods.onTrue(new InstantCommand(() -> s_Swerve.resetModulesToAbsolute()));

            setCube.onTrue(
                new ParallelCommandGroup( new InstantCommand(() -> isCube = true),
                new InstantCommand(() -> s_Leds.purple())));
            setCone.onTrue(
                new ParallelCommandGroup( new InstantCommand(() -> isCube = false),
                new InstantCommand(() -> s_Leds.yellow())));

            lowPickup.whileTrue(new PickupPosition(s_Arm, s_Intake, s_Flight, s_Wrist));
            lowPickup.onFalse(new StowPosition(s_Arm, s_Intake, s_Flight, s_Wrist));
            
            midPickup.whileTrue(new MidPickupPosition(s_Arm, s_Intake, s_Flight, s_Wrist));
            midPickup.onFalse(new StowPosition(s_Arm, s_Intake, s_Flight, s_Wrist));


            stow.onTrue(new StowPosition(s_Arm, s_Intake, s_Flight, s_Wrist));
    }
    

    public Leds getLeds(){
        return s_Leds;
    }

    public void setEventMap() {
    //Constants.eventMap.put("LedBlue", new InstantCommand(() -> s_LEDs.LEDsBlue()));
    //SystemConstants.eventMap.put("intakeRetract", new InstantCommand(m_robotIntake::intakeRetract, m_robotIntake));
      }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // selected auto will run
        return m_chooser.getSelected();
    }
}
