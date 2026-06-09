package frc.robot.command;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotHardware;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class PIDConfigureAuto extends NewRepeatedCommand {

    private final CommandSwerveDrivetrain drivetrain;


    DoubleSubscriber dblSub;

    public PIDConfigureAuto(){
        this.drivetrain = RobotHardware.getInstance().drivetrain;                         
    }

    @Override
    public void initialize(){
        drivetrain.configureAuto(SmartDashboard.getNumber("P1", 0), SmartDashboard.getNumber("I1", 0), SmartDashboard.getNumber("D1", 0), SmartDashboard.getNumber("P2", 0), SmartDashboard.getNumber("I2", 0), SmartDashboard.getNumber("D2", 0));
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean interrupted) {
        
    }

}
