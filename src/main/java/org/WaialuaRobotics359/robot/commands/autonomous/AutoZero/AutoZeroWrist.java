package org.WaialuaRobotics359.robot.commands.autonomous.AutoZero;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoZeroWrist extends CommandBase {
    private boolean finished = false;

    private Wrist s_Wrist;
    private Timer Timer = new Timer();

    private double currentLimit = 10;
    private double velocityChange = 50;

    public AutoZeroWrist(Wrist s_Wrist) {
        this.s_Wrist = s_Wrist;
        addRequirements(s_Wrist);
    }

    @Override
    public void initialize() {
        finished = false;
        s_Wrist.setPosition(Constants.Pivot.softMax +1000);
          
        Timer.reset();
        Timer.start();

    }

    @Override
    public void execute() {
        if(s_Wrist.getCurrent() > currentLimit && Math.abs(s_Wrist.getVelocity()) < velocityChange && Timer.hasElapsed(.2) || Timer.hasElapsed(1.5)){
            s_Wrist.stop();
            s_Wrist.setPosition(0);
            s_Wrist.setDesiredPosition(0);
            finished = true;
        }else{
            s_Wrist.setPercentOutput(-.1);
        }
    }
    
    @Override
    public boolean isFinished(){
        return finished;
    }
}
