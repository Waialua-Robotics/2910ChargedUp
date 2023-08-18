package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualIntake extends CommandBase{

    private Intake s_Intake;
    private BooleanSupplier intake;
    private BooleanSupplier outake;
    private Boolean manualMode = false;
    private boolean setIdle = false;
    
    public ManualIntake(Intake s_Intake, BooleanSupplier intake, BooleanSupplier outake){
        this.intake = intake;
        this.outake = outake;
        this.s_Intake = s_Intake;
        addRequirements(s_Intake);
    }

    @Override
    public void execute(){

        boolean inValue = intake.getAsBoolean();
        boolean outValue = outake.getAsBoolean();

        if(inValue || outValue && !manualMode){
            s_Intake.stop();
        }

        if (inValue) {
            s_Intake.intake();
            manualMode = true;
            setIdle = true;
        } else if (outValue) {
            s_Intake.outake();
            manualMode = true;
            setIdle = false;
        } else if (manualMode) {
            if (setIdle) {
                s_Intake.intakeIdle();
            } else {
                s_Intake.stop();
            }
            manualMode = false; 
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
