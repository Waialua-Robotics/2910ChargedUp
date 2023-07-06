package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Flight;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import org.WaialuaRobotics359.robot.subsystems.Leds;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Score extends CommandBase{
    private Arm s_Arm;
    private Intake s_Intake;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Flight s_Flight;
    private Leds s_Leds;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    private static int MidArmPosition;
    private static int MidPivotPosition;
    private static int MidWristPosition;


    public Score(Arm s_Arm, Intake s_Intake, Flight s_Flight, Wrist s_Wrist, Leds s_Leds){
        this.s_Arm = s_Arm;
        this.s_Intake = s_Intake;
        this.s_Flight = s_Flight;
        this.s_Wrist = s_Wrist;
        this.s_Leds = s_Leds;
        addRequirements(s_Arm);
        addRequirements(s_Intake);
        addRequirements(s_Flight);
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

        System.out.println("initialize");

        finished = false;
        System.out.println("initialize2");

    }

    @Override
    public void execute(){

        System.out.println("ex");

        if(RobotContainer.isCube){
                s_Intake.outake(100);

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
                    s_Leds.purple();
                    finished = true;
                }       
            }else {
                s_Intake.outake(100);

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
                    s_Leds.yellow();
                    finished = true;
            }   
        }        
    }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
