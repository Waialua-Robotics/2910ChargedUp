package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase{
    private TalonFX m_Wrist;
    private double desiredPosition = 0;
    
    public Wrist(){
        m_Wrist = new TalonFX(Constants.Wrist.WristID);
        m_Wrist.configFactoryDefault();
    }

    public void percentOutput(double value){
        m_Wrist.set(ControlMode.PercentOutput, value);
    }

    public double getPosition(){
        double position = m_Wrist.getSelectedSensorPosition();
        return position;
    }

    public double getDesiredPosition(){
        return desiredPosition;
    }

    public void setDesiredPosition(double value){
        desiredPosition = value;
    }

    public void stop(){
        m_Wrist.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("wPercentOutput", m_Wrist.getMotorOutputPercent());
    }
}
