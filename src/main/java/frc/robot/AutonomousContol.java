package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

public class AutonomousContol {

private SendableChooser<Command> autoChooser;

    public AutonomousContol(){
        

        //NamedCommands.registerCommand("Debug", Commands.print("DEBUG AutonomousControl"));
        


        autoChooser = AutoBuilder.buildAutoChooser();

        SmartDashboard.putData("Auto Chooser", autoChooser);

    }


    public Command getAutonomousCommand(){
        Command selected = autoChooser.getSelected();
        return selected;
    }
    public void registerCommands(){

        //NamedCommands.registerCommand("Debug", Commands.print("DEBUG registerCommands"));
        


        autoChooser = AutoBuilder.buildAutoChooser();

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }
}
