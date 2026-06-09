package frc.robot.util.NetworkTables;

import java.util.Optional;

public interface SmartDashValue<ValueType> {
    ValueType get();
    Optional<ValueType> getNullable();
    void set(ValueType to);
}
