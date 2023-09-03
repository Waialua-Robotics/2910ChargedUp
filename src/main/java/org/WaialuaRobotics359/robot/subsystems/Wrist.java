package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase{
    private TalonFX m_Wrist;
    
    private int desiredPosition = 0;
    
    public Wrist(){
        m_Wrist = new TalonFX(Constants.Wrist.WristID);
        m_Wrist.setNeutralMode(NeutralMode.Brake);
        
        m_Wrist.setInverted(TalonFXInvertType.Clockwise);

        m_Wrist.setSelectedSensorPosition(0);
        m_Wrist.configFactoryDefault();
        m_Wrist.configMotionCruiseVelocity(15000);
        m_Wrist.configMotionAcceleration(30000);
        m_Wrist.configMotionSCurveStrength(0);
        m_Wrist.configForwardSoftLimitEnable(true);
        m_Wrist.configReverseSoftLimitEnable(true);
        m_Wrist.configForwardSoftLimitThreshold(42000);
        m_Wrist.configReverseSoftLimitThreshold(50);
        m_Wrist.configPeakOutputForward(1);
        m_Wrist.configPeakOutputReverse(-1);

        m_Wrist.config_kP(0, .25);
        m_Wrist.config_kI(0, 0);
        m_Wrist.config_kD(0, 0);
        m_Wrist.config_kF(0, 0);

    }

    public void setCoast(){
        m_Wrist.setNeutralMode(NeutralMode.Coast);
    }

    public void setBrake(){
        m_Wrist.setNeutralMode(NeutralMode.Brake);
    }

    public void setCurrentPosition(){
        desiredPosition = getPosition();
        goToPosition();
    }

    public void setPosition(double pos){
        m_Wrist.setSelectedSensorPosition(pos);
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public void goToPosition(){
        m_Wrist.set(ControlMode.MotionMagic, desiredPosition);
    }

    public void setPercentOutput(double value){
        m_Wrist.set(ControlMode.PercentOutput, value);
    }

    public int getPosition(){
        return (int) m_Wrist.getSelectedSensorPosition();
    }

    public double getVelocity(){
        return m_Wrist.getSelectedSensorVelocity();
    }

    public boolean inPosition(){
        return Math.abs(getPosition() - desiredPosition) < 1000; 
    }

    public void getPercentOutput(){
        m_Wrist.getMotorOutputPercent();
    }

    public void percentOutput(double value){
        m_Wrist.set(ControlMode.PercentOutput, value);
    }

    public double getCurrent(){
        return m_Wrist.getStatorCurrent();
    } 
    
    public void stop() {
        m_Wrist.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic(){
        //SmartDashboard.putNumber("wPercentOutput", m_Wrist.getMotorOutputPercent());
        //SmartDashboard.putNumber("wDesiredPos", desiredPosition);
        SmartDashboard.putNumber("wPosition", getPosition());
        //SmartDashboard.putNumber("wPercentOutput", m_Wrist.getMotorOutputPercent());
    }
}
