package frc.robot;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.ChangeCentricity;
import frc.robot.subsystems.CommandSwerveDrivetrain;



public class RobotHardware{

    private static RobotHardware instance = null;
    public static CommandSwerveDrivetrain drivetrain = null;
    public static ChangeCentricity changeCentricity = null;

    public final SparkMax leftLauncherMotor;
    public final SparkMax rightLauncherMotor;


    static final boolean LEFT_INVERSION_STATUS = false;


    /* CAN Ordering:
     * 0-20 (Avoided to exclude legacy setups)
     * 21-30 (Swerve Drive Modules)
     *     - Even: Magnitude
     *     - Odd: Angle
     * 31-40 (Encoders+)
     * 41-50 (Reserved for future uses)
     * 50-70 (Other Motors)

    //  TODO: Turn into enums swapappable 

     * Even = Drive
     * Odd = Steer
     * A = 20 = Front Right
     * B = 22 = Back Right
     * C = 24 = Front Left
     * D = 26 = Back Left
     */

    public static RobotHardware getInstance(){
        if(instance == null) instance = new RobotHardware();
        return instance;
    }


    public float speedLimiterDrive;
    public float speedLimiterSpin;

    public RobotHardware(){
        instance = this;

        speedLimiterDrive = 0.2f;
        speedLimiterSpin = 1.0f;

        //Drive train
        drivetrain = TunerConstants.createDrivetrain();
        changeCentricity = new ChangeCentricity();


        // Launcher motors
        leftLauncherMotor = new SparkMax(50, MotorType.kBrushless);
        rightLauncherMotor = new SparkMax(51, MotorType.kBrushless);

        SparkMaxConfig leftLauncherMotorConfig = new SparkMaxConfig();
        leftLauncherMotorConfig
            .inverted(LEFT_INVERSION_STATUS)
            .idleMode(IdleMode.kCoast);
        leftLauncherMotorConfig.encoder
            .positionConversionFactor(1) //Note to future self, use a factor of 1 for standard RPM
            .velocityConversionFactor(1);
        leftLauncherMotorConfig.closedLoop
            .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
            .pid(0.0001, 0.0, 0.0)
            .velocityFF(1.0 / 5676.0); 
        leftLauncherMotor.configure(leftLauncherMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig rightLauncherMotorConfig = new SparkMaxConfig();
        rightLauncherMotorConfig
            .inverted(!LEFT_INVERSION_STATUS)
            .idleMode(IdleMode.kCoast);
        rightLauncherMotorConfig.encoder
            .positionConversionFactor(1) //Note to future self, use a factor of 1 for standard RPM
            .velocityConversionFactor(1);
        rightLauncherMotorConfig.closedLoop
            .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
            .pid(0.0001, 0.0, 0.0)
            .velocityFF(1.0 / 5676.0); 
        rightLauncherMotor.configure(rightLauncherMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    
}