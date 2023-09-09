package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoZeroArm extends CommandBase {
    private Arm s_Arm;
    private Timer Timer = new Timer();

    private double currentLimit = 20;
    private double velocityChange = 40;

    public AutoZeroArm(Arm s_Arm) {
        this.s_Arm = s_Arm;
        addRequirements(s_Arm);
    }

    @Override
    public void initialize() {
        s_Arm.setPosition(Constants.Pivot.softMax +1000);
          
        Timer.reset();
        Timer.start();

    }

    @Override
    public void execute() {
        s_Arm.setPercentOutput(-.1);
    }
    
    @Override
    public boolean isFinished(){
        return s_Arm.getCurrent() > currentLimit && Math.abs(s_Arm.getVelocity()) < velocityChange && Timer.hasElapsed(.2) || Timer.hasElapsed(3);
    }

    @Override 
    public void end(boolean interupted) {
        s_Arm.stop();
        s_Arm.setPosition(0);
        s_Arm.setDesiredPosition(0);
    }
}
