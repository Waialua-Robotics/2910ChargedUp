package org.WaialuaRobotics359.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoWait extends CommandBase {
    private Double Time;

    public AutoWait(double Time) {
        this.Time = Time; 
    }

    private Timer Timer = new Timer();

    @Override
    public void initialize() {
        Timer.reset();
        Timer.start();
    }

    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished(){
        return Timer.hasElapsed(Time);
    }

    @Override 
    public void end(boolean interupted) {}
}