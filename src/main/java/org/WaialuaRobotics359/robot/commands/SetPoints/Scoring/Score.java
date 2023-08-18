package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.Robot;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.Timer;

public class Score extends CommandBase {
    private Arm s_Arm;
    private Intake s_Intake;
    private Wrist s_Wrist;
    private Pivot s_Pivot;
    private Leds s_Leds;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public Score(Arm s_Arm, Intake s_Intake, Pivot s_Pivot, Wrist s_Wrist, Leds s_Leds) {
        this.s_Arm = s_Arm;
        this.s_Intake = s_Intake;
        this.s_Pivot = s_Pivot;
        this.s_Wrist = s_Wrist;
        this.s_Leds = s_Leds;
        addRequirements(s_Arm);
        addRequirements(s_Intake);
        addRequirements(s_Pivot);
        addRequirements(s_Wrist);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize() {

        ArmPosition = Constants.Arm.stowPosition;
        PivotPosition = Constants.Pivot.stowPosition;
        WristPosition = Constants.Wrist.stowPosition;

        Timer.reset();
        Timer.start();

        finished = false;
    }

    @Override
    public void execute() {

        if (!RobotContainer.allowScore) {

            if (RobotContainer.isCube) {

                if (RobotContainer.retractOnScore) {
                    s_Pivot.setDesiredPosition(PivotPosition); // s_Pivot.backScoreRetract()
                    s_Pivot.goToPosition();
                }

                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();

                if (Timer.hasElapsed(.3)) {
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                }
                if (Timer.hasElapsed(.6)) {
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                    RobotContainer.retractOnScore = false;
                    finished = true;
                }
            } else {

                if (RobotContainer.retractOnScore) {
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                }

                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();

                if (Timer.hasElapsed(.3)) {
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                }
                if (Timer.hasElapsed(.6)) {
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                    RobotContainer.retractOnScore = false;
                    finished = true;
                }
            }

        } else {

            if (RobotContainer.isCube) {

                s_Intake.outake();

                if (RobotContainer.retractOnScore) {
                    s_Pivot.setDesiredPosition(PivotPosition); // s_Pivot.backScoreRetract()
                    s_Pivot.goToPosition();
                }

                if (Timer.hasElapsed(.1)) {
                    s_Arm.setDesiredPosition(ArmPosition);
                    s_Arm.goToPosition();
                }

                if (Timer.hasElapsed(.2)) { // timer.3
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                    s_Intake.stop();
                }
                if (Timer.hasElapsed(.6)) { // timer.6
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                    s_Leds.noPiece();
                    new InstantCommand(() -> s_Leds.actionReady = true);
                    RobotContainer.retractOnScore = false;
                    finished = true;
                }

            } else {

                s_Intake.outake();

                if (RobotContainer.retractOnScore) {
                    s_Pivot.setDesiredPosition(PivotPosition); // s_Pivot.backScoreRetract()
                    s_Pivot.goToPosition();
                }

                if (Timer.hasElapsed(.1)) {
                    s_Arm.setDesiredPosition(ArmPosition);
                    s_Arm.goToPosition();
                }

                if (Timer.hasElapsed(.2)) { // timer.3
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                    s_Intake.stop();
                }
                if (Timer.hasElapsed(.6)) { // timer.6
                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                    s_Leds.noPiece();
                    new InstantCommand(() -> s_Leds.actionReady = true);
                    RobotContainer.retractOnScore = false;
                    finished = true;
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
