package frc.robot.subsystems;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers;
import frc.robot.RobotHardware;
import frc.robot.command.NewRepeatedCommand;
import frc.robot.generated.TunerConstants;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

public class AlignToAprilTags extends NewRepeatedCommand{

    public static double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    public static final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private CommandSwerveDrivetrain drivetrain = RobotHardware.getInstance().drivetrain;
  // simple proportional turning control with Limelight.
  // "proportional control" is a control algorithm in which the output is proportional to the error.
  // in this case, we are going to return an angular velocity that is proportional to the 
  // "tx" value from the Limelight.
  double limelight_aim_proportional()
  {    
    // kP (constant of proportionality)
    // this is a hand-tuned number that determines the aggressiveness of our proportional control loop
    // if it is too high, the robot will oscillate around.
    // if it is too low, the robot will never reach its target
    // if the robot never turns in the correct direction, kP should be inverted.
    double kP = .0035;

    double angleFromTarget = FieldLocationsHelper.getDifferencePoseFromRobot(FieldLocationsHelper.getHubTargetPosition()).robotDifferenceAngle+180;
    if(angleFromTarget < -180){
      angleFromTarget = (angleFromTarget + 360);
    }
    System.out.println(angleFromTarget);
    SmartDashboard.putNumber("Angle", angleFromTarget);

    // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
    // your limelight 3 feed, tx should return roughly 31 degrees.
    double targetingAngularVelocity = (angleFromTarget) * kP;

    // convert to radians per second for our drive method
    targetingAngularVelocity *= drivetrain.MaxAngularRate;

    targetingAngularVelocity *= 1.0;
    if(targetingAngularVelocity < 1 && targetingAngularVelocity > 0){
      targetingAngularVelocity = 1;
    }else if(targetingAngularVelocity > -1 && targetingAngularVelocity < 0){
      targetingAngularVelocity = -1;
    }
    System.out.println("vel: " + targetingAngularVelocity);
    SmartDashboard.putNumber("Velocity", targetingAngularVelocity);

    return targetingAngularVelocity;
  }

  // simple proportional ranging control with Limelight's "ty" value
  // this works best if your Limelight's mount height and target mount height are different.
  // if your limelight and target are mounted at the same or similar heights, use "ta" (area) for target ranging rather than "ty"
  double limelight_range_proportional()
  {    
    double kP = .1;
    double targetingForwardSpeed = LimelightHelpers.getTY("limelight") * kP;
    targetingForwardSpeed *= drivetrain.MaxSpeed;
    targetingForwardSpeed *= -1.0;
    return targetingForwardSpeed;
  }

 
    @Override
    public void execute() {
          drivetrain.setControl(
                drive.withVelocityX(0) // Drive forward with negative Y (forward)
                        .withVelocityY(0) // Drive left with negative X (left)
                        .withRotationalRate(limelight_aim_proportional()) // Drive counterclockwise with negative X (left)
                );
    }

    @Override
    public void end(boolean interrupted) {

    }
    
}
