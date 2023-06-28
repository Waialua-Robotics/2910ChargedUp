package org.WaialuaRobotics359.robot.subsystems;

import java.io.IOException;
import java.util.Optional;

import org.WaialuaRobotics359.robot.Constants.PhotonConstants;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.Camera1;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.Camera2;
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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotonVision extends SubsystemBase {
    private PhotonCamera photonCam1;
    private PhotonCamera photonCam2;

    private PhotonCamera[] camera;

    private Transform3d robotToCam1;
    private Transform3d robotToCam2;
    private AprilTagFieldLayout aprilTagFieldLayout;
    private PhotonPoseEstimator photonCam1PoseEstimator;
    private PhotonPoseEstimator photonCam2PoseEstimator;

    public PhotonVision() {
        try { 
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        } 
        catch (IOException error) {
            error.printStackTrace();
        }
        
        camera = new PhotonCamera[2];
        camera[0] = photonCam1;
        camera[1] = photonCam2;
        
        //Camera1
        camera[0] = new PhotonCamera("camera1");
        robotToCam1 = new Transform3d(new Translation3d(Camera1.xOffset, Camera1.yOffset, Camera1.zOffset), 
            new Rotation3d(Camera1.rollOffset, Camera1.pitchOffset, Camera1.yawOffset)); //insert camera pos. fromm center
        photonCam1PoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.AVERAGE_BEST_TARGETS, camera[0], robotToCam1);

        //Camera2
        camera[1] = new PhotonCamera("camera2");
        robotToCam2 = new Transform3d(new Translation3d(Camera2.xOffset, Camera2.yOffset, Camera2.zOffset), 
            new Rotation3d(Camera2.rollOffset, Camera2.pitchOffset, Camera2.yawOffset)); //insert camera pos. fromm center
        photonCam1PoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.AVERAGE_BEST_TARGETS, camera[1], robotToCam2);

        ConfigStart();
        System.out.println("PhotonConfig");
    }

    public void ConfigStart(){
        setPipeline(0, 2);
        setLEDs(0, Camera1.LEDPipe);

        setPipeline(1, 2);
        setLEDs(1, Camera2.LEDPipe);
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
        if (!hasTarget(num)) {
            return null;
        }
        if (num == 0) {
            return photonCam1PoseEstimator.update();
        }
        return photonCam2PoseEstimator.update();
    }

    public void setLEDs(int num, int LEDmode){
        VisionLEDMode[] LEDstate = {VisionLEDMode.kDefault, VisionLEDMode.kOff, VisionLEDMode.kBlink, VisionLEDMode.kOn};
        camera[num].setLED(LEDstate[LEDmode]);
    }

    public double getDistance(int num) {
        if (!camera[num].getLatestResult().hasTargets()) {
            return 0;
        }
        return (PhotonConstants.cameraHeight - PhotonConstants.nodeHeight) / Math.tan(Math.toRadians(getPitch(num)));
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
