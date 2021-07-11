package Drivers;

public class DriverCounter {
    public DriverCounter() {}

    public static int getCount() {
        return DriverCounter.numOfDrivers;
    }
    public synchronized int inc() {
        return DriverCounter.numOfDrivers++;
    }

    private static int numOfDrivers = 0;
}
