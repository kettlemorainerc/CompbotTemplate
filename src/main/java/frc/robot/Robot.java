// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Random;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.command.PIDConfigureAuto;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.FieldLocationsHelper;
import frc.robot.subsystems.FieldLocationsHelper.AngleDistance;


public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private AutonomousContol m_AutonomousContol;
  private DriveStation driveStation;
  private RobotHardware hardware;
  double timeRemaining = Timer.getMatchTime();
  double voltage = RobotController.getBatteryVoltage();
  private CommandSwerveDrivetrain drivetrain;
  int tick;
  private ElasticControls elastic;


  Random random = new Random();


  private double oldP1 = 0;
  private double oldP2 = 0;
  private double oldI1 = 0;
  private double oldI2 = 0;
  private double oldD1 = 0;
  private double oldD2 = 0;

  private float oldDriveLimit = 0.2f;
  private float oldRotateLimit = 1.0f;

  public static final Field2d m_field = new Field2d();
  public static final Field2d t_field = new Field2d();

  //PhotonCamera camera = new PhotonCamera("Camera_Module_v1");
  private SparkMax leftLauncherMotor;


  @Override public void robotInit() {
    hardware = new RobotHardware();
    driveStation = new DriveStation(hardware);
    m_AutonomousContol  = new AutonomousContol();
    drivetrain = RobotHardware.getInstance().drivetrain;


    this.drivetrain = RobotHardware.getInstance().drivetrain;

    CameraServer.startAutomaticCapture();


// TODO: THIS IS FINE, WE WILL MOVE THIS

    elastic = new ElasticControls(drivetrain);
    
    leftLauncherMotor = hardware.leftLauncherMotor;


    // TODO: We will also move this   // Do this in either robot or subsystem init
  
    SmartDashboard.putData("Field", m_field);
    SmartDashboard.putNumber("P1", 0);
    SmartDashboard.putNumber("I1", 0);
    SmartDashboard.putNumber("D1", 0);
    SmartDashboard.putNumber("P2", 0);
    SmartDashboard.putNumber("I2", 0);
    SmartDashboard.putNumber("D2", 0);

    SmartDashboard.putNumber("Drive Limit", 0.1f);
    SmartDashboard.putNumber("Rotation Limiter", 0.5f);

    SmartDashboard.putData("TargetPoseField", t_field);

    t_field.setRobotPose(FieldLocationsHelper.getHubTargetPosition());
  }

  public Robot() {
    Shuffleboard.getTab("Teleoperated").addCamera("DriverCamera", "testCamera","http://10.20.77.200:1181/stream.mjpg");
    Shuffleboard.getTab("Teleoperated").addCamera("Limelight", "FrontCamera", "http://10.20.77.20:5800");  //Would you like some marinara for you spagetti fine sir?
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
    voltage = RobotController.getBatteryVoltage();
    // Do this in either robot periodic or subsystem periodic
    
    SmartDashboard.putNumber("Match Timer", Timer.getMatchTime());
    SmartDashboard.putNumber("Motor Rpm", leftLauncherMotor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Battery Voltage", voltage);
    t_field.setRobotPose(FieldLocationsHelper.getHubTargetPosition());
    // SmartDashboard.putData("PID", PIDConfigureAuto);
  
    var state = drivetrain.getState();
    Pose2d pose = state.Pose;
    m_field.setRobotPose(pose);

    if(SmartDashboard.getNumber("P1", 0) != oldP1){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldP1 = SmartDashboard.getNumber("P1", 0);
    }
    if(SmartDashboard.getNumber("P2", 0) != oldP2){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldP2 = SmartDashboard.getNumber("P2", 0);
    }
    if(SmartDashboard.getNumber("I1", 0) != oldI1){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldI1 = SmartDashboard.getNumber("I1", 0);
    }
    if(SmartDashboard.getNumber("I2", 0) != oldI2){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldI2 = SmartDashboard.getNumber("I2", 0);
    }
    if(SmartDashboard.getNumber("D1", 0) != oldD1){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldD1 = SmartDashboard.getNumber("D1", 0);
    }
    if(SmartDashboard.getNumber("D2", 0) != oldD2){
      drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
      m_AutonomousContol.registerCommands();
      oldD2 = SmartDashboard.getNumber("D2", 0);
    }

    if(SmartDashboard.getNumber("Drive Limit", 1.0f) != oldDriveLimit){
      RobotHardware.getInstance().speedLimiterDrive = (float) SmartDashboard.getNumber("Drive Limit", 0.1f);
    }
    if(SmartDashboard.getNumber("Rotation Limit", 1.0f) != oldRotateLimit){
      RobotHardware.getInstance().speedLimiterSpin = (float) SmartDashboard.getNumber("Rotation Limit", 0.5f);
    }

    // Elastic Field with limelight

    m_field.setRobotPose(FieldLocationsHelper.getRobotFieldPose());

    Pose2d targetPose = t_field.getRobotPose();

    AngleDistance ad = FieldLocationsHelper.getDifferencePoseFromRobot(targetPose);
    SmartDashboard.putNumber("Angle", ad.robotDifferenceAngle);
    SmartDashboard.putNumber("fieldAngle", ad.fieldDifferenceAngle);
  }


  @Override
  public void disabledInit() {
  
  }

  @Override
  public void disabledPeriodic() {
  
  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_AutonomousContol.getAutonomousCommand();

    if(m_autonomousCommand != null){
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void autonomousExit() { 

  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}

}