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
    
    public ManualIntake(Intake s_Intake, BooleanSupplier intake, BooleanSupplier outake){
        this.intake = intake;
        this.outake = outake;
        this.s_Intake = s_Intake;
        addRequirements(s_Intake);
    }

    @Override
    public void execute(){

        boolean lBumperControl = intake.getAsBoolean();
        boolean rBumperControl = outake.getAsBoolean();

        if(rBumperControl){
            s_Intake.percentOutput(1);
        } else if(lBumperControl){
            s_Intake.percentOutput(-1);
        } else {
            s_Intake.stop();
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
