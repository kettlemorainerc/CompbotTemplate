package frc.robot.command;

import frc.robot.RobotHardware;
import frc.robot.subsystems.Shaker;

public class ShakerControl extends NewRepeatedCommand{

    private Shaker shaker = RobotHardware.getInstance().shaker;
    private int time;
    private boolean stoper; //Does not actually do anything

    public ShakerControl(boolean stopOnStop){
        this.stoper = stopOnStop;
    }
    public ShakerControl(){
        this.stoper = false;
    }

    @Override
    public void initialize(){
        time = 0;
    }

    @Override
    public void execute() {
        time++;
        if(time < 5){
            shaker.Shake();
        }else if(time > 5 && time <= 10){
            shaker.Unshake();
        }else if(time >= 10){
            time = 0;
        }
    }

    @Override
    public void end(boolean interrupted) {
        
    }

}
