package org.WaialuaRobotics359.robot.commands.SetPoints;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;


public class StowPosition extends CommandBase{
    private Arm s_Arm;
    private Leds s_Leds;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public StowPosition(Arm s_Arm, Leds s_Leds, Flight s_Flight, Wrist s_Wrist, Pivot s_Pivot){
        this.s_Arm = s_Arm;
        this.s_Leds = s_Leds;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        addRequirements(s_Arm);
        addRequirements(s_Flight);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize(){

        ArmPosition = Constants.Arm.stowPosition;
        PivotPosition = Constants.Pivot.stowPosition;
        WristPosition = Constants.Wrist.stowPosition;

        Timer.reset();
        Timer.start();

        finished = false;
    }

    @Override
    public void execute(){

        s_Arm.setDesiredPosition(ArmPosition);
        s_Arm.goToPosition();
        s_Wrist.setDesiredPosition(WristPosition);
        s_Wrist.goToPosition();

        if(s_Arm.isRetracted()){
            s_Pivot.setDesiredPosition(PivotPosition);
            s_Pivot.goToPosition();
        }
/* 
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

        if(Timer.hasElapsed(.8)){
            s_Leds.clearAnimation();
            s_Leds.purple();
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

        if(Timer.hasElapsed(.8)){
            s_Leds.clearAnimation();
            s_Leds.yellow();
            finished = true;
        }
    }
    */
}
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
