package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.DoubleSupplier;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ManualWrist extends CommandBase {
    private Wrist s_Wrist;
    private DoubleSupplier wristAxis;
    private DoubleSupplier revWristAxis;
    private boolean finished;
  

    public ManualWrist(Wrist s_Wrist, DoubleSupplier wristAxis, DoubleSupplier revWristAxis) {
        this.s_Wrist = s_Wrist;
        this.wristAxis = wristAxis;
        this.revWristAxis = revWristAxis;
        addRequirements(s_Wrist);
    }

    public void initialize() {}

    @Override
    public void execute() {
        double lTrigger = MathUtil.applyDeadband(revWristAxis.getAsDouble(), Constants.OI.deadband);
        double rTrigger = MathUtil.applyDeadband(wristAxis.getAsDouble(), Constants.OI.deadband);

        //SmartDashboard.putNumber("lTriggerValue", lTrigger);
        //SmartDashboard.putNumber("rTriggerValue", rTrigger);


        if (Math.abs(rTrigger) > 0) {
            s_Wrist.percentOutput(rTrigger);
        }  else if (Math.abs(lTrigger) > 0){
            s_Wrist.percentOutput(lTrigger);
        } else {
            s_Wrist.stop(); 
           }

    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void end() {}

    public void interrupted() {}
}
