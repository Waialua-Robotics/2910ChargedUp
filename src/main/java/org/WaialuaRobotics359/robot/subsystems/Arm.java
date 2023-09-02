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
    private boolean brakeMode = true;

    private int maxSpeed = 85000;
    private int maxAcceleration = 85000;

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
        m_lArm.configMotionCruiseVelocity(maxSpeed);//65000
        m_lArm.configMotionAcceleration(maxAcceleration);//75000
        m_lArm.configMotionSCurveStrength(7);
        m_lArm.configForwardSoftLimitEnable(true);
        m_lArm.configReverseSoftLimitEnable(true);
        m_lArm.configForwardSoftLimitThreshold(26550);
        m_lArm.configReverseSoftLimitThreshold(0);
        m_lArm.configPeakOutputForward(1);
        m_lArm.configPeakOutputReverse(-1);


        m_lArm.config_kP(0, .25);
        m_lArm.config_kI(0, 0);
        m_lArm.config_kD(0, 0);
        m_lArm.config_kF(0, 0);
    }

    public void stowSpeed() {
        m_lArm.configMotionCruiseVelocity(85000);
        m_lArm.configMotionAcceleration(85000);
    }

    public void normSpeed() {
        m_lArm.configMotionCruiseVelocity(maxSpeed); // 70000
        m_lArm.configMotionAcceleration(maxAcceleration); // 40000
    }

    public void setCoast(){
        m_lArm.setNeutralMode(NeutralMode.Coast);
        m_rArm.setNeutralMode(NeutralMode.Coast);
    }

    public void setBrake(){
        m_lArm.setNeutralMode(NeutralMode.Brake);
        m_rArm.setNeutralMode(NeutralMode.Brake);
    }

    public void setPercentOutput(double value){
        m_lArm.set(ControlMode.PercentOutput, value);
    }

    public void setPosition(double pos){
        m_lArm.setSelectedSensorPosition(pos);
    }

    public void setCurrentPosition(){
        double currentPosition = getPosition();
        setDesiredPosition((int)currentPosition);
        goToPosition();

        currentPosition = desiredPosition;
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public int getPosition(){
        return (int) m_lArm.getSelectedSensorPosition();
    }

    public double getVelocity(){
        return m_lArm.getSelectedSensorVelocity();
    }

    public boolean inPosition(){
        return Math.abs(getPosition() - desiredPosition) < 1000; 
    }

    public boolean isRetracted(){
        return Math.abs(getPosition() - desiredPosition) < 300;
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

    public double getCurrent(){
        return m_lArm.getStatorCurrent();
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
