package frc.robot.command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.LauncherCalculator;


public class RPMChangeHolder{
    
    private double targetRPM;
    private boolean autoSpeed;

    public RPMChangeHolder(int rpm){
        this.targetRPM = rpm;
        this.autoSpeed = false;
    }

    public RPMChangeHolder(){
        this.autoSpeed = true;
    }

    public void changeRPMTarget(Double rpm){
        this.targetRPM += rpm;
        this.targetRPM = Math.min(targetRPM, 3500);
        this.targetRPM = Math.max(targetRPM, 2000);
        SmartDashboard.putNumber("Motor Max", targetRPM);
    }
    public void setRPMTarget(Double RPM){
        this.targetRPM = RPM;
        SmartDashboard.putNumber("Auto speed", targetRPM);
    }

    public double getTargetRPM(){      
        if(autoSpeed){
            this.targetRPM = LauncherCalculator.getRPMFromDistance();
        }  
        return targetRPM;
    }

}