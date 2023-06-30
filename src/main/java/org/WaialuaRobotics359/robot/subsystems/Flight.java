package org.WaialuaRobotics359.robot.subsystems;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Flight extends SubsystemBase {
    private TimeOfFlight m_Flight;
    public Flight () {
        m_Flight = new TimeOfFlight(1);

        m_Flight.setRangingMode(RangingMode.Long, 200); //time 24 -1000 msec
    }

    public double getSensorRange(){
        return m_Flight.getRange();
    }

    
    public void periodic(){
        SmartDashboard.putNumber("PWF Distance", getSensorRange());
        SmartDashboard.putNumber("pid", m_Flight.pidGet());
        SmartDashboard.putNumber("standerd deveation", m_Flight.getRangeSigma());
    }
}
