package Main;

import Framework.Units.*;

public class UnitsTest {
    public static void main(String[] args) {
        System.out.println("1 M " + LengthUnits.METER.toBase(1));
        System.out.println("1 IN " + LengthUnits.IN.toBase(1));
        System.out.println("1 FT " + LengthUnits.FEET.toBase(1));
        System.out.println("1 CM " + LengthUnits.cMETER.toBase(1));
        System.out.println("1 MM " + LengthUnits.mMETER.toBase(1));

        System.out.println("_____________________________________________________");

        System.out.println("1 M " + LengthUnits.METER.toUnit(1));
        System.out.println("1 M " + LengthUnits.IN.toUnit(1));
        System.out.println("1 M " + LengthUnits.FEET.toUnit(1));
        System.out.println("1 M " + LengthUnits.cMETER.toUnit(1));
        System.out.println("1 M " + LengthUnits.mMETER.toUnit(1));

        System.out.println("_____________________________________________________");

        System.out.println("1 M/S " + VelocityUnits.MET_SEC.toBase(1));
        System.out.println("1 MPH " + VelocityUnits.MIL_HOUR.toBase(1));

        System.out.println("_____________________________________________________");

        System.out.println("1 M/S " + VelocityUnits.MET_SEC.toUnit(1));
        System.out.println("1 M/S " + VelocityUnits.MIL_HOUR.toUnit(1));

        System.out.println("_____________________________________________________");

        System.out.println("1 SEC " + TimeUnits.SEC.toUnit(1));
        System.out.println("1 MIN " + TimeUnits.MIN.toUnit(1));
        System.out.println("1 HOUR " + TimeUnits.HOUR.toUnit(1));
        System.out.println("1 mSEC " + TimeUnits.mSEC.toUnit(1));

        System.out.println("_____________________________________________________");

        System.out.println("1 SEC " + TimeUnits.SEC.toBase(1));
        System.out.println("1 SEC " + TimeUnits.MIN.toBase(1));
        System.out.println("1 SEC " + TimeUnits.HOUR.toBase(1));
        System.out.println("1 SEC " + TimeUnits.mSEC.toBase(1));

        System.out.println("_______________________________________________________");

        System.out.println("1 RAD/SEC " + AngularVelocityUnits.RAD_SEC.toBase(1));
        System.out.println("1 REV/MIN" + AngularVelocityUnits.REV_MIN.toBase(1));

        System.out.println("_______________________________________________________");

        System.out.println("1 RAD/SEC " + AngularVelocityUnits.RAD_SEC.toUnit(1));
        System.out.println("1 REV/MIN" + AngularVelocityUnits.REV_MIN.toUnit(1));

        System.out.println("_______________________________________________________");

        System.out.println("1 RAD" + AngleUnits.RAD.toBase(1));
        System.out.println("1 DEG" + AngleUnits.DEG.toBase(1));

        System.out.println("_______________________________________________________");

        System.out.println("1 RAD" + AngleUnits.RAD.toUnit(1));
        System.out.println("1 DEG" + AngleUnits.DEG.toUnit(1));


    }
}
