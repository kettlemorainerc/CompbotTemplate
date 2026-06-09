package frc.robot.command;

import frc.robot.RobotHardware;
import frc.robot.subsystems.LauncherCalculator;
import frc.robot.subsystems.LauncherOperations;


//Max RPM of The Default NEO Spark Max's is 5,676 at 12Volts
//90% is 5,106.4


public class LauncherControl extends NewRepeatedCommand{
    private final LauncherOperations launcher;
    private final double changeByRPM;
    private final RPMChangeHolder holder;
    private final boolean isChanger;
    private final boolean auto;
    private final boolean isSetter;

    public LauncherControl(Double rpm, RPMChangeHolder holder, boolean set){
        this.launcher = RobotHardware.getInstance().launcherOperations;
        this.changeByRPM = rpm;
        this.holder = holder;
        this.isChanger = !set;
        this.isSetter = set;
        this.auto = false;
    }

    public LauncherControl(RPMChangeHolder holder){
        this.launcher = RobotHardware.getInstance().launcherOperations;
        this.changeByRPM = 0;
        this.holder = holder;
        this.isChanger = false;
        this.isSetter = false;
        this.auto = false;
    }

    public LauncherControl(RPMChangeHolder holder, boolean auto){
        this.launcher = RobotHardware.getInstance().launcherOperations;
        this.changeByRPM = 0;
        this.holder = holder;
        this.isChanger = false;
        this.isSetter = false;
        this.auto = auto;
    }
    

    @Override
    public void initialize(){
        if(isChanger){
            holder.changeRPMTarget(changeByRPM);
        } else if(isSetter){
            holder.setRPMTarget(changeByRPM);
        }else   {
            launcher.startMoveTest(holder.getTargetRPM());
        }
    }

    @Override
    public void execute() {
        if(!isChanger && !isSetter){
            launcher.startMoveTest(holder.getTargetRPM());
        }else if(isSetter){
            holder.setRPMTarget(LauncherCalculator.getRPMFromDistance());
        }
    }

    @Override
    public void end(boolean interrupted) {
        if(isChanger){
            
        } else {
            if(!auto){
                launcher.endMoveTest();
            }
        }
    }
}
