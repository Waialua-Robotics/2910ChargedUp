package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.DoubleSupplier;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualIntake extends CommandBase{

    private Intake s_Intake;
    private DoubleSupplier intakeAxis;
    private DoubleSupplier outakeAxis;
    
    public ManualIntake(Intake s_Intake, DoubleSupplier intakeAxis, DoubleSupplier outakeAxis){
        this.intakeAxis = intakeAxis;
        this.outakeAxis = outakeAxis;
        this.s_Intake = s_Intake;
        addRequirements(s_Intake);
    }

    @Override
    public void execute(){

        double rTriggerControl = MathUtil.applyDeadband(intakeAxis.getAsDouble(), Constants.OI.deadband);
        double lTriggerControl = MathUtil.applyDeadband(outakeAxis.getAsDouble(), Constants.OI.deadband);

        if(Math.abs(rTriggerControl) > 0){
            s_Intake.percentOutput(rTriggerControl);
        } else if(Math.abs(lTriggerControl) > 0){
            s_Intake.percentOutput(lTriggerControl);
        } else {
            s_Intake.stop();
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
