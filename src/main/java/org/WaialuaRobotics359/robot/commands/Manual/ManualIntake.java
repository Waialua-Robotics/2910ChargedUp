package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.BooleanSupplier;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import org.WaialuaRobotics359.robot.subsystems.Leds;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualIntake extends CommandBase{

    private Intake s_Intake;
    private Leds s_Leds;
    private BooleanSupplier intake;
    private BooleanSupplier outake;
    private Boolean manualMode = false;
    private boolean setIdle = false;
    
    public ManualIntake(Intake s_Intake, Leds s_Leds, BooleanSupplier intake, BooleanSupplier outake){
        this.intake = intake;
        this.outake = outake;
        this.s_Intake = s_Intake;
        this.s_Leds = s_Leds;
        addRequirements(s_Intake);
    }

    @Override
    public void execute(){

        boolean inValue = intake.getAsBoolean();
        boolean outValue = outake.getAsBoolean();

        if (manualMode && (!inValue || !outValue)) {
            if (setIdle) {
                s_Intake.intakeIdle();
                
            } else {
                s_Intake.stop();
            }
            manualMode = false;
        }

        if (inValue) {
            s_Intake.intakeFull();
            manualMode = true;
            setIdle = true;
        } else if (outValue) {
            s_Intake.outakeFull();
            s_Leds.hasPiece = false;
            manualMode = true;
            setIdle = false;
        }




        
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
