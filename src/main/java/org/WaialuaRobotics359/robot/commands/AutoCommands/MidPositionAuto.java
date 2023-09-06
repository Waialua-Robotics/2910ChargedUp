package org.WaialuaRobotics359.robot.commands.AutoCommands;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class MidPositionAuto extends CommandBase {

    private Arm s_Arm;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Leds s_Leds;
    private Intake s_Intake;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public MidPositionAuto(Intake s_Intake, Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds){
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
        if (RobotContainer.isCube){
            ArmPosition = Constants.Arm.Cube.midPosition;
            PivotPosition = Constants.Pivot.Cube.midPosition;
            WristPosition = Constants.Wrist.Cube.midPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.autoMidPosition;
            PivotPosition = Constants.Pivot.Cone.autoMidPosition;
            WristPosition = Constants.Wrist.Cone.autoMidPosition;
        }
        Timer.reset();
        Timer.start();


        finished = false;

    }

    @Override
    public void execute(){
        s_Pivot.stowSpeed();
        s_Arm.stowSpeed();
        
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
                new InstantCommand(()-> s_Leds.actionReady = true);
                RobotContainer.retractOnScore = true;
                s_Pivot.normSpeed();
                s_Arm.normSpeed();
                finished = true;
            }

        } else {

            s_Arm.setDesiredPosition(ArmPosition);
            s_Arm.goToPosition();

            if (Timer.hasElapsed(.2)) {
                s_Wrist.setDesiredPosition(WristPosition);
                s_Wrist.goToPosition();
            }

            if (Timer.hasElapsed(.4)) {
                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
            }

            if (Timer.hasElapsed(.6) && s_Pivot.inPosition()) {
                new InstantCommand(() -> s_Leds.actionReady = true);
                RobotContainer.retractOnScore = true;
                s_Pivot.normSpeed();
                s_Arm.normSpeed();
                finished = true;
                System.out.print("Print");
            }

        }
        
    }

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
