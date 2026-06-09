package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RobotHardware;

import com.revrobotics.spark.SparkMax;

public class LauncherOperations implements Subsystem{
    
    RobotHardware robotHardware;
    double output;

    public LauncherOperations(){
       robotHardware = RobotHardware.getInstance();
    }

    public void startMoveTest(double rpm){
        output = Math.min(rpm, 5000);
        output = Math.max(rpm, 2000);
        robotHardware.leftLauncherMotor.getClosedLoopController().setSetpoint(output, SparkMax.ControlType.kVelocity);
        robotHardware.rightLauncherMotor.getClosedLoopController().setSetpoint(output, SparkMax.ControlType.kVelocity);
    }

    public void endMoveTest(){
        robotHardware.leftLauncherMotor.set(0);
        robotHardware.rightLauncherMotor.set(0);
    }

    public void setSpeed(int rpm){
        output = Math.min(rpm, 5000);
        output = Math.max(rpm, 0);
    }

}
