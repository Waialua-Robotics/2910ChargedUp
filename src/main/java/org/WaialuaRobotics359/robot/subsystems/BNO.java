package org.WaialuaRobotics359.robot.subsystems;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BNO extends SubsystemBase {
    private SerialPort m_BNO;

    private double value;


    public BNO () {
        m_BNO = new SerialPort(115200, SerialPort.Port.kUSB2);
        m_BNO.enableTermination();

    }

    private String getstring(){
        return m_BNO.readString(10);   
    }

    private void getBNO(){

        value = Double.parseDouble(getstring());
    
    }




    public void periodic(){
        getBNO();
        //System.out.println(value);
        SmartDashboard.putNumber("BNOVal", value);

    }

}
