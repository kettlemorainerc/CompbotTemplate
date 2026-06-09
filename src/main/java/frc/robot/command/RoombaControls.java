package frc.robot.command;

import frc.robot.RobotHardware;
import frc.robot.subsystems.Roomba;

public class RoombaControls extends NewRepeatedCommand {
    public enum RoombaDirection{
        START,
        REVERSE,
        STOP
    }

    private final RoombaDirection direction;
    private final Roomba roomba;
    private final boolean auto;

    public RoombaControls(RoombaDirection direction){
        this.direction = direction;
        roomba = RobotHardware.getInstance().roomba;
        this.auto = false;
    }

    public RoombaControls(RoombaDirection direction, Boolean auto){
        this.direction = direction;
        roomba = RobotHardware.getInstance().roomba;
        this.auto = auto;
    }

    @Override
    public void initialize(){
        switch (direction) {
            case START:
                roomba.startRoomba();
                break;
            case REVERSE:
                roomba.reverseRoomba();
                break;
            case STOP:
                roomba.stopRoomba();
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
        if(!auto){
            roomba.stopRoomba();
        }
    }
    
}
