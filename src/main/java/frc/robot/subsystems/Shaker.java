package frc.robot.subsystems;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import frc.robot.RobotHardware;
import frc.robot.generated.TunerConstants;

public class Shaker{

    public static double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    public static final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private CommandSwerveDrivetrain drivetrain = RobotHardware.getInstance().drivetrain;
    public Shaker(){
        
    }

    public void Shake(){
        drivetrain.setControl(
                drive.withVelocityX(0) // Drive forward with negative Y (forward)
                        .withVelocityY(2) // Drive left with negative X (left)
                        .withRotationalRate(0) // Drive counterclockwise with negative X (left)
                );

    }

    public void Unshake(){
                drivetrain.setControl(
                drive.withVelocityX(0) // Drive forward with negative Y (forward)
                        .withVelocityY(-2) // Drive left with negative X (left)
                        .withRotationalRate(0) // Drive counterclockwise with negative X (left)
                );
    }

}
