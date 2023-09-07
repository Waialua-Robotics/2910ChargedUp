package org.WaialuaRobotics359.robot.subsystems;
import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
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
    private int maxSpeed = 50000;
    private int maxAcceleration = 40000;

    public Pivot(){

        m_FlPivot = new TalonFX(Constants.Pivot.Fl_PivotID);
        m_FrPivot = new TalonFX(Constants.Pivot.Fr_PivotID);
        m_BlPivot = new TalonFX(Constants.Pivot.Bl_PivotID);
        m_BrPivot = new TalonFX(Constants.Pivot.Br_PivotID);

        m_FlPivot.setNeutralMode(NeutralMode.Brake);
        m_FrPivot.setNeutralMode(NeutralMode.Brake);
        m_BlPivot.setNeutralMode(NeutralMode.Brake);
        m_BrPivot.setNeutralMode(NeutralMode.Brake);

        m_FlPivot.configFactoryDefault();
        m_FrPivot.configFactoryDefault();
        m_BlPivot.configFactoryDefault();
        m_BrPivot.configFactoryDefault();

        m_FlPivot.setInverted(TalonFXInvertType.Clockwise);

        m_FrPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);
        m_FrPivot.setInverted(TalonFXInvertType.OpposeMaster);

        m_BlPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);
        m_BlPivot.setInverted(TalonFXInvertType.FollowMaster);

        m_BrPivot.set(ControlMode.Follower, Constants.Pivot.Fl_PivotID);    
        m_BrPivot.setInverted(TalonFXInvertType.OpposeMaster);


        //Motion Magic
        m_FlPivot.setSelectedSensorPosition(0);
        m_FlPivot.configForwardSoftLimitEnable(true);
        m_FlPivot.configReverseSoftLimitEnable(true);
        m_FlPivot.configForwardSoftLimitThreshold(116000);
        m_FlPivot.configReverseSoftLimitThreshold(0);

        m_FlPivot.configMotionCruiseVelocity(maxSpeed); //70000
        m_FlPivot.configMotionAcceleration(maxAcceleration); //40000
        m_FlPivot.configMotionSCurveStrength(0);
        m_FlPivot.configPeakOutputForward(1);
        m_FlPivot.configPeakOutputReverse(-1);
 
        m_FlPivot.config_kP(0, .25);
        m_FlPivot.config_kI(0, 0);
        m_FlPivot.config_kD(0, 0);
        m_FlPivot.config_kF(0, 0);
    }

    public boolean autoPos(){
        if (Math.abs(getPosition() - Constants.Pivot.stowPosition) < 100) {
            return RobotContainer.pivotAutoStart = true;
        } else {
            return RobotContainer.pivotAutoStart = false;
        }
    }

    public void stowSpeed() {
        m_FlPivot.configMotionCruiseVelocity(50000);
        m_FlPivot.configMotionAcceleration(20000);
    }

    public void normSpeed() {
        m_FlPivot.configMotionCruiseVelocity(maxSpeed); // 70000
        m_FlPivot.configMotionAcceleration(maxAcceleration); // 40000
    }

    public void setPercentOutput(double speed){
        m_FlPivot.set(ControlMode.PercentOutput, speed);
    }

    public int backScoreRetract(){
        return Constants.Pivot.Cone.highPosition - 10000;
    }

    public int feederConeScoreRetract(){
        return Constants.Pivot.Cone.feederPosition - 10000;
    }

    public int frontScoreRetract(){
        return desiredPosition + 10000;
    }

    public boolean isRetracted(){
        return Math.abs(getPosition() - desiredPosition) < 300;
    }

    public boolean isStowed(){
        return getPosition() < 1000;
    }


    public void setCurrentPosition(){
        setDesiredPosition(getPosition());
        goToPosition();
    }

    public void setCoast(){
        m_FlPivot.setNeutralMode(NeutralMode.Coast);
        m_FrPivot.setNeutralMode(NeutralMode.Coast);
        m_BlPivot.setNeutralMode(NeutralMode.Coast);
        m_BrPivot.setNeutralMode(NeutralMode.Coast);
    }

    public void setBrake(){
        m_FlPivot.setNeutralMode(NeutralMode.Brake);
        m_FrPivot.setNeutralMode(NeutralMode.Brake);
        m_BlPivot.setNeutralMode(NeutralMode.Brake);
        m_BrPivot.setNeutralMode(NeutralMode.Brake);
    }

    public void setPosition(double pos){
        m_FlPivot.setSelectedSensorPosition(pos);
    }

    public void setDesiredPosition (int position) {
        desiredPosition = position;
    }

    public double feedForwardValue(){
        double maxGravity = .05; //percent to hold arm up
        double degrees = getPosition() / Constants.Pivot.kTicksPerDegree;
        double radiants = java.lang.Math.toRadians(degrees);
        double cosineSclaar = java.lang.Math.cos(radiants);

        return maxGravity * cosineSclaar;
    }

    public void goToPosition(){
        m_FlPivot.set(ControlMode.MotionMagic, desiredPosition, DemandType.ArbitraryFeedForward, feedForwardValue());
    }

    public boolean inPosition(){
        return Math.abs(getPosition() - desiredPosition) < 1000; 
    }

    public int getPosition(){
        return (int) m_FlPivot.getSelectedSensorPosition();
    }

    public void getPercentOutput(){
        m_FlPivot.getMotorOutputPercent();
    }

    public void percentOutput(double value){
        m_FlPivot.set(ControlMode.PercentOutput, value);
    }

    public double getCurrent(){
        return m_FlPivot.getStatorCurrent();
    } 

    public double getVelocity(){
        return m_FlPivot.getSelectedSensorVelocity();
    }
    
    public void stop() {
        m_FlPivot.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("pPosition", getPosition());
        //SmartDashboard.putNumber("pDesired", desiredPosition);
        //SmartDashboard.putNumber("pPercentOutput", m_FlPivot.getMotorOutputPercent());
        //SmartDashboard.putBoolean("RetractOnScore", RobotContainer.retractOnScore);

    }
}
