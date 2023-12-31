// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.WaialuaRobotics359.robot;

import org.WaialuaRobotics359.robot.util.CTREConfigs;

import com.pathplanner.lib.server.PathPlannerServer;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static CTREConfigs ctreConfigs;

  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    ctreConfigs = new CTREConfigs();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    PathPlannerServer.startServer(5811);
    }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    m_robotContainer.getLeds().allOff();
  }

  @Override
  public void disabledPeriodic() {
    //m_robotContainer.getSwerve().resetModulesToAbsolute();
    m_robotContainer.getSwerve().setCurrentAngle();

    m_robotContainer.getPivot().autoPos();
    m_robotContainer.getArm().autoPos();
    m_robotContainer.getWrist().autoPos();

    m_robotContainer.getPivot().autoStart();
    m_robotContainer.getArm().autoStart();
    m_robotContainer.getWrist().autoStart();

    m_robotContainer.getArm().stop();
    m_robotContainer.getPivot().stop();
    m_robotContainer.getWrist().stop();
    m_robotContainer.getArm().setCurrentPosition();
    m_robotContainer.getPivot().setCurrentPosition();
    m_robotContainer.getWrist().setCurrentPosition();

    if (!m_robotContainer.brakeToggle.get() && m_robotContainer.toggleMode) {

      m_robotContainer.toggleMode = false;

      if (m_robotContainer.brakeMode) {
        m_robotContainer.getArm().setBrake();
        m_robotContainer.getPivot().setBrake();
        m_robotContainer.getWrist().setBrake();
      } else if (!m_robotContainer.brakeMode) {
        m_robotContainer.getArm().setCoast();
        m_robotContainer.getPivot().setCoast();
        m_robotContainer.getWrist().setCoast();
      }

      m_robotContainer.brakeMode = !m_robotContainer.brakeMode;
    }

    if (m_robotContainer.brakeToggle.get()) {
      m_robotContainer.toggleMode = true;
    }

    if (!m_robotContainer.zero.get() && m_robotContainer.zeroMode) {
      m_robotContainer.zeroMode = false;
      m_robotContainer.getSwerve().zeroGyro();
      m_robotContainer.getArm().setPosition(0);
      m_robotContainer.getArm().setDesiredPosition(0);
      m_robotContainer.getArm().goToPosition();
      m_robotContainer.getPivot().setPosition(0);
      m_robotContainer.getPivot().setDesiredPosition(0);
      m_robotContainer.getPivot().goToPosition();
      m_robotContainer.getWrist().setPosition(0);
      m_robotContainer.getWrist().setDesiredPosition(0);
      m_robotContainer.getWrist().goToPosition();
    } else {
      m_robotContainer.zeroMode = true;
    }

    m_robotContainer.getLeds().DisabledLed(!m_robotContainer.zero.get(),m_robotContainer.brakeMode );

  }

  /*
   * if pressed && firsttime
   *  first time = false
   *  set toogle oppisite true 
   * else 
   * first time = true
   */

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
