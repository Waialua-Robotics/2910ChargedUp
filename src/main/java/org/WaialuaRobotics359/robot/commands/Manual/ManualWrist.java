package org.WaialuaRobotics359.robot.commands.Manual;

import java.util.function.DoubleSupplier;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualWrist extends CommandBase{
    private Wrist s_Wrist;
    private DoubleSupplier wristAxis;
    private DoubleSupplier revWristAxis;
    private boolean finished = false;

    public ManualWrist(Wrist s_Wrist, DoubleSupplier wristAxis, DoubleSupplier revWristAxis){
        this.s_Wrist = s_Wrist;
        this.wristAxis = wristAxis;
        this.revWristAxis = revWristAxis;

        addRequirements(s_Wrist);
    }

    public void initialize(){}

    @Override
    public void execute(){
        double rTriggerControl = MathUtil.applyDeadband(wristAxis.getAsDouble(), Constants.OI.deadband);
        double lTriggerControl = MathUtil.applyDeadband(revWristAxis.getAsDouble(), Constants.OI.deadband);

        if(Math.abs(rTriggerControl) > 0){
            s_Wrist.percentOutput(rTriggerControl);
        } else if(Math.abs(lTriggerControl) > 0){
            s_Wrist.percentOutput(lTriggerControl);
        } else {
            s_Wrist.stop();
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
