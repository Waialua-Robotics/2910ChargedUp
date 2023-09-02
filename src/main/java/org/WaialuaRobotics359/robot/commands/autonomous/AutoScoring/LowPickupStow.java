package org.WaialuaRobotics359.robot.commands.autonomous.AutoScoring;

import edu.wpi.first.wpilibj.Timer;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Flight;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import org.WaialuaRobotics359.robot.subsystems.Leds;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class LowPickupStow extends CommandBase {

    private Arm s_Arm;
    private Pivot s_Pivot;
    private Wrist s_Wrist;
    private Leds s_Leds;
    private Intake s_Intake;
    private Flight s_Flight;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public LowPickupStow(Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Intake s_Intake, Leds s_Leds, Flight s_Flight) {
        this.s_Arm = s_Arm;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        this.s_Leds = s_Leds;
        this.s_Intake = s_Intake;
        this.s_Flight = s_Flight;
        addRequirements(s_Arm);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
        addRequirements(s_Intake);
        addRequirements(s_Flight);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize() {

        if (RobotContainer.isCube) {
            ArmPosition = Constants.Arm.Cube.groundPosition;
            PivotPosition = Constants.Pivot.Cube.groundPosition;
            WristPosition = Constants.Wrist.Cube.groundPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.groundPosition;
            PivotPosition = Constants.Pivot.Cone.groundPosition;
            WristPosition = Constants.Wrist.Cone.groundPosition;
        }

        Timer.reset();
        Timer.start();
    }

    @Override
    public void execute() {

        if (RobotContainer.isCube) {
            s_Pivot.setDesiredPosition(PivotPosition);
            s_Pivot.goToPosition();

            if (Timer.hasElapsed(.2)) {
                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();
            }

            if (Timer.hasElapsed(.4)) {
                s_Wrist.setDesiredPosition(WristPosition);
                s_Wrist.goToPosition();
            }

            if (Timer.hasElapsed(.6)) {
                new InstantCommand(() -> s_Leds.actionReady = true);
                s_Intake.intake();
            }

            if (s_Intake.current() > 40) {
                s_Intake.intakeIdle();
                s_Leds.hasPiece();
                new InstantCommand(() -> s_Leds.actionReady = false);
            }
                if (s_Leds.hasPiece) {
                    Timer.reset();
                    Timer.start();
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();

                    if (Timer.hasElapsed(0)) {
                        s_Arm.setDesiredPosition(ArmPosition);
                        s_Arm.goToPosition();
                    }

                    if (Timer.hasElapsed(.3) || s_Pivot.isRetracted()) {
                        s_Wrist.setDesiredPosition(WristPosition);
                        s_Wrist.goToPosition();
                    }
                    if (Timer.hasElapsed(.6)) {
                        finished = true;
                    }
                }

        } else {
            s_Pivot.setDesiredPosition(PivotPosition);
            s_Pivot.goToPosition();

            if (Timer.hasElapsed(.2)) {
                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();
            }

            if (Timer.hasElapsed(.4)) {
                s_Wrist.setDesiredPosition(WristPosition);
                s_Wrist.goToPosition();
            }

            if (Timer.hasElapsed(.6)) {
                new InstantCommand(() -> s_Leds.actionReady = true);
                s_Intake.intake();
            }

            if (s_Flight.getSensorRange() < 200) {
                s_Intake.intakeIdle();
                s_Leds.hasPiece();
                new InstantCommand(() -> s_Leds.actionReady = false);
            }
                if (s_Leds.hasPiece) {
                    Timer.reset();
                    Timer.start();
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();

                    if (Timer.hasElapsed(0)) {
                        s_Arm.setDesiredPosition(ArmPosition);
                        s_Arm.goToPosition();
                    }

                    if (Timer.hasElapsed(.3) || s_Pivot.isRetracted()) {
                        s_Wrist.setDesiredPosition(WristPosition);
                        s_Wrist.goToPosition();
                    }
                    if (Timer.hasElapsed(.6)) {
                        s_Pivot.setDesiredPosition(PivotPosition);
                        s_Pivot.goToPosition();
                        s_Pivot.normSpeed();
                        s_Arm.normSpeed();
                        finished = true;
                    }
                }
            }
        }

    }
