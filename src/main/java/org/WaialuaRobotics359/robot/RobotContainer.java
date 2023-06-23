package org.WaialuaRobotics359.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import org.WaialuaRobotics359.robot.autos.*;
import org.WaialuaRobotics359.robot.commands.*;
import org.WaialuaRobotics359.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver = new Joystick(0);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton ResetMods = new JoystickButton(driver, XboxController.Button.kStart.value); 

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    //private final LEDsSubsystem s_LEDs = new LEDsSubsystem();

    /*The autonomous routines*/

    private final Command m_twomAuto = new twomAuto(s_Swerve);
    private final Command m_SwerveBuilderAuto = new swerveBuilderAuto(s_Swerve);

    /*chooser for autonomous commands*/
    SendableChooser<Command> m_chooser = new SendableChooser<>();


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
            s_Swerve, 
            () -> -driver.getRawAxis(translationAxis), 
            () -> -driver.getRawAxis(strafeAxis), 
            () -> -driver.getRawAxis(rotationAxis), 
            () -> robotCentric.getAsBoolean()
            )
        );

        // Configure the button bindings
        configureButtonBindings();

        // Add commands to the autonomous command chooser
        m_chooser.setDefaultOption("swerveBuilderAuto", m_SwerveBuilderAuto);
        m_chooser.addOption("twomAuto", m_twomAuto);

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
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
        ResetMods.onTrue(new InstantCommand(() -> s_Swerve.resetModulesToAbsolute()));

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
