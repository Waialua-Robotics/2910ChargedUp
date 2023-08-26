package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.lib.math.Conversions;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;

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

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.awt.Color;

public class Leds extends SubsystemBase{
    
    private CANdle LED;
    private int animationSlot;

    //Const
    private static final boolean paradeMode = false;
    private static final int length = 68;

    // States
    //disabled
    public boolean lowBatteryAlert = false;
    public boolean bothControllers = false; //conected
    private Alliance alliance = Alliance.Invalid; //conected
    //teleop
    public boolean isCube; //conected
    public boolean actionReady = false; //conected
    public boolean autoScore = false; 
    public boolean distraction = false;
    public boolean endgameAlert = false; //conected
    public boolean hasPiece = false; //coneccted

    //auto Maybe?
    public boolean autoFinished = false;
    public double autoFinishedTime = 0.0;
    private boolean lastEnabledAuto = false;
    private double lastEnabledTime = 0.0;   

    //Both
    private boolean estopped = false; //conected
    

    public Leds(){
        LED = new CANdle(8);
    }

    public void allOff(){
        solid(0, 0, 0, Section.All);
    }

    public void clearAnimation(int animationSlot){
        LED.clearAnimation(animationSlot);
    }
    
    /*LED Options */
    private void solid(Color color, Section section){
        solid(color.getRed(), color.getGreen(), color.getBlue(), section);
    }

    private void solid(int R, int G, int B, Section section){
        clearAnimation(section.animationSlot());
        LED.setLEDs(R, G, B, 0, section.startOne(), section.length());
        LED.setLEDs(R, G, B, 0, section.startTwo(), section.length());
    }

    private void strobe(Color color ,Section section,  double duration) {
        boolean on = ((Timer.getFPGATimestamp() % duration) / duration) > 0.5;
        solid( on ? color : color.black, section);
    }

    private void rainbow(Section section,double speed){
        LED.animate(new RainbowAnimation(1, speed, section.length(), false, section.startOne()), section.animationSlot());
        LED.animate(new RainbowAnimation(1, speed, section.length(), false, section.startTwo()), section.animationSlot());
    }

    private void ColorFlow(Color color, Section section, double speed){
        LED.animate(new ColorFlowAnimation(color.getRed(), color.getGreen(), color.getBlue(), 0, speed, section.length(), Direction.Forward, section.startOne()),section.animationSlot());
        LED.animate(new ColorFlowAnimation(color.getRed(), color.getGreen(), color.getBlue(), 0, speed, section.length(), Direction.Forward, section.startTwo()),section.animationSlot());
    }

    public void hasPiece(){
        hasPiece = true;
    }

    public void noPiece(){
        hasPiece = false;
    }

    public void DisabledLed(boolean zeroButton, boolean inBrake){

        // update controller state
        if (DriverStation.isJoystickConnected(0) && DriverStation.isJoystickConnected(1)) {
            bothControllers = true;
        } else {
            bothControllers = false;
        }

        // Update alliance color
        alliance = DriverStation.getAlliance();

        /*Button Leds */

        if(zeroButton){
            rainbow(Section.OnBoard, 1);
            //solid(Color.GREEN, Section.OnBoard);
        }else{
            if(!inBrake){
                solid(alliance == DriverStation.Alliance.Blue? Color.BLUE : Color.RED, Section.OnBoard);
            }else{
                strobe(alliance == DriverStation.Alliance.Blue? Color.BLUE : Color.RED , Section.OnBoard, 1);
            }
        }

        if(alliance == DriverStation.Alliance.Invalid){
            ColorFlow(Color.DARK_GRAY, Section.OffBoard, .3);
        } else if (!bothControllers) {
            solid(Color.BLUE, Section.Right);
            solid(Color.RED, Section.Left);
        } else if (alliance == DriverStation.Alliance.Red) {
            solid(Color.RED, Section.OffBoard);
        } else if (alliance == DriverStation.Alliance.Blue) {
            solid(Color.BLUE, Section.OffBoard);
        } else {
            ColorFlow(Color.DARK_GRAY, Section.OffBoard, .3);
        }
    }

    public void periodic() {

        //sync states
        isCube = RobotContainer.isCube;

        // Update alliance color
        if (DriverStation.isFMSAttached()) {
            alliance = DriverStation.getAlliance();
        }

        // Update auto state
        if (DriverStation.isDisabled()) {
            autoFinished = false;
        } else {
            lastEnabledAuto = DriverStation.isAutonomous();
            lastEnabledTime = Timer.getFPGATimestamp();
        }

        // Update estop state
        if (DriverStation.isEStopped()) {
            estopped = true;
        }

        //endgame alert
        if (DriverStation.isTeleopEnabled() && (DriverStation.getMatchTime() >0.0) && (Conversions.isBetween(DriverStation.getMatchTime(), 20, 30))){
            endgameAlert = true;
        }else{
            endgameAlert = false;
        }

        /* Set Leds */
        if (estopped) {
            solid(Color.RED, Section.All);
        } else if (endgameAlert) {
            rainbow(Section.OffBoard, 1);
        } else if (DriverStation.isEnabled()) {
            if (isCube) {
                if (hasPiece) {
                    strobe(Color.MAGENTA, Section.All, .3);
                } else {
                    solid(Color.MAGENTA, Section.All);
                }
            } else {
                if (hasPiece) {
                    strobe(Color.ORANGE, Section.All, .3);
                } else {
                    solid(Color.ORANGE, Section.All);
                }
            }
        }
    }

        /*End Periodic */


    /*Sections*/
    enum Section {
        OnBoard,
        OffBoard,
        Front,
        Back,
        Left,
        Right,
        All;

        private int startOne(){
            switch(this){
                case OnBoard:
                    return 0;
                case OffBoard:
                    return 8;
                case Front:
                    return 8;
                case Back:
                    return 26;
                case Left:
                    return 8;
                case Right:
                    return 38;
                case All:
                    return 0;
                default:
                    return 0;
            }
        }

        private int startTwo(){
            switch(this){
                case OnBoard:
                    return 0;
                case OffBoard:
                    return 8;
                case Front:
                    return 50;
                case Back:
                    return 38;
                case Left:
                    return 8;
                case Right:
                    return 39;
                case All:
                    return 0;
                default:
                    return 0;
            }
        }

        private int length(){
            switch(this){
                case OnBoard:
                    return 8;
                case OffBoard:
                    return 60;
                case Front:
                    return 18;
                case Back:
                    return 12;
                case Left:
                    return 30;
                case Right:
                    return 30;
                case All:
                    return 68;
                default:
                    return 0;
            }
        }

        private int animationSlot(){
            switch(this){
                case OnBoard:
                    return 0;
                case OffBoard:
                    return 1;
                case Front:
                    return 1;
                case Back:
                    return 1;
                case Left:
                    return 1;
                case Right:
                    return 1;
                default:
                    return 0;
            }
        }
    }
}
