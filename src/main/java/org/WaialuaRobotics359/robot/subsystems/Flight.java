package org.WaialuaRobotics359.robot.subsystems;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Flight extends SubsystemBase {
    private TimeOfFlight m_Flight;

    private boolean flightWorking = true;

    public Flight () {
        m_Flight = new TimeOfFlight(1);

        m_Flight.setRangingMode(RangingMode.Short, 200); //time 24 -1000 msec
    }

    public void working(){
        flightWorking = true;
    }

    public void notWorking(){
        flightWorking = false;
    }

    public boolean connected(){
        if(m_Flight.getAmbientLightLevel() == 0){
            return false; 
        } else { 
            return true;
        }
    }

    public double getSensorRange(){
        return flightWorking ? m_Flight.getRange(): 400;
    }

    public double offsetFromCenterIn(){
        if(getSensorRange()<365){
            return -1*((getSensorRange()-160)/1000);
        }else{
            return 0;
        }
    }

    public void periodic(){
        SmartDashboard.putNumber("PWF Distance", getSensorRange());
        //SmartDashboard.putNumber("pid", m_Flight.pidGet());
        //SmartDashboard.putNumber("standerd deveation", m_Flight.getRangeSigma());
    }
}
