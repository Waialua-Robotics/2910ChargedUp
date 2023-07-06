package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import com.ctre.phoenix.led.CANdle;

public class Leds {
    
    private CANdle LED;

    public Leds(){
        LED = new CANdle(Constants.Leds.CandleID);
    }

    public void yellow(){
        LED.setLEDs(225, 225, 0);
    }

    public void purple(){
        LED.setLEDs(160, 32, 240);
    }

    public void blue(){
        LED.setLEDs(0, 0, 255);
    }

    public void red(){
        LED.setLEDs(255, 0, 0);
    }
}
