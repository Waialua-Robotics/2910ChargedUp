package org.WaialuaRobotics359.robot.commands.Manual;
import java.util.function.DoubleSupplier;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ManualPivot extends CommandBase {
    private Pivot s_Pivot;
    private DoubleSupplier pivot;
    private boolean finished;
  

    public ManualPivot(Pivot s_Pivot, DoubleSupplier pivot) {
        this.s_Pivot = s_Pivot;
        this.pivot = pivot;
        addRequirements(s_Pivot);
    }

    public void initialize() {}

    @Override
    public void execute() {
        double joystickControl = MathUtil.applyDeadband(pivot.getAsDouble(), Constants.OI.deadband);

        if (Math.abs(joystickControl) > 0) {
            s_Pivot.percentOutput(joystickControl);
        }  else {
            s_Pivot.goToPosition(); 
           }

    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void end() {}

    public void interrupted() {}
}

