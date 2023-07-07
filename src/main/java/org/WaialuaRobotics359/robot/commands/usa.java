package org.WaialuaRobotics359.robot.commands;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.subsystems.Arm;
import org.WaialuaRobotics359.robot.subsystems.Flight;
import org.WaialuaRobotics359.robot.subsystems.Intake;
import org.WaialuaRobotics359.robot.subsystems.Leds;
import org.WaialuaRobotics359.robot.subsystems.Pivot;
import org.WaialuaRobotics359.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class usa extends CommandBase {

    Leds s_Leds;

    public usa(Leds s_Leds){
        this.s_Leds = s_Leds;
    }


    private boolean finished = false;

    private Timer Timer = new Timer();

    public void initialize(){
   
        Timer.reset();
        Timer.start();

        finished = false;
    }

    @Override
    public void execute(){

        s_Leds.red();

        if(Timer.hasElapsed(.2)){
            s_Leds.white();
        }

        if(Timer.hasElapsed(.4)){
            s_Leds.blue();
        }
        finished = true;
                 
    }

    @Override
    public boolean isFinished(){
        return finished;
    }


}


