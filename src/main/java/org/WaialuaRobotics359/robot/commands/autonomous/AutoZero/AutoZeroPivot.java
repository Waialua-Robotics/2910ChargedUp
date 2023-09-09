package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Pivot;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoZeroPivot extends CommandBase {
    private Pivot s_Pivot;
    private Timer Timer = new Timer();

    private double currentLimit = 5;
    private double velocityChange = 50;

    public AutoZeroPivot(Pivot s_Pivot) {
        this.s_Pivot = s_Pivot;
        addRequirements(s_Pivot);
    }

    @Override
    public void initialize() {
        s_Pivot.setPosition(Constants.Pivot.softMax +1000);
          
        Timer.reset();
        Timer.start();

    }

    @Override
    public void execute() {
        s_Pivot.setPercentOutput(-.01);
    }
    
    @Override
    public boolean isFinished(){
        return s_Pivot.getCurrent() > currentLimit && Math.abs(s_Pivot.getVelocity()) < velocityChange && Timer.hasElapsed(.2) || Timer.hasElapsed(3);
    }

    @Override 
    public void end(boolean interupted) {
        s_Pivot.stop();
        s_Pivot.setPosition(0);
        s_Pivot.setDesiredPosition(0);
    }
}
