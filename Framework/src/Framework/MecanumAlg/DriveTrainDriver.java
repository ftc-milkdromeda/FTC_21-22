package Framework.MecanumAlg;

import Framework.Drivers.Driver;
import Framework.Drivers.DriverType;
import Framework.Error;
import Framework.GeneralError;
import Framework.Units.AngleUnits;
import Framework.Units.AngularVelocityUnits;
import Framework.Units.VelocityUnits;

public abstract class DriveTrainDriver extends Driver {
    protected DriveTrainDriver(DriverType type, double wheelRadius, double maxVelocity) {
        super(type);
        this.wheelRadius = wheelRadius;
        this.maxVelocity = maxVelocity;
    }

    public abstract Error setMotor0(double power);
    public abstract Error setMotor1(double power);
    public abstract Error setMotor2(double power);
    public abstract Error setMotor3(double power);

    public abstract double getMotor0();
    public abstract double getMotor1();
    public abstract double getMotor2();
    public abstract double getMotor3();

    public Error setMotors(MotorSettingList settings, Error[] error) {
        error[0] = setMotor0(settings.w0);
        error[1] = setMotor1(settings.w1);
        error[2] = setMotor2(settings.w2);
        error[3] = setMotor3(settings.w3);

        return  error[0] == GeneralError.NO_ERROR &&
                error[1] == GeneralError.NO_ERROR &&
                error[2] == GeneralError.NO_ERROR &&
                error[3] == GeneralError.NO_ERROR ? GeneralError.NO_ERROR : MecError.MOTOR_ERROR;
    }

    public Error reset() {
        Error[] error = {GeneralError.NO_ERROR, GeneralError.NO_ERROR, GeneralError.NO_ERROR, GeneralError.NO_ERROR};

        if(this.getMotor0() != motorSettings[0])
            error[0] = this.setMotor0(motorSettings[0]);
        if(this.getMotor1() != motorSettings[1])
            error[1] = this.setMotor0(motorSettings[1]);
        if(this.getMotor2() != motorSettings[2])
            error[2] = this.setMotor0(motorSettings[2]);
        if(this.getMotor3() != motorSettings[3])
            error[3] = this.setMotor0(motorSettings[3]);

        return  error[0] == GeneralError.NO_ERROR &&
                error[1] == GeneralError.NO_ERROR &&
                error[2] == GeneralError.NO_ERROR &&
                error[3] == GeneralError.NO_ERROR ? GeneralError.NO_ERROR : MecError.ERROR_ON_MOTOR_RESET;
    }

    public double[] getMotors() {
        return new double[] {
                this.getMotor0(),
                this.getMotor1(),
                this.getMotor2(),
                this.getMotor3()
        };
    }

    private double[] motorSettings;

    private final double wheelRadius;
    private final double maxVelocity;

    public static class MotorSettingList {
        public MotorSettingList(double w0, double w1, double w2, double w3) {
            this.w0 = w0;
            this.w1 = w1;
            this.w2 = w2;
            this.w3 = w3;
        }

        public double getW0() {
            return this.w0;
        }
        public double getW1() {
            return this.w1;
        }
        public double getW2() {
            return this.w2;
        }
        public double getW3() {
            return this.w3;
        }

        public final double w0;
        public final double w1;
        public final double w2;
        public final double w3;

    }
    public class WheelVelocity {
        public WheelVelocity(double w0, double w1, double w2, double w3, AngularVelocityUnits units) {
            this.w0 = units.toBase(w0);
            this.w1 = units.toBase(w1);
            this.w2 = units.toBase(w2);
            this.w3 = units.toBase(w3);
        }

        public double getW0(AngularVelocityUnits units) {
            return units.toUnit(w0);
        }
        public double getW1(AngularVelocityUnits units) {
            return units.toUnit(w1);
        }
        public double getW2(AngularVelocityUnits units) {
            return units.toUnit(w2);
        }
        public double getW3(AngularVelocityUnits units) {
            return units.toUnit(w3);
        }

        public final double w0;
        public final double w1;
        public final double w2;
        public final double w3;
    }

    public class MovementParameter {
        public MovementParameter(double x, double y, VelocityUnits vel_units, double w, AngularVelocityUnits w_units) {
            this.x = vel_units.toBase(x);
            this.y = vel_units.toBase(y);
            this.w = w_units.toBase(w);
        }
        public MovementParameter(double r, VelocityUnits vel_units, double theta, AngleUnits theta_units, double w, AngularVelocityUnits w_units) {
            this(r * Math.cos(theta_units.toBase(theta)), r * Math.sin(theta_units.toBase(theta)), vel_units, w, w_units);
        }

        public double getX(VelocityUnits units) {
            return units.toUnit(this.x);
        }
        public double getY(VelocityUnits units) {
            return units.toUnit(this.y);
        }
        public double getW(AngularVelocityUnits units) {
            return units.toUnit(this.w);
        }

        public double getR(VelocityUnits units) {
            return units.toUnit(Math.sqrt(this.x * this.x + this.y * this.y));
        }
        public double getTheta(AngleUnits units) {
            return units.toUnit(Math.cos(this.x / this.getR(VelocityUnits.NORMALIZEDUNIT)) * Math.signum(this.y));
        }

        public NormalizedMovementParameter normalize() {
            return new NormalizedMovementParameter(this.x, this.y, this.w);
        }

        public final double x;
        public final double y;
        public final double w;
    }
    public static class NormalizedMovementParameter {
        public NormalizedMovementParameter(double x, double y, double w) {
            this.x = x;
            this.y = y;
            this.w = w;
        }
        public NormalizedMovementParameter(double r, double theta, AngleUnits theta_units, double w) {
            this(r * Math.cos(theta_units.toBase(theta)), r * Math.sin(theta_units.toBase(theta)), w);
        }

        public double getX() {
            return this.x;
        }
        public double getY() {
            return this.y;
        }
        public double getW() {
            return this.w;
        }

        public double getR() {
            return Math.sqrt(this.x * this.x + this.y * this.y);
        }
        public double getTheta() {
            return Math.cos(this.x / this.getR()) * Math.signum(this.y);
        }

        public final double x;
        public final double y;
        public final double w;
    }
}

//todo add normalized absolute translation