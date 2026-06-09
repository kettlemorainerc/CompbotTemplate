package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meters;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.LimelightHelpers;
import frc.robot.RobotHardware;

public class FieldLocationsHelper implements Subsystem {

    private static FieldLocationsHelper instance = null;

    private static CommandSwerveDrivetrain drivetrain;

    public static FieldLocationsHelper getInstance(){
        if(instance == null) instance = new FieldLocationsHelper();
        return instance;
    }

    public FieldLocationsHelper(){
        instance = this;
        
    }

    public static Pose2d getRobotFieldPose() {
        if (drivetrain == null) {
            drivetrain = RobotHardware.getInstance().drivetrain;
        }

        Pose2d limelightPose = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight").pose;
        Pose2d drivetrainPose = drivetrain.getState().Pose;
  
        if(limelightPose.getX() == 0 && limelightPose.getY() == 0){
            //If limelightPose is 0 it is assumed bad and uses pigeon
            return drivetrainPose;
        }else{
            drivetrain.resetPose(limelightPose);

            return limelightPose;
        }
    }

    public static class AngleDistance{
        public double fieldDifferenceAngle;
        public double robotDifferenceAngle;
        public Distance distance;

        public AngleDistance(double fieldDifferenceAngle, double robotDifferenceAngle, Distance distance){
            this.fieldDifferenceAngle = fieldDifferenceAngle;
            this.robotDifferenceAngle = robotDifferenceAngle;
            this.distance = distance;
        }
    }

    public static AngleDistance getDifferencePoseFromRobot(Pose2d targetPose) {
        Pose2d robotPose = getRobotFieldPose();

        double xT = targetPose.getX();
        double yT = targetPose.getY();
        double xR = robotPose.getX();
        double yR = robotPose.getY();
        double robotDegrees = robotPose.getRotation().getDegrees() - 90;


        double deltaX = xT-xR;
        double deltaY = yT-yR;

        double hypotenuse = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY,2));
        Distance distance = targetPose.getMeasureX().unit().of(hypotenuse);

        double fieldDifferenceAngle = Math.atan2(xT-xR,yT-yR) *180/Math.PI;

        double robotDifferenceAngle = (fieldDifferenceAngle + robotDegrees) % 360;

        
        // System.out.println("Robo Degree: "+robotDegrees);

        return new AngleDistance(fieldDifferenceAngle, robotDifferenceAngle, distance); 
    }

    public static Pose2d getHubTargetPosition(){
        Optional<Alliance> alli = DriverStation.getAlliance();
        if(!alli.isEmpty()){
            if(alli.get() == DriverStation.Alliance.Red){
                // System.out.println("RED");
                return new Pose2d(Meters.of(12), Meters.of(4), new Rotation2d());
            }else{
                // System.out.println("BLUE");
                return new Pose2d(Meters.of(4.5), Meters.of(4), new Rotation2d());
            }
        }else{
            System.out.println("NO ALLIANCE");
            return null;
        }
    }
    
}