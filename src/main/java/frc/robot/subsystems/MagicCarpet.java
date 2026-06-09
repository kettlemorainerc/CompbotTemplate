package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.Subsystem;

public class MagicCarpet implements Subsystem {

    private final SparkMax motor;

    public MagicCarpet(){
        motor = new SparkMax(54, MotorType.kBrushless);
        SparkMaxConfig config = new SparkMaxConfig();
        config
            .inverted(false)
            .idleMode(IdleMode.kBrake);
        config.encoder
            .positionConversionFactor(1000)
            .velocityConversionFactor(1000);
        config.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(1.0, 0.0, 0.0);
        
        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void carpetFly(){
        motor.set(0.5);
    }

    public void carpetFall(){
        motor.set(-0.5);
    }

    public void stopCarpet(){
        motor.set(0);
    }

}