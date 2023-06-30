package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase{
    private TalonFX m_lPivot;
    private TalonFX m_rPivot;

    private int desiredPosition = 0;

    public Pivot(){
        m_lPivot = new TalonFX(Constants.Pivot.lPivotID);
        m_rPivot = new TalonFX(Constants.Pivot.rPivotID);
        m_lPivot.setNeutralMode(NeutralMode.Brake);
        m_rPivot.setNeutralMode(NeutralMode.Brake);



        //Motion Magic
        m_lPivot.setSelectedSensorPosition(0);
        m_lPivot.configFactoryDefault();
        m_lPivot.configMotionCruiseVelocity(15000);
        m_lPivot.configMotionAcceleration(30000);
        m_lPivot.configMotionSCurveStrength(6);
 
        m_rPivot.setSelectedSensorPosition(0);
        m_rPivot.configFactoryDefault();
        m_rPivot.configMotionCruiseVelocity(15000);
        m_rPivot.configMotionAcceleration(30000);
        m_rPivot.configMotionSCurveStrength(6);
 
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public void goToPosition(){
        m_lPivot.set(ControlMode.MotionMagic, desiredPosition);
        m_rPivot.set(ControlMode.MotionMagic, desiredPosition);

    }

    public void getPercentOutput(){
        m_lPivot.getMotorOutputPercent();
        m_rPivot.getMotorOutputPercent();

    }

    public void percentOutput(double value){
        m_lPivot.set(ControlMode.PercentOutput, value);
        m_rPivot.set(ControlMode.PercentOutput, value);

    }

    public void getCurrent(){
        m_lPivot.getStatorCurrent();
        m_rPivot.getStatorCurrent();

    } 
    
    public void stop() {
        m_lPivot.set(ControlMode.PercentOutput, 0);
        m_rPivot.set(ControlMode.PercentOutput, 0);

    }
}
