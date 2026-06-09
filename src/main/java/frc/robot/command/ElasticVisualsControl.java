package frc.robot.command;

import frc.robot.util.Elastic;

public class ElasticVisualsControl extends NewRepeatedCommand{

    public enum SwitchTo{
        MAIN,
        FIELD
    }

    private final SwitchTo tab;
    


    public ElasticVisualsControl(SwitchTo tab){
        this.tab = tab;      
    }


    public void switchTabField(){
        Elastic.selectTab("Field");
    }

    public void switchTabMain(){
        Elastic.selectTab("Main");
    }



    @Override
    public void initialize(){
        switch(tab) {
            case MAIN:
                switchTabMain();
                break;
            case FIELD:
                switchTabField();
                break;
        
            default:
                break;
        }

    }

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {

	}
}
