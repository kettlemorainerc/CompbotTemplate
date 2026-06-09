package frc.robot.util.NetworkTables;

import edu.wpi.first.wpilibj.Preferences;

public class SmartDashRobotPreference {

    private NetWorkTableNumber display;
    private String key;

    private double value = 0.0;

    public SmartDashRobotPreference(String key, double defaultValue){
        this.key = key;

        display = new NetWorkTableNumber(key, defaultValue, false);
        Preferences.initDouble(key, defaultValue);

        value = Preferences.getDouble(key, defaultValue);
        display.set(value);

        display.onChange(this::onChange);
    }

    public void onChange(){
        value = display.get();
        Preferences.setDouble(key, value);
    }

    public double get(){
        return value;
    }
}
