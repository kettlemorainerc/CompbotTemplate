package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Distance;
import frc.robot.subsystems.FieldLocationsHelper.AngleDistance;

public class LauncherCalculator {

    public static double getRPMFromDistance() {

        Pose2d tp = FieldLocationsHelper.getHubTargetPosition();
        AngleDistance targetPose = FieldLocationsHelper.getDifferencePoseFromRobot(tp);//add their stuff here
        

        // The 'z' translation in camera space is the direct distance to the tag
        Distance distanceInMeters = targetPose.distance; //change this 
        // System.out.println(distanceInMeters);

        // Inputs
        double distanceToTarget = distanceInMeters.in(Meters); //change this also
        double launchAngleDegrees = 35.0; // degrees
        double wheelDiameter = 0.1016; // 100mm, in meters
            
        // 1. Calculate Required Velocity (v)
        double angleRad = Math.toRadians(launchAngleDegrees);
        double gravity = 9.81;
        // Simplified formula: v = sqrt(d * g / sin(2 * theta))
        double velocityRequired = Math.sqrt((distanceToTarget * gravity) / Math.sin(2 * angleRad));
            
        // 2. Convert Velocity to RPM
        // RPM = (v * 60) / (pi * Diameter)
        double distanceRPM = (velocityRequired * 60) / (Math.PI * wheelDiameter);
            
        System.out.println("Required Velocity: " + String.format("%.2f", velocityRequired) + " m/s");
        System.out.println("Required RPM: " + String.format("%.0f", distanceRPM));

        return distanceRPM + 2250;


    }
}
