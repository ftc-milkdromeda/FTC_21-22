package Main;

import Framework.Error;
import Framework.GeneralError;
import Framework.MecanumAlg.DriveTrainDriver;
import Framework.MecanumAlg.MecanumAlg;
import RobotCode.Drivers.DriverDirectory;

public class MecanumTest {
    public static void main(String [] args) {
        DriveTrainDriver driver = new DriveTrainDriver(DriverDirectory.MEC_DRIVER, 1, 10) {
            @Override
            public Error setMotor0(double power) {
                System.out.println("Motor 0" + power);

                return GeneralError.NO_ERROR;
            }

            @Override
            public Error setMotor1(double power) {
                System.out.println("Motor 1" + power);

                return GeneralError.NO_ERROR;
            }

            @Override
            public Error setMotor2(double power) {
                System.out.println("Motor 2" + power);

                return GeneralError.NO_ERROR;
            }

            @Override
            public Error setMotor3(double power) {
                System.out.println("Motor 3" + power);

                return GeneralError.NO_ERROR;
            }

            @Override
            public double getMotor0() {
                return 0;
            }

            @Override
            public double getMotor1() {
                return 0;
            }

            @Override
            public double getMotor2() {
                return 0;
            }

            @Override
            public double getMotor3() {
                return 0;
            }
        };

        MecanumAlg alg = new MecanumAlg();

        DriveTrainDriver.MotorSettingList settings = alg.getMotorSettingsNORM(new DriveTrainDriver.NormalizedMovementParameter(1, 1, 1));

        System.out.println(settings.w0);
        System.out.println(settings.w1);
        System.out.println(settings.w2);
        System.out.println(settings.w3);


    }

}
