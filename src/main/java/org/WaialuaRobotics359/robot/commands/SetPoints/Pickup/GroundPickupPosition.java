package org.WaialuaRobotics359.robot.commands.SetPoints.Pickup;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Timer;

public class GroundPickupPosition extends CommandBase{
    private Arm s_Arm;
    private Intake s_Intake;
    private Pivot s_Pivot;
    private Wrist s_Wrist; 
    private Flight s_Flight;
    private Leds s_Leds;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public GroundPickupPosition(Arm s_Arm, Intake s_Intake, Flight s_Flight, Wrist s_Wrist, Leds s_Leds, Pivot s_Pivot){
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

    private boolean disqalifyTOF = false; 

    private double flightValue;

    private Timer Timer = new Timer();
    private Timer IntakeingTimer = new Timer();

    public void initialize(){
        s_Leds.noPiece();
        if (RobotContainer.isCube){
            ArmPosition = Constants.Arm.Cube.groundPosition;
            PivotPosition = Constants.Pivot.Cube.groundPosition;
            WristPosition = Constants.Wrist.Cube.groundPosition;
        } else {
            ArmPosition = Constants.Arm.Cone.groundPosition;
            PivotPosition = Constants.Pivot.Cone.groundPosition;
            WristPosition = Constants.Wrist.Cone.groundPosition;
        }

        if(s_Flight.getSensorRange() < 300){
            disqalifyTOF = true;
        }else{
            disqalifyTOF = false;
        }

        Timer.reset();
        Timer.start();

        IntakeingTimer.reset();
        IntakeingTimer.stop();

        finished = false;
    }

    @Override
    public void execute(){

        if(disqalifyTOF){
          flightValue = 400;
        }else{
          flightValue = s_Flight.getSensorRange();
        }

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
            s_Intake.intake();
        }

        if(s_Intake.current() > 80 && Timer.hasElapsed(1)){
            s_Wrist.setDesiredPosition(0);
            s_Wrist.goToPosition();
            s_Leds.hasPiece();
            new InstantCommand(()-> s_Leds.actionReady = false);
        }

        if(s_Leds.hasPiece && s_Wrist.getPosition()<100){
            s_Intake.intakeIdle();
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
            s_Intake.intake();
        }

        if(flightValue < 300){
            IntakeingTimer.start();
            s_Leds.hasPiece();
            new InstantCommand(()-> s_Leds.actionReady = false);
            finished = true;
        }

        if(IntakeingTimer.hasElapsed(.5)){
            s_Intake.intakeIdle();
        }

    }                        
}
    
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
