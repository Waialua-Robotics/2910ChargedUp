package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase{
    private TalonFX m_lArm;
    private TalonFX m_rArm;

    private int desiredPosition = 0;

    public Arm(){
        m_lArm = new TalonFX(Constants.Arm.lArmID);
        m_rArm = new TalonFX(Constants.Arm.rArmID);

        m_lArm.setNeutralMode(NeutralMode.Brake);
        m_rArm.setNeutralMode(NeutralMode.Brake);

        m_lArm.configFactoryDefault();
        m_rArm.configFactoryDefault();

        m_lArm.setInverted(TalonFXInvertType.Clockwise);

        m_rArm.set(ControlMode.Follower, Constants.Arm.lArmID);    
        m_rArm.setInverted(TalonFXInvertType.FollowMaster);
       
        //Motion Magic
        m_lArm.setSelectedSensorPosition(0);
        m_lArm.configMotionCruiseVelocity(15000);
        m_lArm.configMotionAcceleration(30000);
        m_lArm.configMotionSCurveStrength(0);
        m_lArm.configForwardSoftLimitEnable(true);
        m_lArm.configReverseSoftLimitEnable(true);
        m_lArm.configForwardSoftLimitThreshold(24500);
        m_lArm.configReverseSoftLimitThreshold(100);
        m_lArm.configPeakOutputForward(.25);
        m_lArm.configPeakOutputReverse(-.1);


        m_lArm.config_kP(0, .25);
        m_lArm.config_kI(0, 0);
        m_lArm.config_kD(0, 0);
        m_lArm.config_kF(0, 0);
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public int getPosition(){
        return (int) m_lArm.getSelectedSensorPosition();
    }

    public void goToPosition(){
        m_lArm.set(ControlMode.MotionMagic, desiredPosition);
    }

    public void getPercentOutput(){
        m_lArm.getMotorOutputPercent();
    }

    public void percentOutput(double value){
        m_lArm.set(ControlMode.PercentOutput, value);
    }

    public void getCurrent(){
        m_lArm.getStatorCurrent();
    } 
    
    public void stop() {
        m_lArm.set(ControlMode.PercentOutput, 0);
    }
    
    @Override
    public void periodic(){
        SmartDashboard.putNumber("aDesiredPos", desiredPosition);
        SmartDashboard.putNumber("aPosition", getPosition());
        SmartDashboard.putNumber("aPercentOutput", m_lArm.getMotorOutputPercent());
    }
}
