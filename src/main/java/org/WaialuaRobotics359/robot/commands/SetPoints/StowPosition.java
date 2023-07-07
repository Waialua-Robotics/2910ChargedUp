package org.WaialuaRobotics359.robot.commands.SetPoints;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Flight;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StowPosition extends CommandBase{
    private Arm s_Arm;
    private Intake s_Intake;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Flight s_Flight;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    private static int MidArmPosition;
    private static int MidPivotPosition;
    private static int MidWristPosition;


    public StowPosition(Arm s_Arm, Intake s_Intake, Flight s_Flight, Wrist s_Wrist){
        this.s_Arm = s_Arm;
        this.s_Intake = s_Intake;
        this.s_Flight = s_Flight;
        this.s_Wrist = s_Wrist;
        addRequirements(s_Arm);
        addRequirements(s_Intake);
        addRequirements(s_Flight);
        addRequirements(s_Wrist);
    }

    private boolean finished = false;


    public void initialize(){
    }

    @Override
    public void execute(){

        System.out.println("ex");

                s_Arm.setDesiredPosition(0);
                s_Arm.goToPosition();
                s_Wrist.setDesiredPosition(0);
                s_Wrist.goToPosition();
                s_Intake.stop();
                  
    }
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
