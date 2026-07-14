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

    /* This system is responsible for allowing the driver to change between robot centric and field centric
     * This first block of code creates two diffenet drive systems of robot and field centric respectively
     * This then allows for code to change what the primary drive type is between the two centricities
     */
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

    // Gets the drive xbox controler to allow for driving 
    private CommandSwerveDrivetrain drivetrain = RobotHardware.getInstance().drivetrain;
    private CommandXboxController driveNewJoystick = null;

    public void setDriveStick(CommandXboxController driveNewJoystick){
        this.driveNewJoystick = driveNewJoystick;
    }

    /* This sets the robot centricity to field centric
     * It flips what each control is when on blue alliance due to how the orientation is when on the field
     */
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

    // Sets robot centricity to robot centric
    public void setRobotCentric(){
        drivetrain.setControl(
            robotDrive.withVelocityX(-driveNewJoystick.getLeftY() * MaxSpeed * 0.5) // Drive forward with negative Y (forward)
                    .withVelocityY(-driveNewJoystick.getLeftX() * MaxSpeed * 0.5) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * 2) // Drive counterclockwise with negative X (left)
            );
    }

    // Sets robot centricity on the robot to backwards so that the back is now the front
    public void setBackwards(){
        drivetrain.setControl(
            robotDrive.withVelocityX(driveNewJoystick.getLeftY() * MaxSpeed * 0.5) // Drive forward with negative Y (forward)
                    .withVelocityY(driveNewJoystick.getLeftX() * MaxSpeed * 0.5) // Drive left with negative X (left)
                    .withRotationalRate(-driveNewJoystick.getRightX() * MaxAngularRate * 2) // Drive counterclockwise with negative X (left)
            );
    }


}
