package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.generated.TunerConstants;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.util.Optional;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.robot.RobotHardware;

public class ChangeCentricity implements Subsystem{
    
    private RobotHardware hardware = RobotHardware.getInstance();

    public static double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    public static final SwerveRequest.RobotCentric robotDrive = new SwerveRequest.RobotCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    public static final SwerveRequest.FieldCentric fieldDrive = new SwerveRequest.FieldCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    public static SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    public static SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();


    private CommandSwerveDrivetrain drivetrain = RobotHardware.getInstance().drivetrain;
    private CommandXboxController driveNewJoystick = null;

    public void setDriveStick(CommandXboxController driveNewJoystick){
        this.driveNewJoystick = driveNewJoystick;
    }

    public void setFieldCentric(){
        drivetrain.setControl(
            fieldDrive.withVelocityX(driveNewJoystick.getLeftY() * MaxSpeed * hardware.speedLimiterDrive) // Drive forward with negative Y (forward)
                    .withVelocityY(driveNewJoystick.getLeftX() * MaxSpeed * hardware.speedLimiterDrive) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * hardware.speedLimiterSpin) // Drive counterclockwise with negative X (left)
        );
        Optional<Alliance> ally = DriverStation.getAlliance();
        if(ally.get() == Alliance.Blue){
            drivetrain.setControl(
                fieldDrive.withVelocityX(-driveNewJoystick.getLeftY() * MaxSpeed * hardware.speedLimiterDrive) // Drive forward with negative Y (forward)
                        .withVelocityY(-driveNewJoystick.getLeftX() * MaxSpeed * hardware.speedLimiterDrive) // Drive left with negative X (left)
                        .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * hardware.speedLimiterSpin) // Drive counterclockwise with negative X (left)
                );
        }else if(ally.get() == Alliance.Red){
            drivetrain.setControl(
            fieldDrive.withVelocityX(driveNewJoystick.getLeftY() * MaxSpeed * hardware.speedLimiterDrive) // Drive forward with negative Y (forward)
                    .withVelocityY(driveNewJoystick.getLeftX() * MaxSpeed * hardware.speedLimiterDrive) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * hardware.speedLimiterSpin) // Drive counterclockwise with negative X (left)
            );
        }
        
    }

    public void setRobotCentric(){
        drivetrain.setControl(
            robotDrive.withVelocityX(-driveNewJoystick.getLeftY() * MaxSpeed * 0.5) // Drive forward with negative Y (forward)
                    .withVelocityY(-driveNewJoystick.getLeftX() * MaxSpeed * 0.5) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * 2) // Drive counterclockwise with negative X (left)
            );
    }

    public void setBackwards(){
        drivetrain.setControl(
            robotDrive.withVelocityX(driveNewJoystick.getLeftY() * MaxSpeed * 0.5) // Drive forward with negative Y (forward)
                    .withVelocityY(driveNewJoystick.getLeftX() * MaxSpeed * 0.5) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * 2) // Drive counterclockwise with negative X (left)
            );
    }


}
