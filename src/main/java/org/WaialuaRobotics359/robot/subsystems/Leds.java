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

  
    public void solid(Color color, Section section){
        solid(color.getRed(), color.getGreen(), color.getBlue(), section);
    }

    public void solid(int R, int G, int B, Section section){
        LED.setLEDs(R, G, B, 0, section.startOne(), section.length());
        LED.setLEDs(R, G, B, 0, section.startTwo(), section.length());
    }

    public void hasPiece(){
        hasPiece = true;
    }

    public void noPiece(){
        hasPiece = false;
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

        //update controller state
        if (DriverStation.isJoystickConnected(0) && DriverStation.isJoystickConnected(1)){
            bothControllers = true;
        }else{
            bothControllers = false;
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
        } else {
            if (DriverStation.isDisabled()) {
                if (!bothControllers) {
                    solid(Color.BLUE, Section.Right);
                    solid(Color.RED, Section.Left);
                } else if (alliance == DriverStation.Alliance.Red) {
                    solid(Color.RED, Section.OffBoard);
                } else if (alliance == DriverStation.Alliance.Blue) {
                    solid(Color.BLUE, Section.OffBoard);
                }
            } else {
                if (isCube) {
                    solid(Color.MAGENTA, Section.OffBoard);
                } else {
                    solid(Color.YELLOW, Section.OffBoard);
                }
            }
        }
    }

        /*End Periodic */

        
        /*
         * 
         * // Select LED mode
         * solid(Section.FULL, Color.kBlack); // Default to off
         * if (estopped) {
         * solid(Section.FULL, Color.kRed);
         * } else if (DriverStation.isDisabled()) {
         * if (lastEnabledAuto && Timer.getFPGATimestamp() - lastEnabledTime <
         * autoFadeMaxTime) {
         * // Auto fade
         * solid(1.0 - ((Timer.getFPGATimestamp() - lastEnabledTime) / autoFadeTime),
         * Color.kGreen);
         * 
         * } else if (lowBatteryAlert) {
         * // Low battery
         * solid(Section.FULL, Color.kOrangeRed);
         * 
         * } else if (prideLeds) {
         * // Pride stripes
         * stripes(
         * Section.FULL,
         * List.of(
         * Color.kBlack,
         * Color.kRed,
         * Color.kOrangeRed,
         * Color.kYellow,
         * Color.kGreen,
         * Color.kBlue,
         * Color.kPurple,
         * Color.kBlack,
         * new Color(0.15, 0.3, 1.0),
         * Color.kDeepPink,
         * Color.kWhite,
         * Color.kDeepPink,
         * new Color(0.15, 0.3, 1.0)),
         * 3,
         * 5.0);
         * switch (alliance) {
         * case Red:
         * solid(Section.STATIC_LOW, Color.kRed);
         * buffer.setLED(staticSectionLength, Color.kBlack);
         * break;
         * case Blue:
         * solid(Section.STATIC_LOW, Color.kBlue);
         * buffer.setLED(staticSectionLength, Color.kBlack);
         * break;
         * default:
         * break;
         * }
         * 
         * } else {
         * // Default pattern
         * switch (alliance) {
         * case Red:
         * wave(
         * Section.FULL,
         * Color.kRed,
         * Color.kBlack,
         * waveAllianceCycleLength,
         * waveAllianceDuration);
         * break;
         * case Blue:
         * wave(
         * Section.FULL,
         * Color.kBlue,
         * Color.kBlack,
         * waveAllianceCycleLength,
         * waveAllianceDuration);
         * break;
         * default:
         * wave(Section.FULL, Color.kGold, Color.kDarkBlue, waveSlowCycleLength,
         * waveSlowDuration);
         * break;
         * }
         * }
         * } else if (fallen) {
         * strobe(Section.FULL, Color.kWhite, strobeFastDuration);
         * } else if (DriverStation.isAutonomous()) {
         * wave(Section.FULL, Color.kGold, Color.kDarkBlue, waveFastCycleLength,
         * waveFastDuration);
         * if (autoFinished) {
         * double fullTime = (double) length / waveFastCycleLength * waveFastDuration;
         * solid((Timer.getFPGATimestamp() - autoFinishedTime) / fullTime,
         * Color.kGreen);
         * }
         * } else {
         * // Demo mode background
         * if (demoMode) {
         * wave(Section.FULL, Color.kGold, Color.kDarkBlue, waveSlowCycleLength,
         * waveSlowDuration);
         * }
         * 
         * // Set HP indicator
         * Color hpColor = null;
         * switch (hpGamePiece) {
         * case NONE:
         * hpColor = null;
         * break;
         * case CUBE:
         * hpColor = Color.kPurple;
         * break;
         * case CONE:
         * if (hpConeTipped) {
         * hpColor = Color.kRed;
         * } else {
         * hpColor = Color.kGold;
         * }
         * break;
         * }
         * if (hpDoubleSubstation) {
         * solid(Section.STATIC_LOW, hpColor);
         * solid(Section.STATIC_HIGH, hpColor);
         * } else if (hpThrowGamePiece) {
         * strobe(Section.STATIC, hpColor, strobeSlowDuration);
         * } else {
         * solid(Section.STATIC, hpColor);
         * }
         * 
         * // Set special modes
         * if (distraction) {
         * strobe(Section.SHOULDER, Color.kWhite, strobeFastDuration);
         * } else if (endgameAlert) {
         * strobe(Section.SHOULDER, Color.kBlue, strobeSlowDuration);
         * } else if (autoScore) {
         * rainbow(Section.SHOULDER, rainbowCycleLength, rainbowDuration);
         * } else if (gripperStopped) {
         * solid(Section.SHOULDER, Color.kGreen);
         * } else if (intakeReady) {
         * solid(Section.SHOULDER, Color.kPurple);
         * } else if (autoSubstation) {
         * rainbow(Section.SHOULDER, rainbowCycleLength, rainbowDuration);
         * }
         * }
         * 
         * // Arm coast alert
         * if (armCoast) {
         * solid(Section.STATIC, Color.kWhite);
         * }
         * 
         * // Arm estop alert
         * if (armEstopped) {
         * strobe(Section.SHOULDER, Color.kRed, strobeFastDuration);
         * }
         * 
         * // Same battery alert
         * if (sameBattery) {
         * breath(Section.STATIC_LOW, Color.kRed, Color.kBlack, breathDuration);
         * }
         * 
         * // Update LEDs
         * leds.setData(buffer);
         */


    public void clearAnimation(){
        LED.clearAnimation(animationSlot);
    }

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
                    return 0;
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
                    return 0;
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
    }
}
