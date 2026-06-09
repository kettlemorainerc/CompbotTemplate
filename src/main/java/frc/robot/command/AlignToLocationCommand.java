package frc.robot.command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotHardware;
import frc.robot.subsystems.LauncherOperations;
import frc.robot.subsystems.MagicCarpet;


public class AlignToLocationCommand extends NewRepeatedCommand{

    // DriveTrain

    double targetX;
    double targetY;

    public AlignToLocationCommand(double x, double y) {
        // Used for doing things based on global field pose
        targetX = x;
        targetY = y;
    }
    
    @Override
    public void initialize(){

    }

    @Override
    public void execute() {
        // drivetrain.setControl(
        //         drive.withVelocityX(2) // Drive forward with negative Y (forward)
        //                 .withVelocityY(0) // Drive left with negative X (left)
        //                 .withRotationalRate(0) // Drive counterclockwise with negative X (left)
        //         );
    }

    @Override
    public void end(boolean interrupted) {

    }
}
