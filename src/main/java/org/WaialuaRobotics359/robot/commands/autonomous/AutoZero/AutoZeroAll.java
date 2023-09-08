package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;


import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class AutoZeroAll extends CommandBase {
    
    private AutoZeroPivot m_AutoZeroPivot;
    private AutoZeroArm m_AutoZeroArm;
    private AutoZeroWrist m_AutoZeroWrist;

    public AutoZeroAll(Pivot s_Pivot, Arm s_Arm, Wrist s_Wrist) {
        this.m_AutoZeroPivot = new AutoZeroPivot(s_Pivot);
        this.m_AutoZeroArm = new AutoZeroArm(s_Arm);
        this.m_AutoZeroWrist = new AutoZeroWrist(s_Wrist);
        addRequirements(s_Pivot);
        addRequirements(s_Arm);
        addRequirements(s_Wrist);
    }

    public void initialize() {
        //CommandScheduler.getInstance().schedule(m_AutoZeroPivot);
        CommandScheduler.getInstance().schedule(m_AutoZeroArm);
        CommandScheduler.getInstance().schedule(m_AutoZeroWrist);
    }

    public boolean isFinished() {
        DriverStation.reportWarning("AutoZeroing", false);
        return true;
    }
    
}