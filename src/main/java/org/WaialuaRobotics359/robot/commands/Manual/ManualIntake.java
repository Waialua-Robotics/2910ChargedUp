package org.WaialuaRobotics359.robot.commands.Manual;

import java.util.function.BooleanSupplier;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualIntake extends CommandBase{

    private Intake s_Intake;
    private BooleanSupplier intake;
    private BooleanSupplier outake;
    double intakeSpeed;
    double outakeSpeed;
    private boolean finished = false;
    
    public ManualIntake(Intake s_Intake, BooleanSupplier intake, BooleanSupplier outake){
        this.intake = intake;
        this.outake = outake;
        this.s_Intake = s_Intake;
        addRequirements(s_Intake);
    }

    

    @Override
    public void execute(){
        boolean rBumperValue = intake.getAsBoolean();
        boolean lBumberValue = outake.getAsBoolean();

        intakeSpeed = Constants.Intake.intakeSpeed;
        outakeSpeed = Constants.Intake.outakeSpeed;

        if (rBumperValue){
            s_Intake.intake(intakeSpeed);
        }else if (lBumberValue){
            s_Intake.intake(outakeSpeed);
        }else{
            s_Intake.stop();
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
