package org.WaialuaRobotics359.robot.commands.SetPoints.Scoring;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class LowPosition extends CommandBase{
    private Arm s_Arm;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Leds s_Leds;
    private PoseEstimator s_Pose;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public LowPosition(Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds, PoseEstimator s_Pose){
        this.s_Arm = s_Arm;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        this.s_Leds = s_Leds;
        this.s_Pose = s_Pose;
        addRequirements(s_Arm);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
    }

    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize() {
        if (RobotContainer.isCube) {
            if (true) { //WAS s_Pose.isFrontScore
                ArmPosition = Constants.Arm.Cube.frontLowPosition;
                PivotPosition = Constants.Pivot.Cube.frontLowPosition;
                WristPosition = Constants.Wrist.Cube.frontLowPosition;
            } else {
                ArmPosition = Constants.Arm.Cube.lowPosition;
                PivotPosition = Constants.Pivot.Cube.lowPosition;
                WristPosition = Constants.Wrist.Cube.lowPosition;
            }
        } else {
            if (true) {
                ArmPosition = Constants.Arm.Cone.frontLowPosition;
                PivotPosition = Constants.Pivot.Cone.frontLowPosition;
                WristPosition = Constants.Wrist.Cone.frontLowPosition;
            } else {
                ArmPosition = Constants.Arm.Cone.lowPosition;
                PivotPosition = Constants.Pivot.Cone.lowPosition;
                WristPosition = Constants.Wrist.Cone.lowPosition;
            }
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

        //if(Timer.hasElapsed(.4) || s_Pivot.inPosition()){
            s_Wrist.setDesiredPosition(WristPosition);
            s_Wrist.goToPosition();
        //}

        if(Timer.hasElapsed(.6)){
            new InstantCommand(()-> s_Leds.actionReady = true);
            finished = true;
        }
    }
}

    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}

