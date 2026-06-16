/*----------------------------------------------------------------------------*/
/* Copyright (c) 2026 FRC Team 2077. All Rights Reserved.                     */
/* Open Source Software - may be modified and shared by FRC teams.            */
/*----------------------------------------------------------------------------*/

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.button.*;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.command.*;
import frc.robot.command.ChangeCentricityControl.Directionality;
import frc.robot.command.ElasticVisualsControl.SwitchTo;
import frc.robot.control.DriveJoystick;
import frc.robot.control.DriveXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AlignToAprilTags;
import frc.robot.subsystems.ChangeCentricity;
import frc.robot.subsystems.CommandSwerveDrivetrain;


/**
 * This class is intended to be the center point of defining actions that can be utilized during teleop segments of
 * control. This is where we should define what USB port joysticks should be registered as in `FRC Driver Station`'s usb
 * menu. As well as define what buttons on primary/technical driver's controllers should do what.
 * */
public class DriveStation {
    // Common controller port numbers
    // Joysticks that support rotation
    private static final int DRIVE_JOYSTICK_PORT = 0;
    private static final int DRIVE_XBOX_PORT = 1;
    private static final int FLYSKY_PORT = 2;

    // Joysticks that do not support rotation
    private static final int TECHNICAL_JOYSTICK_PORT = 4;
    private static final int NUMPAD_PORT = 5;

    private final DriveXboxController driveStick;
    private final Joystick technicalStick;

    private final XboxController halfSwitch;

    private final CommandXboxController driveNewJoystick = new CommandXboxController(0);

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    private static ChangeCentricity changeCentricity = RobotHardware.getInstance().changeCentricity;

    public static boolean isFieldCentric = false;

    CommandSwerveDrivetrain drivetrain = RobotHardware.getInstance().drivetrain;

    public DriveStation(RobotHardware hardware) {
        /** Set the driver's control method this MUST be a {@link DriveStick} implementation */
        //driveStick = getFlysky();
        //driveStick = getJoystick();
        driveStick = getXbox();
        halfSwitch = new XboxController(DRIVE_XBOX_PORT);

        /** Set the technical control method. This can be any {@link Joystick} implementation */
        //technicalStick = getTechnicalJoystick();
        technicalStick = getNumpad();

        bind(hardware);
        configureBindings();
    }

    /**
     * This method binds any subsystem's default command and bind commands to a user's chosen
     * control method.
     */
    public void bind(RobotHardware hardware) {

        // Setup basic robot movement commands
        // hardware.getPosition().setDefaultCommand(new CardinalMovement((DriveXboxController) driveStick, halfSwitch));
        // hardware.getHeading().setDefaultCommand(new RotationMovement(driveStick, halfSwitch));

        bindDriverControl(hardware, driveStick);
        bindTechnicalControl(hardware, technicalStick);
    }

    /** Bind primary driver's button commands here */
    private static void bindDriverControl(RobotHardware hardware, DriveXboxController primary) {
        new ChangeCentricityControl(Directionality.FIELD).bind(new JoystickButton(primary, 6));
        new ChangeCentricityControl(Directionality.BACKWARDS).bind(new JoystickButton(primary, 3));
        // new ShakerControl().bind(new JoystickButton(primary, 4));
        new AlignToAprilTags().bind(new JoystickButton(primary, 8));
        // new AlignToAprilTags().bind(new JoystickButton(primary, DRIVE_JOYSTICK_PORT));
        // new ChangeCentricityControl().bind(new JoystickButton((GenericHID) primary, 0));
        
    }

    /** Bind technical driver button commands here */
    private void bindTechnicalControl(RobotHardware hardware, Joystick secondary) {
        
      
    }



    /** Normal (silver/brighter) joystick that supports rotation */
    private static DriveJoystick getDriveNewJoystick() {
        return new DriveJoystick(DRIVE_JOYSTICK_PORT).setDriveSensitivity(.15, 5)
                                                     .setRotationSensitivity(.1, 1);
    }

    /** Flysky Drone Controller */
    private static DriveJoystick getFlysky() {
        return new DriveJoystick(FLYSKY_PORT, 4).setDriveSensitivity(.3, 1)
                                                .setRotationSensitivity(.05, 2.5);
    }

    private static DriveXboxController getXbox(){
        return new DriveXboxController(DRIVE_JOYSTICK_PORT).setDriveSensitivity(.25,1)
                                                       .setRotationSensitivity(.05,1);
    }

    /** Currently the darker joystick that doesn't support rotation */
    private static Joystick getTechnicalJoystick() {
        return new Joystick(TECHNICAL_JOYSTICK_PORT);
    }

    private static Joystick getNumpad() {
        return new Joystick(NUMPAD_PORT);
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed * RobotHardware.getInstance().speedLimiterDrive) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed * RobotHardware.getInstance().speedLimiterDrive) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate  * RobotHardware.getInstance().speedLimiterSpin) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public CommandXboxController getController(){
        return driveNewJoystick;
    }

}
