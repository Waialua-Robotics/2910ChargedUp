package org.WaialuaRobotics359.robot.subsystems;

import org.WaialuaRobotics359.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase{
    private TalonFX m_FlPivot;
    private TalonFX m_FrPivot;
    private TalonFX m_BlPivot;
    private TalonFX m_BrPivot;

    private int desiredPosition = 0;

    public Pivot(){

        m_FlPivot = new TalonFX(Constants.Pivot.Fl_PivotID);
        m_FrPivot = new TalonFX(Constants.Pivot.Fr_PivotID);
        m_BlPivot = new TalonFX(Constants.Pivot.Bl_PivotID);
        m_BrPivot = new TalonFX(Constants.Pivot.Bl_PivotID);

        m_FlPivot.setNeutralMode(NeutralMode.Brake);
        m_FrPivot.setNeutralMode(NeutralMode.Brake);
        m_BlPivot.setNeutralMode(NeutralMode.Brake);
        m_BrPivot.setNeutralMode(NeutralMode.Brake);

        m_FlPivot.configFactoryDefault();
        m_FrPivot.configFactoryDefault();
        m_BlPivot.configFactoryDefault();
        m_BrPivot.configFactoryDefault();

         m_FrPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);
         m_FrPivot.setInverted(TalonFXInvertType.OpposeMaster);

         m_BlPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);
         m_BlPivot.setInverted(TalonFXInvertType.FollowMaster);

         m_BrPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);    
         m_BrPivot.setInverted(TalonFXInvertType.OpposeMaster);


        //Motion Magic
        m_FlPivot.setSelectedSensorPosition(0);
        m_FlPivot.configMotionCruiseVelocity(15000);
        m_FlPivot.configMotionAcceleration(30000);
        m_FlPivot.configMotionSCurveStrength(0);
 
        m_FlPivot.config_kP(0, .25);
        m_FlPivot.config_kI(0, 0);
        m_FlPivot.config_kD(0, 0);
        m_FlPivot.config_kF(0, 0);
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public void goToPosition(){
        m_FlPivot.set(ControlMode.MotionMagic, desiredPosition);
    }

    public void getPercentOutput(){
        m_FlPivot.getMotorOutputPercent();
    }

    public void percentOutput(double value){
        m_FlPivot.set(ControlMode.PercentOutput, value);
    }

    public void getCurrent(){
        m_FlPivot.getStatorCurrent();
    } 
    
    public void stop() {
        m_FlPivot.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("PDesiredPos", desiredPosition);
        SmartDashboard.putNumber("FlPosition", m_FlPivot.getSelectedSensorPosition());
        SmartDashboard.putNumber("pDesired", desiredPosition);


    }
}
