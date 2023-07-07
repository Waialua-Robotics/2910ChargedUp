package org.WaialuaRobotics359.robot.commands.SetPoints.Pickup;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;


import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PickupPosition extends CommandBase{
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


    public PickupPosition(Arm s_Arm, Intake s_Intake, Flight s_Flight, Wrist s_Wrist, Leds s_Leds){
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
        if (RobotContainer.isCube){
            ArmPosition = Constants.Arm.Cube.groundPosition;
            PivotPosition = Constants.Pivot.Cube.groundPosition;
            WristPosition = Constants.Wrist.Cube.groundPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.groundPosition;
            PivotPosition = Constants.Arm.Cone.groundPosition;
            WristPosition = Constants.Arm.Cone.groundPosition;
        }
        Timer.reset();
        Timer.start();


        finished = false;


    }

    @Override
    public void execute(){


        if(RobotContainer.isCube){

                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();

                if(Timer.hasElapsed(.4)){
                    s_Wrist.setDesiredPosition(ArmPosition);
                    s_Wrist.goToPosition();
                }
                
                if(Timer.hasElapsed(.6)){
                    s_Intake.intake();
                }

                if(s_Flight.getSensorRange() < 500){
                    s_Intake.stop();
                    s_Leds.red();
                    finished = true;

                }
            } else {
                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();

                if(Timer.hasElapsed(.4)){
                    s_Wrist.setDesiredPosition(ArmPosition);
                    s_Wrist.goToPosition();
                }
                
                if(Timer.hasElapsed(.6)){
                    s_Intake.intake();
                }

                if(s_Flight.getSensorRange() < 500){
                    s_Intake.stop();
                    s_Leds.red();
                    finished = true;

                }
            }

                            
    }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
