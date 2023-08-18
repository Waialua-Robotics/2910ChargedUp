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
    private Intake s_Intake;

    private static int ArmPosition;
    private static int PivotPosition;
    private static int WristPosition;

    public StowPosition(Intake s_Intake, Arm s_Arm, Leds s_Leds, Flight s_Flight, Wrist s_Wrist, Pivot s_Pivot){
        this.s_Arm = s_Arm;
        this.s_Leds = s_Leds;
        this.s_Wrist = s_Wrist;
        this.s_Pivot = s_Pivot;
        this.s_Intake = s_Intake;
        addRequirements(s_Arm);
        addRequirements(s_Flight);
        addRequirements(s_Wrist);
        addRequirements(s_Pivot);
        addRequirements(s_Intake);
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
        s_Intake.intakeIdle();

           if (RobotContainer.isCube) {

            if (RobotContainer.retractOnScore) {
                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
            }

            if (Timer.hasElapsed(0)) {
                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();
            }

            if (Timer.hasElapsed(.3)) {
                s_Wrist.setDesiredPosition(WristPosition);
                s_Wrist.goToPosition();
            }
            if (Timer.hasElapsed(.6)) {
                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
                RobotContainer.retractOnScore = false;
                finished = true;
            }

        } else {

            if (RobotContainer.retractOnScore) {
                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
            }

            if (Timer.hasElapsed(0)) {
                s_Arm.setDesiredPosition(ArmPosition);
                s_Arm.goToPosition();
            }

            if (Timer.hasElapsed(.3)) {
                s_Wrist.setDesiredPosition(WristPosition);
                s_Wrist.goToPosition();
            }
            if (Timer.hasElapsed(.6)) {
                s_Pivot.setDesiredPosition(PivotPosition);
                s_Pivot.goToPosition();
                RobotContainer.retractOnScore = false;
                finished = true;
            }
        }

    }
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
