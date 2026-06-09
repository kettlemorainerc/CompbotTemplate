package frc.robot.command;

import frc.robot.RobotHardware;
import frc.robot.subsystems.MagicCarpet;

public class MagicCarpetControls extends NewRepeatedCommand {
    public enum CarpetDirection{
        FLY,
        FALL,
        WAIT,
        STOP
    }

    private final CarpetDirection direction;
    private final MagicCarpet carpet;
    private RPMChangeHolder holder;
    private final boolean auto;

    public MagicCarpetControls(CarpetDirection direction, RPMChangeHolder holder){
        this.direction = direction;
        carpet = RobotHardware.getInstance().carpet;
        this.holder = holder;
        this.auto = false;
    }
    public MagicCarpetControls(CarpetDirection direction, Boolean auto){
        this.direction = direction;
        carpet = RobotHardware.getInstance().carpet;
        this.auto = auto;
    }


    @Override
    public void initialize(){
        switch (direction) {
            case FLY:
                carpet.carpetFall();
                break;
            case FALL:
                carpet.carpetFly();
                break;
            case STOP:
                carpet.stopCarpet();
                break;
            case WAIT:
                break;
        }
    }

    @Override
    public void execute() {
        if(direction == CarpetDirection.FLY){
            carpet.carpetFly();
        } else if(direction == CarpetDirection.FALL){
            carpet.carpetFall();
        } else if(direction == CarpetDirection.WAIT){
            if(holder.getTargetRPM() > RobotHardware.getInstance().leftLauncherMotor.getEncoder().getVelocity()) {
                
            } else if(holder.getTargetRPM() <= RobotHardware.getInstance().leftLauncherMotor.getEncoder().getVelocity()) {
                carpet.carpetFall();
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if(!auto){
            carpet.stopCarpet();
        }
    }
    
}
