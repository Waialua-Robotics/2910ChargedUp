package org.WaialuaRobotics359.robot.subsystems;

import java.io.IOException;
import java.util.Optional;

import org.WaialuaRobotics359.robot.Constants;
import org.WaialuaRobotics359.robot.RobotContainer;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.CameraLeft;
import org.WaialuaRobotics359.robot.Constants.PhotonConstants.CameraRight;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
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
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotonVision extends SubsystemBase {
    private PhotonCamera photonLeft;
    private PhotonCamera photonRight;

    private Transform3d robotToCam1;
    private Transform3d robotToCam2;

    private PhotonPoseEstimator photonLeftPoseEstimator;
    private PhotonPoseEstimator photonRightPoseEstimator;

    public AprilTagFieldLayout aprilTagFieldLayout;

    private final Leds s_Leds;

    public PhotonVision(Leds s_Leds) {
        this.s_Leds = s_Leds;

        //Load AprilTagFieldLayout
        try { 
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        } 
        catch (IOException error) {
            error.printStackTrace();
        }
        
        //CameraLeft
        try {
            photonLeft = new PhotonCamera("photonLeft");
        } catch (Exception e) {
            System.out.println("Photon Left Camera Not Found");
        }

        robotToCam1 = new Transform3d(new Translation3d(CameraLeft.xOffset, CameraLeft.yOffset, CameraLeft.zOffset), 
            new Rotation3d(CameraLeft.rollOffset, CameraLeft.pitchOffset, CameraLeft.yawOffset)); //insert camera pos. from center

        photonLeftPoseEstimator = new PhotonPoseEstimator(
            aprilTagFieldLayout, 
            PoseStrategy.LOWEST_AMBIGUITY, 
            photonLeft, robotToCam1);
        
        //CameraRight
        try {
            photonRight = new PhotonCamera("photonRight");
        } catch (Exception e) {
            System.out.println("Photon Right Camera Not Found");
        }

        robotToCam2 = new Transform3d(new Translation3d(CameraRight.xOffset, CameraRight.yOffset, CameraRight.zOffset), 
            new Rotation3d(CameraRight.rollOffset, CameraRight.pitchOffset, CameraRight.yawOffset)); //insert camera pos. from center

        photonRightPoseEstimator = new PhotonPoseEstimator(
            aprilTagFieldLayout, 
            PoseStrategy.LOWEST_AMBIGUITY, 
            photonRight, 
            robotToCam2);

        //Finish Config
        ConfigStart();
        System.out.println("PhotonConfig");
    }

    public void ConfigStart(){
        photonLeft.setPipelineIndex(0);
        photonRight.setPipelineIndex(0);
    }


    // Look through both cameras and set the reference pose with
    // the camera that has the lowest ambiguity if both cameras can see a target,
    // or,
    // the camera that can see a target if only one camera can see a target
    // only if it's ambiguity is lower than the threshold

    public Optional<EstimatedRobotPose> getEstimatedRobotPose() {
        // First, update the pose estimators
        Optional<EstimatedRobotPose> PhotonLeftPose = photonLeftPoseEstimator.update();
        Optional<EstimatedRobotPose> PhotonRightPose = photonRightPoseEstimator.update();
        // Then, get the current camera data
        Optional<PhotonCamera> PhotonLeft = getPhotonLeft();
        Optional<PhotonCamera> PhotonRight = getPhotonRight();
        // Make ambiguity doubles to compare the results
        double PhotonLeftAmbiguity = 1;
        double PhotonRightAmbiguity = 1;

        // Get the ambiguity of each camera, if applicable
        if (PhotonLeft.isPresent()) {
            PhotonPipelineResult result = PhotonLeft.get().getLatestResult();
            if (result.hasTargets()) {
                PhotonLeftAmbiguity = result.getBestTarget().getPoseAmbiguity();
                s_Leds.trackApril = true;
            }
        }
        if (PhotonRight.isPresent()) {
            PhotonPipelineResult result = PhotonRight.get().getLatestResult();
            if (result.hasTargets()) {
                PhotonRightAmbiguity = result.getBestTarget().getPoseAmbiguity();
                s_Leds.trackApril = true;
            }
        }

        // If both cameras have results, and both cameras can see a target, compare them
        if (PhotonLeftPose.isPresent() && PhotonRightPose.isPresent()) {
            // Return the camera with the lower ambiguity (only if under the minimum
            // threshold)
            // Recall that the lower the ambiguity, the more confident we are in the pose
            if (PhotonLeftAmbiguity < PhotonRightAmbiguity
                    && PhotonLeftAmbiguity < Constants.PhotonConstants.AmbiguityThreshold) {
                        s_Leds.usingApril = true;
                return PhotonLeftPose;
            } else if (PhotonRightAmbiguity < Constants.PhotonConstants.AmbiguityThreshold) {
                        s_Leds.usingApril = true;
                return PhotonRightPose;
            } 
        }

        // If only the front camera has a result, and it can see a target, return it
        // * if it has low ambiguity
        if (PhotonLeftPose.isPresent() && PhotonLeftAmbiguity < Constants.PhotonConstants.AmbiguityThreshold) {
                s_Leds.usingApril = true;
            return PhotonLeftPose;
        }

        // If only the back camera has a result, and it can see a target, return it
        // * if it has low ambiguity
        if (PhotonRightPose.isPresent() && PhotonRightAmbiguity < Constants.PhotonConstants.AmbiguityThreshold) {
                s_Leds.usingApril = true;
            return PhotonRightPose;
        }
        s_Leds.usingApril = false;
        s_Leds.trackApril = false;

        // return a null Optional<EstimatedRobotPose> if no camera can see a target
        return Optional.empty();
    }

    public Optional<PhotonCamera> getPhotonLeft() {
        return Optional.ofNullable(photonLeft);
    }

    public Optional<PhotonCamera> getPhotonRight() {
        return Optional.ofNullable(photonRight);
    }

    public boolean leftConected() {
        return photonLeft.isConnected();
    }

    public boolean rightConected() {
        return photonRight.isConnected();
    }

    public double leftLatency() {
        return photonLeft.getLatestResult().getLatencyMillis();
    }

    public double rightLatency() {
        return photonRight.getLatestResult().getLatencyMillis();
    }

    @Override
    public void periodic() {
        Leds.leftConected = leftConected();
        Leds.rightConected = rightConected();
    }
}
