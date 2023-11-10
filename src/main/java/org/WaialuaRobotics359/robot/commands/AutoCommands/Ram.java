package org.WaialuaRobotics359.robot.commands.AutoCommands;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class Ram extends CommandBase {

    private Arm s_Arm;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Leds s_Leds;
    private Intake s_Intake;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public Ram(Intake s_Intake, Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds){
        this.s_Arm = s_Arm;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        this.s_Leds = s_Leds;
        this.s_Intake = s_Intake;
        addRequirements(s_Arm);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
        addRequirements(s_Intake);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize(){
        ArmPosition = Constants.Arm.Cube.groundPosition;
        PivotPosition = Constants.Pivot.Cube.groundPosition;
        WristPosition = Constants.Wrist.Cube.groundPosition;
        Timer.reset();
        Timer.start();


        finished = false;

    }

    @Override
    public void execute(){

                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
                
                if(Timer.hasElapsed(.2)){
                    s_Arm.setDesiredPosition(ArmPosition);
                    s_Arm.goToPosition();
                }

                if(Timer.hasElapsed(.4)){
                    s_Wrist.setDesiredPosition(WristPosition);
                    s_Wrist.goToPosition();
                    finished = true;
                }
 
            }                        

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
