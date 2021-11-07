package RobotCode.Drivers;

import Framework.Error;
import Framework.GeneralError;
import Framework.MecanumAlg.DriveTrainDriver;
import Framework.Units.AngularVelocityUnits;

public class TestMecDriver extends DriveTrainDriver {
    public TestMecDriver() {
        super(DriverDirectory.MEC_DRIVER, 1, AngularVelocityUnits.RAD_SEC);
    }

    @Override
    protected Error setLeftUpper(double power) {
        System.out.println("LEFT_UPPER: " + power);
        return GeneralError.NO_ERROR;
    }

    @Override
    protected Error setLeftLower(double power) {
        System.out.println("LEFT_LOWER: " + power);
        return GeneralError.NO_ERROR;
    }

    @Override
    protected Error setRightUpper(double power) {
        System.out.println("RIGHT_UPPER: " + power);
        return GeneralError.NO_ERROR;
    }

    @Override
    protected Error setRightLower(double power) {
        System.out.println("RIGHT_LOWER: " + power);
        return GeneralError.NO_ERROR;
    }

    @Override
    protected double getLeftUpper() {
        return 0;
    }

    @Override
    protected double getLeftLower() {
        return 0;
    }

    @Override
    protected double getRightUpper() {
        return 0;
    }

    @Override
    protected double getRightLower() {
        return 0;
    }
}
