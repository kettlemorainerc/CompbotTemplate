package frc.robot.command;

import frc.robot.RobotHardware;
import frc.robot.subsystems.ChangeCentricity;

public class ChangeCentricityControl extends NewRepeatedCommand{
    public enum Directionality{
        FIELD,
        BACKWARDS
    }

    private final ChangeCentricity ChangeCentricity;
    private Directionality direction;

    public ChangeCentricityControl(Directionality directionality){
        System.out.println("ChangeCentricity was called "+directionality.toString());
        this.ChangeCentricity = RobotHardware.getInstance().changeCentricity;
        this.direction = directionality;
    }
    
    @Override
    public void initialize(){

    }

    @Override
    public void execute() {
        if(direction == Directionality.FIELD){
            ChangeCentricity.setFieldCentric();
        }else if(direction == Directionality.BACKWARDS){
            ChangeCentricity.setBackwards();
        }

    }

    @Override
    public void end(boolean interrupted) {
    }


}
