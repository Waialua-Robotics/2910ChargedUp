package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class HighPosition extends CommandBase {

    private Arm s_Arm;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Leds s_Leds;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public HighPosition(Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds){
        this.s_Arm = s_Arm;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        this.s_Leds = s_Leds;
        addRequirements(s_Arm);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize(){
        if (RobotContainer.isCube){
            ArmPosition = Constants.Arm.Cube.highPosition;
            PivotPosition = Constants.Pivot.Cube.highPosition;
            WristPosition = Constants.Wrist.Cube.highPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.highPosition;
            PivotPosition = Constants.Pivot.Cone.highPosition;
            WristPosition = Constants.Wrist.Cone.highPosition;
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

                if(Timer.hasElapsed(5.7)){
                    new InstantCommand(()-> s_Leds.actionReady = true);
                    finished = true;
                }

            } else {

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
                    new InstantCommand(()-> s_Leds.actionReady = true);
                    RobotContainer.retractOnScore = true;
                    finished = true;
                }
            }
        }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
