package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;


import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class JustZero extends CommandBase {
    
    private Pivot s_Pivot;
    private Arm s_Arm;
    private Wrist s_Wrist;

    public JustZero(Pivot s_Pivot, Arm s_Arm, Wrist s_Wrist) {
        this.s_Arm = s_Arm;
        this.s_Pivot = s_Pivot;
        this.s_Wrist = s_Wrist;
    }

    public void initialize() {
        s_Arm.stop();
        s_Arm.setPosition(0);

        //s_Pivot.stop();
        //s_Pivot.setPosition(0);

        s_Wrist.stop();
        s_Wrist.setPosition(0);

    }

    public boolean isFinished() {
        return true;
    }
    
}