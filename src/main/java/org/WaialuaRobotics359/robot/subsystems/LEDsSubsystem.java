package org.WaialuaRobotics359.robot.subsystems;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.WaialuaRobotics359.robot.Constants;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;


public class LEDsSubsystem extends SubsystemBase {
    private final CANdle m_candle = new CANdle(Constants.LEDs.CANdleID, "rio");

    public LEDsSubsystem() {
    CANdleConfiguration config = new CANdleConfiguration();
    config.stripType = LEDStripType.RGB; // set the strip type to RGB
    config.brightnessScalar = 1; // dim the LEDs to half brightness
    m_candle.configAllSettings(config);
    LEDsDefault();
    }

    public void LEDsDefault() {
        if (DriverStation.getAlliance() == Alliance.Red) {
            LEDsRed();
        } else{
            LEDsBlue();
        }

    }

    public void LEDsRed() {
        m_candle.setLEDs(255, 0, 0);
    }

    public void LEDsBlue() {
        m_candle.setLEDs(0, 0, 255);
    }



    
}
   