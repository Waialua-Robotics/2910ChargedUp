package org.WaialuaRobotics359.robot.subsystems;

import java.io.IOException;
import java.util.Optional;

import org.WaialuaRobotics359.robot.Constants.PhotonConstants;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.CameraLeft;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.CameraRight;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotonVision extends SubsystemBase {
    private PhotonCamera photonLeft;
    private PhotonCamera photonRight;

    private PhotonCamera[] camera;

    private Transform3d robotToCam1;
    private Transform3d robotToCam2;
    private AprilTagFieldLayout aprilTagFieldLayout;
    private PhotonPoseEstimator photonLeftPoseEstimator;
    private PhotonPoseEstimator photonRightPoseEstimator;

    public PhotonVision() {
        try { 
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        } 
        catch (IOException error) {
            error.printStackTrace();
        }
        
        camera = new PhotonCamera[2];
        camera[0] = photonLeft;
        camera[1] = photonRight;
        
        //CameraLeft
        camera[0] = new PhotonCamera("photonLeft");
        robotToCam1 = new Transform3d(new Translation3d(CameraLeft.xOffset, CameraLeft.yOffset, CameraLeft.zOffset), 
            new Rotation3d(CameraLeft.rollOffset, CameraLeft.pitchOffset, CameraLeft.yawOffset)); //insert camera pos. from center
        photonLeftPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.AVERAGE_BEST_TARGETS, camera[0], robotToCam1);

        //CameraRight
        camera[1] = new PhotonCamera("photonRight");
        robotToCam2 = new Transform3d(new Translation3d(CameraRight.xOffset, CameraRight.yOffset, CameraRight.zOffset), 
            new Rotation3d(CameraRight.rollOffset, CameraRight.pitchOffset, CameraRight.yawOffset)); //insert camera pos. from center
        photonRightPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.AVERAGE_BEST_TARGETS, camera[1], robotToCam2);

        ConfigStart();
        System.out.println("PhotonConfig");
    }

    public void ConfigStart(){
        setPipeline(0, 0);
        //setLEDs(0, CameraLeft.LEDPipe);

        setPipeline(1, 0);
        //setLEDs(1, CameraRight.LEDPipe);
    }

    public double getYaw(int num) {
        if (!camera[num].getLatestResult().hasTargets()) {
            return 0;
        }
        return camera[num].getLatestResult().getBestTarget().getYaw();
    }

    public double getPitch(int num) {
        if (!camera[num].getLatestResult().hasTargets()) {
            return 0;
        }
        return camera[num].getLatestResult().getBestTarget().getPitch();
    }

    public double getDistance(int num) {
        if (!camera[num].getLatestResult().hasTargets()) {
            return 0;
        }
        return camera[num].getLatestResult().getBestTarget().getBestCameraToTarget().getX();
    }

    public double getPoseAmbiguity(int cam){
        if (!camera[cam].getLatestResult().hasTargets()) {
            return 0;
        }
        return camera[cam].getLatestResult().getBestTarget().getPoseAmbiguity();
    }

    public double getLatency(int num) {
        return camera[num].getLatestResult().getLatencyMillis();
    }

    public double getLatencySec(int num){
        return Units.millisecondsToSeconds(getLatency(num));
    }

    public boolean hasTarget(int num) {
        return camera[num].getLatestResult().hasTargets();
    }

    public void setPipeline(int num, int pipeline) {
        camera[num].setPipelineIndex(pipeline);
    }

    public double getPipeline(int num){
        return camera[num].getPipelineIndex();
    }

    public Optional<EstimatedRobotPose> getEstimatedPose(int num) {
        /* 
        if (!hasTarget(num)) {
            return Optional.empty();
        }
        */
        if (num == 0) {
            return photonLeftPoseEstimator.update();
        }else{
            return photonRightPoseEstimator.update();
        }
        
    }

    public void setLEDs(int num, int LEDmode){
        VisionLEDMode[] LEDstate = {VisionLEDMode.kDefault, VisionLEDMode.kOff, VisionLEDMode.kBlink, VisionLEDMode.kOn};
        camera[num].setLED(LEDstate[LEDmode]);
    }

    public void toggleDriver(int num){
        camera[num].setDriverMode(!camera[num].getDriverMode());
    }

    public boolean GetDriverMode(int num){
        return camera[num].getDriverMode();
    }

    @Override
    public void periodic() {
    }    
}
