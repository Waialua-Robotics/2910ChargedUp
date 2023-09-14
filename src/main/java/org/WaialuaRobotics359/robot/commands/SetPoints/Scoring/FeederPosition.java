package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class FeederPosition extends CommandBase {

    private Arm s_Arm;
    private Intake s_Intake;
    private Wrist s_Wrist; 
    private Flight s_Flight;
    private Leds s_Leds;
    private Pivot s_Pivot;

    private static int ArmPosition;
    private static int WristPosition;
    private static int PivotPosition;

    public FeederPosition(Arm s_Arm, Intake s_Intake, Flight s_Flight, Wrist s_Wrist, Leds s_Leds, Pivot s_Pivot){
        this.s_Arm = s_Arm;
        this.s_Intake = s_Intake;
        this.s_Flight = s_Flight;
        this.s_Wrist = s_Wrist;
        this.s_Leds = s_Leds;
        this.s_Pivot = s_Pivot;
        addRequirements(s_Arm);
        addRequirements(s_Intake);
        addRequirements(s_Flight);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize(){
        s_Leds.hasPiece = false;
        if (RobotContainer.isCube){
            ArmPosition = Constants.Arm.Cube.feederPosition;
            WristPosition = Constants.Wrist.Cube.feederPosition;
            PivotPosition = Constants.Pivot.Cube.feederPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.feederPosition;
            WristPosition = Constants.Wrist.Cone.feederPosition;
            PivotPosition = Constants.Pivot.Cone.feederPosition;
        }

        Timer.reset();
        Timer.start();

        finished = false;
    }

    @Override
    public void execute(){

            if(RobotContainer.isCube){

                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();

                if(Timer.hasElapsed(.2)){
                    s_Arm.setDesiredPosition(ArmPosition);
                    s_Arm.goToPosition();
                }

                if(Timer.hasElapsed(.4)){
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                }

                if(Timer.hasElapsed(.6)){
                    s_Intake.intake();
                    new InstantCommand(()-> s_Leds.actionReady = true);
                    //finished = true;
                }

                if(s_Intake.current() > 40 && Timer.hasElapsed(.8)){
                    //s_Intake.intakeIdle();
                    s_Wrist.setDesiredPosition(Constants.Wrist.stowPosition);
                    s_Wrist.goToPosition();
                    new InstantCommand(()-> s_Leds.actionReady = true);
                    s_Leds.hasPiece = true;
                    finished = true;
                }

            } else {

                    s_Pivot.setDesiredPosition(PivotPosition);
                    s_Pivot.goToPosition();
                    RobotContainer.retractOnScore = true;

                if(Timer.hasElapsed(.075)){
                    s_Arm.setDesiredPosition(ArmPosition);
                    s_Arm.goToPosition();
                }

                if(Timer.hasElapsed(.4)){
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                }

                if(Timer.hasElapsed(.6)){
                    s_Intake.intake();
                }

                if(s_Flight.getSensorRange() < 200){
                    s_Intake.intakeIdle();
                    new InstantCommand(()-> s_Leds.actionReady = true);
                    s_Leds.hasPiece = true;
                    finished = true;

                }
            }
        }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
