package Framework.MecanumAlg;

import Framework.Units.AngleUnits;
import Framework.Units.AngularVelocityUnits;
import Framework.Units.LengthUnits;
import Framework.Units.VelocityUnits;
import org.ejml.data.DMatrix4;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;

public class MecanumAlg {
    public MecanumAlg(double length, double height, double wheelRadius, LengthUnits units, DriveTrainDriver driver) {
        length = units.toBase(length);
        height = units.toBase(height);

        this.motorSettingMatrix = new DMatrix4x4(
                 1, 1, -(length + height) * .5, 0,
                -1, 1, -(length + height) * .5, 0,
                -1, 1,  (length + height) * .5, 0,
                 1, 1,  (length + height) * .5, 1
        );

        this.velocityMatrix = new DMatrix4x4();

        CommonOps_DDF4.invert(this.motorSettingMatrix, this.velocityMatrix);

        this.radius = units.toBase(wheelRadius);
        this.driver = driver;
    }
    public MecanumAlg() {
        this.velocityMatrix = null;
        this.motorSettingMatrix = null;
        this.radius = 0;
        this.driver = null;
     }

     public DriveTrainDriver.MotorSettingList getMotorSettingsNORM(DriveTrainDriver.NormalizedMovementParameter param) {
        DMatrix4 input = new DMatrix4(
                param.x,
                param.y,
                param.w,
                0
        );
        DMatrix4 output = new DMatrix4();

        CommonOps_DDF4.mult(this.motorSettingMatrix_normalized, input, output);

        return new DriveTrainDriver.MotorSettingList(output.a1, output.a2, output.a3, output.a4);
     }
     public DriveTrainDriver.NormalizedMovementParameter getVelocityNORM(DriveTrainDriver.MotorSettingList param) {
         DMatrix4 input = new DMatrix4(
                 param.w0,
                 param.w1,
                 param.w2,
                 param.w3
         );
         DMatrix4 output = new DMatrix4();

         CommonOps_DDF4.mult(this.velocityMatrix_normalized, input, output);

         return new DriveTrainDriver.NormalizedMovementParameter(output.a1, output.a2, output.a3);

     }

    private final DMatrix4x4 motorSettingMatrix;
    private final DMatrix4x4 velocityMatrix;

    private final DMatrix4x4 motorSettingMatrix_normalized = new DMatrix4x4(
             1, 1, -2, 0,
            -1, 1, -2, 0,
            -1, 1,  2, 0,
             1, 1,  2, 1
    );
    private final DMatrix4x4 velocityMatrix_normalized = new DMatrix4x4(
            0.5, -0.5,  0.0,  0,
            0.5,  0.0,  0.5,  0,
            0.0, -0.25, 0.25, 0,
            -1.0,  1.0, -1,    1
    );

    private final double radius;
    private final DriveTrainDriver driver;
}

//todo add non-normalized algs
//todo add mecanum in form of tasks
