package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.DoubleSupplier;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Arm;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualArm extends CommandBase{

    private Arm s_Arm;
    private DoubleSupplier arm;
    
    public ManualArm(Arm s_Arm, DoubleSupplier arm){
        this.arm = arm;
        this.s_Arm = s_Arm;
        addRequirements(s_Arm);
    }

    @Override
    public void execute(){

        double joystickControl = MathUtil.applyDeadband(arm.getAsDouble(), Constants.OI.deadband);

        if(Math.abs(joystickControl) > 0){
            s_Arm.percentOutput(joystickControl*.01);
        } else {
            s_Arm.stop();
        }
    }
    

    @Override
    public boolean isFinished(){
        return false;
    }
}

