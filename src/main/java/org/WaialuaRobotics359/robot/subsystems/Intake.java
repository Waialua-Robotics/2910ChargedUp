package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{

    private TalonFX m_Intake;

    public Intake (){
        m_Intake = new TalonFX(Constants.Intake.IntakeID);

        m_Intake.configFactoryDefault();
    }

    public void intake(double speed){
        m_Intake.set(ControlMode.PercentOutput, speed);
    }

    public void outake(double speed){
        m_Intake.set(ControlMode.PercentOutput, -speed);
    }

    public void getPercentOutput(){
        m_Intake.getMotorOutputPercent();
    }

    public void getCurrent(){
        m_Intake.getStatorCurrent();
    } 
    
    public void stop() {
        m_Intake.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("iPercentOutput", m_Intake.getMotorOutputPercent());
    }
}
