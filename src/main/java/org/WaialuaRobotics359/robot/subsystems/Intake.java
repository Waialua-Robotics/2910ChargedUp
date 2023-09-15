package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{

    private TalonFX m_Intake;

    private int desiredPosition = 0;

    public Intake (){
        m_Intake = new TalonFX(Constants.Intake.IntakeID);
        m_Intake.setNeutralMode(NeutralMode.Coast);

        //Motion magic
        m_Intake.setSelectedSensorPosition(0);
        m_Intake.configFactoryDefault();
        m_Intake.configMotionCruiseVelocity(15000);
        m_Intake.configMotionAcceleration(30000);
        m_Intake.configMotionSCurveStrength(6);
        m_Intake.configPeakOutputForward(1);
        m_Intake.configPeakOutputReverse(-1);

        m_Intake.config_kP(0, .2);
        m_Intake.config_kI(0, 0);
        m_Intake.config_kD(0, .05);
        m_Intake.config_kF(0, 0);
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public void goToPosition(){
        m_Intake.set(ControlMode.MotionMagic, desiredPosition);
    }

    public int getPosition(){
        return (int) m_Intake.getSelectedSensorPosition();
    }

    public boolean inPosition(){
        return Math.abs(getPosition() - desiredPosition) < 1000; 
    }

    public void intakeIdle(){
        m_Intake.set(ControlMode.Velocity,  RobotContainer.isCube ? 305 : -400);
        
    }

    public void getPercentOutput(){
        m_Intake.getMotorOutputPercent();
    }

    public void percentOutput(double value){
        m_Intake.set(ControlMode.PercentOutput, value);
    }

    public void intake(){
        m_Intake.set(ControlMode.PercentOutput, RobotContainer.isCube ? .7 : -.7);
    }

    public void outake(){
        m_Intake.set(ControlMode.PercentOutput,  RobotContainer.isCube ? -.5 : .5);
    }

    public void intakeFull(){
        m_Intake.set(ControlMode.PercentOutput, RobotContainer.isCube ? 1 : -.1);
    }

    public void outakeFull(){
        m_Intake.set(ControlMode.PercentOutput,  RobotContainer.isCube ? -1 : 1);
    }

    public void getCurrent(){
        m_Intake.getStatorCurrent();
    } 

    public int current(){
        return (int) m_Intake.getStatorCurrent();
    } 
    
    public void stop() {
        m_Intake.set(ControlMode.PercentOutput, 0);
    }


    @Override
    public void periodic(){
        //SmartDashboard.putNumber("iPercentOutput", m_Intake.getMotorOutputPercent());
        //SmartDashboard.putNumber("Ivelocity", m_Intake.getSelectedSensorVelocity());
        SmartDashboard.putNumber("iCurrent", m_Intake.getStatorCurrent());
        //SmartDashboard.putBoolean("Mode", RobotContainer.isCube);
        //SmartDashboard.putBoolean("KillSwitch", RobotContainer.allowScore);


    }
}
