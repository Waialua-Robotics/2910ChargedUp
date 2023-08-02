package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.robot.Constants;
import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.TwinkleOffAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Leds extends SubsystemBase{
    
    private CANdle LED;
    private int animationSlot;

    public Leds(){
        LED = new CANdle(Constants.Leds.CandleID);
    }

    public void setAnimation(Animation animation){
        LED.animate(animation, animationSlot);
    }

    public void yellow(){
        LED.setLEDs(200, 90, 0);
    }

    public void purple(){
        LED.setLEDs(205, 0, 185, 150, 0, 98);
    }

    public void blue(){
        LED.setLEDs(0, 0, 255);
    }

    public void red(){
        LED.setLEDs(255, 0, 0);
    }

    public void green(){
        LED.setLEDs(0, 255, 0);
    }

    public void white(){
        LED.setLEDs(255, 250, 255);
    }

    public void rainbowAnimation(double speed){
        setAnimation(new RainbowAnimation(1, speed, 68, true, 0));
    }

    public void warningAnimation(double speed){
        setAnimation(new StrobeAnimation(255, 50, 0, 255, speed, 98));
    }

    public void yellowBlinkAnimation(double speed){
        setAnimation(new StrobeAnimation(200, 90, 0, 255, speed, 98));
    }

    public void purpleBlinkAnimation(double speed){
        setAnimation(new StrobeAnimation(205, 0, 185, 255, speed, 98));
    }

    public void zoomAnimation(double speed){
        setAnimation(new LarsonAnimation(0, 0, 255, 255, speed, 68, BounceMode.Front, 3));
    }

    public void flowAnimation(double speed){
        setAnimation(new ColorFlowAnimation(255, 0, 0, 255, speed, 38, Direction.Forward));
    }

    public void fadeAnimation(double speed){
        setAnimation(new RgbFadeAnimation(1, speed, 68, 0));
    }

    public void fireAnimation(double speed){
        setAnimation(new FireAnimation(1, speed, 68, .5, .8, false, 0));
    }
    public void twinkAnimation(double speed){
        setAnimation(new TwinkleAnimation(255, 0, 0, 255, speed, 68, TwinklePercent.Percent64 , 0));
    }
    public void twinkOffAnimation(double speed){
        setAnimation(new TwinkleOffAnimation(255, 0, 0, 255, speed, 68, TwinkleOffPercent.Percent42 , 0));
    }

    public void clearAnimation(){
        LED.clearAnimation(animationSlot);
    }

    public void clearAnimationYellow(){
        LED.clearAnimation(animationSlot);
        LED.setLEDs(200, 90, 0);
    }

    public void clearAnimationPurple(){
        LED.clearAnimation(animationSlot);
        LED.setLEDs(205, 0, 185, 150, 0, 98);
    }
}
