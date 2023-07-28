package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

public class Score extends CommandBase{
    private Arm s_Arm;
    private Intake s_Intake;
    private Wrist s_Wrist; 
    private Pivot s_Pivot;
    private Leds s_Leds;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;


    public Score(Arm s_Arm, Intake s_Intake, Pivot s_Pivot, Wrist s_Wrist, Leds s_Leds){
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

    public void initialize(){
      
        ArmPosition = Constants.Arm.stowPosition;;
        PivotPosition = Constants.Pivot.stowPosition;
        WristPosition = Constants.Wrist.stowPosition;

        Timer.reset();
        Timer.start();

        finished = false;
    }

    @Override
    public void execute(){

        if(!RobotContainer.allowScore){

                if(RobotContainer.isCube){

                            s_Pivot.setDesiredPosition(PivotPosition);
                            s_Pivot.goToPosition();
                         
                    if(Timer.hasElapsed(.3)){
                            s_Arm.setDesiredPosition(ArmPosition);
                            s_Arm.goToPosition();
                        }
                    if(Timer.hasElapsed(.6)){
                            s_Wrist.setDesiredPosition(WristPosition);
                            s_Wrist.goToPosition();
                            finished = true;
                        }
                    } else {

                            s_Pivot.setDesiredPosition(PivotPosition);
                            s_Pivot.goToPosition();

                   if(Timer.hasElapsed(.3)){
                            s_Arm.setDesiredPosition(ArmPosition);
                            s_Arm.goToPosition();
                        }
                   if(Timer.hasElapsed(.6)){
                            s_Wrist.setDesiredPosition(WristPosition);
                            s_Wrist.goToPosition();
                            finished = true;
                        }
                    }  

                } else {
            
                if(RobotContainer.isCube){

                    s_Intake.outake();

                if(Timer.hasElapsed(.2)){
                        s_Intake.stop();
                        s_Pivot.setDesiredPosition(PivotPosition);
                        s_Pivot.goToPosition();
                    }
                if(Timer.hasElapsed(.3)){
                        s_Arm.setDesiredPosition(ArmPosition);
                        s_Arm.goToPosition();
                    }
                if(Timer.hasElapsed(.6)){
                        s_Wrist.setDesiredPosition(WristPosition);
                        s_Wrist.goToPosition();
                        s_Leds.clearAnimation();
                        s_Leds.purple();
                        finished = true;
                    }
                } else{
            
                    s_Intake.outake();

                if(Timer.hasElapsed(.2)){
                         s_Intake.stop();
                         s_Pivot.setDesiredPosition(PivotPosition);
                         s_Pivot.goToPosition();
                    }
                if(Timer.hasElapsed(.3)){
                        s_Arm.setDesiredPosition(ArmPosition);
                        s_Arm.goToPosition();
                    }
                if(Timer.hasElapsed(.6)){
                        s_Wrist.setDesiredPosition(WristPosition);
                        s_Wrist.goToPosition();
                        s_Leds.clearAnimation();
                        s_Leds.yellow();
                        finished = true;          
                    }
                }
            }    
        }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
