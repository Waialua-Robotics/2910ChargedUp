package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;


import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class AutoZeroAll extends CommandBase {
    //private Timer Timer = new Timer();
    //private boolean finished = false;
    
    private AutoZeroPivot m_AutoZeroPivot;
    private AutoZeroArm m_AutoZeroArm;
    private AutoZeroWrist m_AutoZeroWrist;
    private JustZero m_JustZero;

    public AutoZeroAll(Pivot s_Pivot, Arm s_Arm, Wrist s_Wrist) {
        this.m_AutoZeroPivot = new AutoZeroPivot(s_Pivot);
        this.m_AutoZeroArm = new AutoZeroArm(s_Arm);
        this.m_AutoZeroWrist = new AutoZeroWrist(s_Wrist);
        this.m_JustZero = new JustZero(s_Pivot, s_Arm, s_Wrist);
        addRequirements(s_Pivot);
        addRequirements(s_Arm);
        addRequirements(s_Wrist);

    }

    public void initialize() {
        //finished = false;
        //Timer.reset();
        //Timer.start();
        CommandScheduler.getInstance().schedule(m_AutoZeroPivot);
        CommandScheduler.getInstance().schedule(m_AutoZeroArm);
        CommandScheduler.getInstance().schedule(m_AutoZeroWrist);
    }

    public void execute() {
        /*if(Timer.hasElapsed(1.5)){
            CommandScheduler.getInstance().schedule(m_JustZero);
            finished = true;
        }*/

    }

    public boolean isFinished() {
        return true;
    }
    
}