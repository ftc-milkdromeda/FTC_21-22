package Framework.MecanumAlg;

import Framework.Error;

public enum MecError implements Error {

    INVALID_MOTOR_SETTINGS,
    LIST_NOT_CORRECTLY_INIT,
    MOTOR_ERROR,
    MOTOR_SETTINGS_NOT_VALID,
    ALG_ONLY_FOR_NORMALIZED,
    MOVEMENT_PARAMETERS_NOT_DEFINED,
    MOVEMENT_PARAMETERS_NOT_NORMALIZED,
    ERROR_ON_MOTOR_RESET
    ;

    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    @Override
    public String getSource() {
        return this.source;
    }

    private String source = "Mech";
}
