package org.WaialuaRobotics359.robot.commands.Manual;

import java.util.function.DoubleSupplier;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualWrist extends CommandBase{
    private Wrist s_Wrist;
    private boolean finished = false;

    public ManualWrist(Wrist s_Wrist, DoubleSupplier wristAxis, DoubleSupplier revWristAxis){
        this.s_Wrist = s_Wrist;

        addRequirements(s_Wrist);
    }

    public void initialize(){}

    @Override
    public void execute(){
    
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
